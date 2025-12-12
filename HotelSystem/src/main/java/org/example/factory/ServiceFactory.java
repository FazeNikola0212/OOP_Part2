package org.example.factory;

import org.example.service.reservation.ReservationService;
import org.example.service.room.RoomService;
import org.example.service.amenity.AmenityService;
import org.example.service.client.ClientService;
import org.example.service.hotel.HotelService;
import org.example.service.user.UserService;

public class ServiceFactory {
    private static UserService userService;
    private static HotelService hotelService;
    private static AmenityService amenityService;
    private static ClientService clientService;
    private static RoomService roomService;
    private static ReservationService reservationService;

    public static UserService getUserService() {
        if (userService == null) {
            userService = new UserService(
                    RepositoryFactory.getUserRepository(),
                    RepositoryFactory.getHotelRepository()
            );
        }
        return userService;
    }

    public static HotelService getHotelService() {
        if (hotelService == null) {
            hotelService = new HotelService(
                    RepositoryFactory.getHotelRepository(),
                    RepositoryFactory.getUserRepository()
            );
        }
        return hotelService;
    }

    public static AmenityService getAmenityService() {
        if (amenityService == null) {
            amenityService = new AmenityService(
                    RepositoryFactory.getAmenityRepository(),
                    RepositoryFactory.getHotelRepository()
            );
        }
        return amenityService;
    }

    public static ClientService getClientService() {
        if (clientService == null) {
            clientService = new ClientService(
                    RepositoryFactory.getClientRepository()
            );
        }
        return clientService;
    }

    public static RoomService getRoomService() {
        if (roomService == null) {
            roomService = new RoomService(
                    RepositoryFactory.getRoomRepository());
        }
        return roomService;
    }

    public static ReservationService getReservationService() {
        if (reservationService == null) {
            reservationService = new ReservationService(
                    RepositoryFactory.getReservationRepository(),
                    RepositoryFactory.getReservationAmenityRepository(),
                    RepositoryFactory.getReservationRoomRepository(),
                    RepositoryFactory.getAmenityRepository());
        }
        return reservationService;
    }
}
