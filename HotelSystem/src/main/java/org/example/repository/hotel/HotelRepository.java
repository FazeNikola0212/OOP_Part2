package org.example.repository.hotel;

import org.example.model.hotel.Hotel;
import org.example.repository.baserepository.CrudRepository;

public interface HotelRepository extends CrudRepository<Hotel, Long> {
    boolean existsByName(String name);
    boolean existsByAddress(String address);
}
