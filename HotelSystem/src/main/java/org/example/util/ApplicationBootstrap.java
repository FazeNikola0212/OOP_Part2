package org.example.util;

import org.example.model.notification.Notification;
import org.example.scheduler.SchedulerService;
import org.example.service.notification.ExpiringReservationNotify;
import org.example.service.notification.NotificationService;
import org.example.service.reservation.ReservationService;
import org.example.service.reservation.ReservationNoShownTerminateTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class ApplicationBootstrap {

    private final SchedulerService schedulerService = new SchedulerService();

    public void start(ReservationService reservationService, NotificationService notificationService) {
        ReservationNoShownTerminateTask noShownReservationTask = new ReservationNoShownTerminateTask(reservationService);
        ExpiringReservationNotify expiringReservationTask = new ExpiringReservationNotify(notificationService);

        Long initialDelay = calcDelayToMidnight();

        schedulerService.start(noShownReservationTask, 0, 2, TimeUnit.HOURS);
        schedulerService.start(expiringReservationTask, initialDelay, TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS);
    }

    private Long calcDelayToMidnight() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime nextRun = now.toLocalDate().atTime(0, 1);

        if (!now.isBefore(nextRun)) {
            nextRun = nextRun.plusDays(1);
        }

        return Duration.between(now, nextRun).toMillis();
    }

    public void stop() {
        schedulerService.shutdown();
    }




}

