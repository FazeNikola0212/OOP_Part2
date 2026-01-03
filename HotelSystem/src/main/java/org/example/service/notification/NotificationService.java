package org.example.service.notification;

import lombok.*;
import org.example.model.client.Client;
import org.example.model.hotel.Hotel;
import org.example.model.notification.Notification;
import org.example.model.notification.NotificationType;
import org.example.model.reservation.Reservation;
import org.example.model.reservation.ReservationRoom;
import org.example.model.user.User;
import org.example.repository.hotel.HotelRepository;
import org.example.repository.notification.NotificationRepository;
import org.example.repository.reservation.ReservationRepository;
import org.example.repository.reservation.ReservationRoomRepository;
import org.example.repository.user.UserRepository;
import org.example.session.SelectedHotelHolder;
import org.example.session.Session;
import org.w3c.dom.html.HTMLFieldSetElement;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Getter
@Setter
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ReservationRoomRepository reservationRoomRepository;
    private final HotelRepository hotelRepository;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository, ReservationRoomRepository reservationRoomRepository, HotelRepository hotelRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.reservationRoomRepository = reservationRoomRepository;
        this.hotelRepository = hotelRepository;
    }

    public List<Notification> getAllNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    public void riskClientNotification(List<Client> clients) {
        List<User> receivers = userRepository.findAllReceptionistsAndManagersByHotel(SelectedHotelHolder.getHotel());

        for (Client client : clients) {
            if (!client.isRisk()) continue;

            for (User user : receivers) {
                Notification notification = Notification.builder()
                        .user(user)
                        .type(NotificationType.RISK)
                        .global(false)
                        .hotel(SelectedHotelHolder.getHotel())
                        .isRead(false)
                        .createdAt(LocalDateTime.now())
                        .message(
                                "Risk client detected: " +
                                        client.getFullName()
                        )
                        .build();

                notificationRepository.save(notification);
            }

            logRiskClientToFile(client);
        }
    }

    public void expiringReservationNotification() {
        List<Hotel> hotels = hotelRepository.findAll();

        for (Hotel hotel : hotels) {
            List<User> receivers = userRepository.findAllReceptionistsAndManagersByHotel(hotel);

            List<ReservationRoom> reservations = reservationRoomRepository.findRoomsWhichEndsAfter1Day();

            for (ReservationRoom reservation : reservations) {

                for (User user : receivers) {
                    Notification notification = Notification.builder()
                            .user(user)
                            .type(NotificationType.WARNING)
                            .global(false)
                            .hotel(hotel)
                            .createdAt(LocalDateTime.now())
                            .message("Reservation expiring: " + reservation.getReservation().getReservationNumber())
                            .isRead(false)
                            .build();

                    notificationRepository.save(notification);
                }
            }
            System.out.println("Found reservations " + reservations.size());
        }

    }

    public void markAsRead(Long notificationId) {
        Notification n = notificationRepository.findById(notificationId);
        n.setRead(true);
        notificationRepository.update(n);
    }



    private void logRiskClientToFile(Client client) {
        Path file = Paths.get("logs", "risk-clients.log");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        try {
            Files.createDirectories(file.getParent());

            String logEntry = """
                    Client: %s
                    Hotel: %s
                    Phone: %s
                    Date: %s
                    No shown count: %d
                    Client's rating: %.2f
                    
                    """.formatted(
                            client.getFullName(),
                        SelectedHotelHolder.getHotel().getName(),
                        client.getPhoneNumber(),
                        LocalDateTime.now().format(formatter),
                        client.getNoShowCount(),
                        client.getRating()
                    );

            Files.writeString(file, logEntry, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        } catch (IOException e){
            e.printStackTrace();
        }

    }

}
