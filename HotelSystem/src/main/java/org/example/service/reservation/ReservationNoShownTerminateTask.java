package org.example.service.reservation;

public class ReservationNoShownTerminateTask implements Runnable {

    private final ReservationService reservationService;

    public ReservationNoShownTerminateTask(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Override
    public void run() {
        reservationService.terminateNoShownReservation();
        System.out.println("Successfully called service");
    }
}
