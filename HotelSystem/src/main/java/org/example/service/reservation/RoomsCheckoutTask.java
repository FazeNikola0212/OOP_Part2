package org.example.service.reservation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RoomsCheckoutTask implements Runnable {
    private static final Logger log = LogManager.getLogger(RoomsCheckoutTask.class);
    private final ReservationService reservationService;

    public RoomsCheckoutTask(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Override
    public void run() {
        try {
            reservationService.releaseRoomsAfterCheckout();
            log.info("Checkout rooms task has been executed successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
