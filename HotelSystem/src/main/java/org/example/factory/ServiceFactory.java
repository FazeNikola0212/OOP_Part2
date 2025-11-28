package org.example.factory;

import org.example.repository.hotel.HotelRepository;
import org.example.service.amenity.AmenityService;
import org.example.service.client.ClientService;
import org.example.service.hotel.HotelService;
import org.example.service.user.UserService;

public class ServiceFactory {
    private static UserService userService;
    private static HotelService hotelService;
    private static AmenityService amenityService;
    private static ClientService clientService;

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
                    RepositoryFactory.getHotelRepository()
            );
        }
        return hotelService;
    }

    public static AmenityService getAmenityService() {
        if (amenityService == null) {
            amenityService = new AmenityService(
                    RepositoryFactory.getAmenityRepository()
            );
        }
        return amenityService;
    }

    public static ClientService getClientService() {
        if (clientService == null) {
            clientService = new ClientService(
                    RepositoryFactory.getClientRepository(),
                    RepositoryFactory.getHotelRepository()
            );
        }
        return clientService;
    }
}
