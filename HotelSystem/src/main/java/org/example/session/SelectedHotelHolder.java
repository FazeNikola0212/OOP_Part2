package org.example.session;

import lombok.*;
import org.example.model.hotel.Hotel;

public class SelectedHotelHolder {
    @Getter
    private static Hotel hotel;
    private static Long hotelId;

    public static void setHotel(Hotel hotel) {
        SelectedHotelHolder.hotel = hotel;
    }
    public static void setHotelId(Long hotelId) {
        SelectedHotelHolder.hotelId = hotelId;
    }

    public static void clear() {
        SelectedHotelHolder.hotel = null;
    }
}
