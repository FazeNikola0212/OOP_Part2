package org.example.service.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.DTO.ReceptionistDetailsDTO;
import org.example.exceptions.ValidationException;
import org.example.model.user.User;
import org.example.repository.amenity.AmenityRepository;
import org.example.repository.reservation.ReservationAmenityRepository;
import org.example.repository.reservation.ReservationRepository;
import org.example.repository.user.UserRepository;
import org.example.session.SelectedHotelHolder;

public class ReceptionistService {
    private static final Logger log = LogManager.getLogger(ReceptionistService.class);
    private UserRepository userRepository;
    private ReservationRepository reservationRepository;
    private ReservationAmenityRepository reservationAmenityRepository;

    public ReceptionistService(UserRepository userRepository, ReservationRepository reservationRepository, ReservationAmenityRepository reservationAmenityRepository) {
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
        this.reservationAmenityRepository = reservationAmenityRepository;
    }

    public ReceptionistDetailsDTO getReceptionistDetails(User receptionist) {
        if (receptionist == null) {
            log.error("Receptionist has not found");
            throw new ValidationException("Receptionist has not found");
        }

        return ReceptionistDetailsDTO.builder()
                .username(receptionist.getUsername())
                .fullName(receptionist.getFullName())
                .hotelName(SelectedHotelHolder.getHotel().getName())
                .createdBy(userRepository.findCreatorByUser(receptionist))
                .revenuePrice(reservationRepository.totalRevenueByReceptionist(receptionist))
                .clientsAssigned(reservationRepository.totalGuestAssignedByReceptionist(receptionist))
                .amenitiesAssigned(reservationAmenityRepository.findCountAmenitiesAssignedByReceptionist(receptionist))
                .reservationsAssigned(reservationRepository.totalReservationsCountByReceptionist(receptionist))
                .build();

    }
}
