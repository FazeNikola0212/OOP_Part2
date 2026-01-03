package org.example.service.reservation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReservationNoShownTerminateTask implements Runnable {
    private static final Logger log = LogManager.getLogger(ReservationNoShownTerminateTask.class);

    private final ReservationService reservationService;

    public ReservationNoShownTerminateTask(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Override
    public void run() {
        reservationService.terminateNoShownReservation();
        log.info("Update for no shown reservation has been executed");
    }
}
