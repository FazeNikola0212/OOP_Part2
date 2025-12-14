package org.example.util;

import org.example.scheduler.SchedulerService;
import org.example.service.reservation.ReservationService;
import org.example.service.reservation.ReservationNoShownTerminateTask;

import java.util.concurrent.TimeUnit;

public class ApplicationBootstrap {

    private final SchedulerService schedulerService = new SchedulerService();

    public void start(ReservationService reservationService) {
        ReservationNoShownTerminateTask task = new ReservationNoShownTerminateTask(reservationService);

        schedulerService.start(task, 0, 2, TimeUnit.HOURS);

    }

    public void stop() {
        schedulerService.shutdown();
    }




}

