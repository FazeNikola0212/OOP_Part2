package org.example.factory;

import org.example.model.reservation.ReservationAmenity;
import org.example.model.reservation.ReservationRoom;
import org.example.repository.amenity.AmenityRepository;
import org.example.repository.amenity.AmenityRepositoryImpl;
import org.example.repository.client.ClientRepository;
import org.example.repository.client.ClientRepositoryImpl;
import org.example.repository.hotel.HotelRepository;
import org.example.repository.hotel.HotelRepositoryImpl;
import org.example.repository.reservation.*;
import org.example.repository.room.RoomRepository;
import org.example.repository.room.RoomRepositoryImpl;
import org.example.repository.user.UserRepository;
import org.example.repository.user.UserRepositoryImpl;

public class RepositoryFactory {
    private static UserRepository userRepository;
    private static HotelRepository hotelRepository;
    private static ClientRepository clientRepository;
    private static AmenityRepository amenityRepository;
    private static RoomRepository roomRepository;
    private static ReservationRepository reservationRepository;
    private static ReservationRoomRepository reservationRoomRepository;
    private static ReservationAmenityRepository reservationAmenityRepository;

    public static UserRepository getUserRepository() {
        if (userRepository == null) {
            userRepository = new UserRepositoryImpl();
        }
        return userRepository;
    }

    public static HotelRepository getHotelRepository() {
        if (hotelRepository == null) {
            hotelRepository = new HotelRepositoryImpl();
        }
        return  hotelRepository;
    }

    public static ClientRepository getClientRepository() {
        if (clientRepository == null) {
            clientRepository = new ClientRepositoryImpl();
        }
        return clientRepository;
    }

    public static AmenityRepository getAmenityRepository() {
        if (amenityRepository == null) {
            amenityRepository = new AmenityRepositoryImpl();
        }
        return amenityRepository;
    }

    public static RoomRepository getRoomRepository() {
        if (roomRepository == null) {
            roomRepository = new RoomRepositoryImpl();
        }
        return roomRepository;
    }

    public static ReservationRepository getReservationRepository() {
        if (reservationRepository == null) {
            reservationRepository = new ReservationRepositoryImpl();
        }
        return reservationRepository;
    }

    public static ReservationAmenityRepository getReservationAmenityRepository() {
        if (reservationRoomRepository == null) {
            reservationAmenityRepository = new ReservationAmenityRepositoryImpl();
        }
        return reservationAmenityRepository;
    }

    public static ReservationRoomRepository getReservationRoomRepository() {
        if (reservationRoomRepository == null) {
            reservationRoomRepository = new ReservationRoomRepositoryImpl();
        }
        return reservationRoomRepository;
    }



}
