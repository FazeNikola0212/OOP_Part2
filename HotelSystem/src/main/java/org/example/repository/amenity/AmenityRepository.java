package org.example.repository.amenity;

import org.example.model.amenity.Amenity;
import org.example.repository.baserepository.CrudRepository;

import java.util.Optional;

public interface AmenityRepository extends CrudRepository<Amenity, Long> {
    Optional<Amenity> findByName(String name);
}
