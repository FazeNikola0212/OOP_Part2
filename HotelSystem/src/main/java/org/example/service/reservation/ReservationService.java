package org.example.service.reservation;

import org.example.model.hotel.Hotel;
import org.example.repository.reservation.ReservationRepository;

import java.util.Random;

public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }


    //Creating Random reservation number by first 2 letters and 6-digit number connected by '-'
    public String generateReservationNumber(Hotel hotel) {
        Random random = new Random();
        int number = random.nextInt(999999);

        String[] symbols = hotel.getName().split("");

        return String.format(symbols[0].toUpperCase() + symbols[1].toUpperCase() + "-%06d", number);
    }
}
