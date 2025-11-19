package org.example.repository.hotel;

import org.example.model.hotel.Hotel;
import org.example.repository.baserepository.GenericRepositoryImpl;

public class HotelRepositoryImpl extends GenericRepositoryImpl<Hotel, Long> {
    public HotelRepositoryImpl() {
        super(Hotel.class);
    }
}
