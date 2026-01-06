package org.example.unit.service;

import org.example.model.client.Client;
import org.example.model.hotel.Hotel;
import org.example.model.notification.Notification;
import org.example.model.reservation.Reservation;
import org.example.model.reservation.ReservationRoom;
import org.example.model.user.User;
import org.example.repository.hotel.HotelRepository;
import org.example.repository.notification.NotificationRepository;
import org.example.repository.reservation.ReservationRoomRepository;
import org.example.repository.user.UserRepository;
import org.example.service.notification.NotificationService;
import org.example.session.SelectedHotelHolder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {
    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReservationRoomRepository reservationRoomRepository;

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void shouldReturnNotificationsByUserId() {
        Long userId = 1L;
        List<Notification> notifications = List.of(new Notification(), new Notification());

        when(notificationRepository.findByUserId(userId))
                .thenReturn(notifications);

        List<Notification> result =
                notificationService.getAllNotificationsByUserId(userId);

        assertEquals(2, result.size());
        verify(notificationRepository).findByUserId(userId);
    }

    @Test
    void shouldCreateNotificationsForRiskClient() {
        Client riskClient = mock(Client.class);
        when(riskClient.isRisk()).thenReturn(true);
        when(riskClient.getFullName()).thenReturn("John Doe");
        when(riskClient.getPhoneNumber()).thenReturn("123");
        when(riskClient.getNoShowCount()).thenReturn(3);
        when(riskClient.getRating()).thenReturn(2.5);

        Hotel hotel = mock(Hotel.class);
        when(hotel.getName()).thenReturn("Test Hotel");
        SelectedHotelHolder.setHotel(hotel);

        User user1 = new User();
        User user2 = new User();

        when(userRepository.findAllReceptionistsAndManagersByHotel(hotel))
                .thenReturn(List.of(user1, user2));

        notificationService.riskClientNotification(List.of(riskClient));

        verify(notificationRepository, times(2))
                .save(any(Notification.class));
    }

    @Test
    void shouldNotCreateNotificationForNonRiskClient() {
        Client client = mock(Client.class);
        when(client.isRisk()).thenReturn(false);

        notificationService.riskClientNotification(List.of(client));

        verify(notificationRepository, never()).save(any());
    }


    @Test
    void shouldCreateExpiringReservationNotifications() {
        Hotel hotel = new Hotel();
        when(hotelRepository.findAll()).thenReturn(List.of(hotel));

        User user = new User();
        when(userRepository.findAllReceptionistsAndManagersByHotel(hotel))
                .thenReturn(List.of(user));

        Reservation reservation = mock(Reservation.class);
        when(reservation.getReservationNumber()).thenReturn("RES-1");

        ReservationRoom room = mock(ReservationRoom.class);
        when(room.getReservation()).thenReturn(reservation);

        when(reservationRoomRepository.findRoomsWhichEndsAfter1Day())
                .thenReturn(List.of(room));

        notificationService.expiringReservationNotification();

        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void shouldMarkNotificationAsRead() {
        Notification notification = new Notification();
        notification.setRead(false);

        when(notificationRepository.findById(1L))
                .thenReturn(notification);

        notificationService.markAsRead(1L);

        assertTrue(notification.isRead());
        verify(notificationRepository).update(notification);
    }




}
