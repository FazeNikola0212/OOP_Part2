package org.example.service.reservation;

import jakarta.transaction.Transactional;
import org.example.DTO.PersistReservationDTO;
import org.example.DTO.ReservationAmenityDTO;
import org.example.DTO.ReservationRoomDTO;
import org.example.exceptions.ValidationException;
import org.example.model.amenity.Amenity;
import org.example.model.hotel.Hotel;
import org.example.model.reservation.Reservation;
import org.example.model.reservation.ReservationAmenity;
import org.example.model.reservation.ReservationRoom;
import org.example.model.reservation.ReservationStatus;
import org.example.model.room.Room;
import org.example.repository.amenity.AmenityRepository;
import org.example.repository.reservation.ReservationAmenityRepository;
import org.example.repository.reservation.ReservationRepository;
import org.example.repository.reservation.ReservationRoomRepository;
import org.example.session.SelectedHotelHolder;
import org.example.session.Session;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationAmenityRepository reservationAmenityRepository;
    private final ReservationRoomRepository reservationRoomRepository;
    private final AmenityRepository amenityRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationAmenityRepository reservationAmenityRepository,
                              ReservationRoomRepository reservationRoomRepository,
                              AmenityRepository amenityRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationAmenityRepository = reservationAmenityRepository;
        this.reservationRoomRepository = reservationRoomRepository;
        this.amenityRepository = amenityRepository;
    }


    //Creating Random reservation number by first 2 letters and 6-digit number connected by '-'
    public String generateReservationNumber(Hotel hotel) {
        Random random = new Random();
        int number = random.nextInt(999999);

        String[] symbols = hotel.getName().split("");

        return String.format(symbols[0].toUpperCase() + symbols[1].toUpperCase() + "-%06d", number);
    }

    public BigDecimal totalRoomPrice(Room room, LocalDateTime start, LocalDateTime end) {
        if (room == null || start == null || end == null) {
            return BigDecimal.ZERO;
        }
        long days = Duration.between(start, end).toDays();

        return room.getPricePerNight().multiply(BigDecimal.valueOf(days));
    }

    public BigDecimal totalReservationPrice(Map<Room, LocalDateTime[]> roomDates,
                                            Map<Amenity, Integer> amenityQuantities,
                                            Map<Amenity, BigDecimal> amenityPrices) {
        BigDecimal total = BigDecimal.ZERO;

        for (Room room : roomDates.keySet()) {
            LocalDateTime[] dates = roomDates.get(room);
            if (dates != null && dates[0] != null && dates[1] != null) {
                total = total.add(totalRoomPrice(room, dates[0], dates[1]));
            }
        }

        for (Amenity amenity :  amenityQuantities.keySet()) {
            int qty = amenityQuantities.getOrDefault(amenity, 0);
            BigDecimal price = amenityPrices.getOrDefault(amenity, BigDecimal.ZERO);
            total = total.add(price.multiply(BigDecimal.valueOf(qty)));
        }

        return total;
    }

    @Transactional
    public void createReservation(PersistReservationDTO dto) {
        validate(dto);
        validateNoOverlapRooms(dto);

        Reservation reservation = Reservation.builder()
                .reservationNumber(dto.getReservationNumber())
                .mainClient(dto.getMainClient())
                .guests(dto.getGuests())
                .receptionist(Session.getSession().getLoggedUser())
                .status(ReservationStatus.COMPLETED)
                .type(dto.getType())
                .terminationType(null)
                .createdAt(LocalDateTime.now())
                .hotel(SelectedHotelHolder.getHotel())
                .totalPrice(dto.getTotalPrice())
                .build();

        reservationRepository.save(reservation);

        for (ReservationAmenityDTO amenityDTO : dto.getAmenities()) {
            ReservationAmenity ra = new ReservationAmenity();
            ra.setAmenity(amenityDTO.getAmenity());
            ra.setReservation(reservation);
            ra.setPrice(amenityDTO.getPrice());
            ra.setQuantity(amenityDTO.getQuantity());

            reservationAmenityRepository.save(ra);

            int newUse = amenityDTO.getQuantity();
            Amenity amenity = amenityRepository.findByName(amenityDTO.getName()).orElseThrow(() -> new InputMismatchException("Invalid amenity"));
            int oldUse = amenity.getUsageCount();
            amenity.setUsageCount(oldUse + newUse);

            amenityRepository.update(amenity);
        }

        for (ReservationRoomDTO roomDTO : dto.getRooms()) {
            ReservationRoom rr = new ReservationRoom();
            rr.setRoom(roomDTO.getRoom());
            rr.setPrice(roomDTO.getPrice());
            rr.setReservation(reservation);
            rr.setStartDate(roomDTO.getStartDate());
            rr.setEndDate(roomDTO.getEndDate());

            reservationRoomRepository.save(rr);
        }
    }

    private void validate(PersistReservationDTO dto) {
        if (dto.getMainClient() == null)
            throw new ValidationException("Main client must be selected.");

        if (dto.getRooms() == null || dto.getRooms().isEmpty())
            throw new ValidationException("At least one room must be selected.");

        for (ReservationRoomDTO roomDTO : dto.getRooms()) {
            if (roomDTO.getStartDate() == null || roomDTO.getEndDate() == null)
                throw new ValidationException("All rooms must have valid dates.");
        }
    }

    private void validateNoOverlapRooms(PersistReservationDTO dto) {
        for (ReservationRoomDTO roomDTO : dto.getRooms()) {

            List<ReservationRoom> overlaps =
                    reservationRoomRepository.findOverlappingReservations(
                            roomDTO.getRoom(),
                            roomDTO.getStartDate(),
                            roomDTO.getEndDate());
            if (!overlaps.isEmpty()) {
                throw new ValidationException("Room " + roomDTO.getRoom().getNumber() + " is booked for the selected dates!");
            }
        }

    }

}
