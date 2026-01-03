package org.example.service.notification;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExpiringReservationNotify implements Runnable {
    private NotificationService notificationService;
    private static final Logger log = LogManager.getLogger(ExpiringReservationNotify.class);


    public ExpiringReservationNotify(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void run() {
        notificationService.expiringReservationNotification();
        log.info("Notification for expiring reservation task has been executed !");
    }

}
