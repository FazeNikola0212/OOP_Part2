package org.example.service.reservation;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.DTO.PersistReservationDTO;
import org.example.DTO.ReservationAmenityDTO;
import org.example.DTO.ReservationRoomDTO;
import org.example.DTO.ReservationRowDTO;
import org.example.exceptions.ValidationException;
import org.example.model.amenity.Amenity;
import org.example.model.client.Client;
import org.example.model.hotel.Hotel;
import org.example.model.reservation.*;
import org.example.model.room.Room;
import org.example.repository.amenity.AmenityRepository;
import org.example.repository.client.ClientRepository;
import org.example.repository.reservation.ReservationAmenityRepository;
import org.example.repository.reservation.ReservationRepository;
import org.example.repository.reservation.ReservationRoomRepository;
import org.example.session.SelectedHotelHolder;
import org.example.session.Session;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ReservationService {
    private static final Logger log = LogManager.getLogger(ReservationService.class);
    private final ReservationRepository reservationRepository;
    private final ReservationAmenityRepository reservationAmenityRepository;
    private final ReservationRoomRepository reservationRoomRepository;
    private final AmenityRepository amenityRepository;
    private final ClientRepository clientRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationAmenityRepository reservationAmenityRepository,
                              ReservationRoomRepository reservationRoomRepository,
                              AmenityRepository amenityRepository,
                              ClientRepository clientRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationAmenityRepository = reservationAmenityRepository;
        this.reservationRoomRepository = reservationRoomRepository;
        this.amenityRepository = amenityRepository;
        this.clientRepository = clientRepository;
    }

    public List<ReservationRowDTO> getReservationRows(Hotel hotel) {
        return reservationRepository.findAllByHotel(hotel)
                .stream()
                .map(r ->
                    new ReservationRowDTO(
                            r.getMainClient().getFirstName() + " " + r.getMainClient().getLastName(),
                            r.getReservationNumber(),
                            r.getTerminationType() != null ? r.getTerminationType().toString() : "-",
                            r.getType().toString(),
                            r.getCreatedAt(),
                            r.getRooms().stream().map(rr -> rr.getRoom().getNumber()).collect(Collectors.joining(", ")),
                            r.getGuests().size(),
                            r.getReceptionist().getFullName(),
                            r.getStatus().toString(),
                            r.isCheckedIn()
                    )
                ).toList();

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
                .status(ReservationStatus.ACTIVE)
                .type(dto.getType())
                .terminationType(null)
                .createdAt(LocalDateTime.now())
                .hotel(SelectedHotelHolder.getHotel())
                .totalPrice(dto.getTotalPrice())
                .isCheckedIn(false)
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

    public void terminateNoShownReservation() {
        /*List<Reservation> activeReservations = reservationRepository.findAllByReservationStatus(ReservationStatus.ACTIVE);

        for (Reservation reservation : activeReservations) {
            if (reservation.isCheckedIn()) {
                continue;
            }

            List<ReservationRoom> rooms =  reservationRoomRepository.findRoomsByReservation(reservation);

            LocalDateTime earliestStart = rooms.stream()
                    .map(ReservationRoom ::getStartDate)
                    .min(LocalDateTime :: compareTo)
                    .orElse(null);

            if (earliestStart == null) {
                continue;
            }

            if (earliestStart.isBefore(LocalDateTime.now().minusDays(2))) {

                reservation.setStatus(ReservationStatus.EXPIRED);
                reservation.setTerminationType(TerminationType.NO_SHOW);

                Client client = reservation.getMainClient();
                client.setNoShowCount(client.getNoShowCount() + 1);

                if (client.getNoShowCount() >= 2) {
                    client.setRisk(true);
                }

                reservationRepository.update(reservation);
                clientRepository.update(client);

            }

        }*/

        LocalDateTime threshold = LocalDateTime.now().minusDays(2);

        int expired = reservationRepository.expireNoShowReservations(threshold);

        if (expired == 0) {
            log.info("0 needed Reservations for terminating");
            return;
        }

        List<Client> clients = reservationRepository.findClientsWithExpiredNoShows(threshold);

        for (Client client : clients) {
            client.setNoShowCount(client.getNoShowCount() + 1);

            if (client.getNoShowCount() >= 2) {
                client.setRisk(true);
            }

            clientRepository.update(client);
            log.info("Successfully terminated reservation");
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
