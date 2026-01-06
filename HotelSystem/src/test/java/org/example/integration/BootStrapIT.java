package org.example.integration;

import org.example.factory.ServiceFactory;
import org.example.service.notification.NotificationService;
import org.example.service.reservation.ReservationService;
import org.example.util.ApplicationBootstrap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(ApplicationExtension.class)
public class BootStrapIT {
    @Test
    void bootstrapStartsAndStopsCorrectly() {
        ReservationService reservationService = ServiceFactory.getReservationService();
        NotificationService notificationService = ServiceFactory.getNotificationService();

        ApplicationBootstrap bootstrap = new ApplicationBootstrap();

        assertDoesNotThrow(() ->
                bootstrap.start(reservationService, notificationService)
        );

        assertDoesNotThrow(bootstrap::stop);
    }
}
