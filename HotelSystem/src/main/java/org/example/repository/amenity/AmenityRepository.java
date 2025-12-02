package org.example.repository.amenity;

import org.example.model.amenity.Amenity;
import org.example.model.hotel.Hotel;
import org.example.repository.baserepository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AmenityRepository extends CrudRepository<Amenity, Long> {
    Optional<Amenity> findByName(String name);
    List<Amenity> getAllAmenitiesByHotel(Hotel hotel);
}
