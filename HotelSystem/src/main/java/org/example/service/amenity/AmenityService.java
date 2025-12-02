package org.example.service.amenity;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exceptions.AmenityNameAlreadyExistsException;
import org.example.model.amenity.Amenity;
import org.example.model.amenity.SeasonAmenity;
import org.example.model.hotel.Hotel;
import org.example.repository.amenity.AmenityRepository;
import org.example.repository.hotel.HotelRepository;

import java.util.List;

public class AmenityService {
    private static final Logger log = LogManager.getLogger(AmenityService.class);
    private final AmenityRepository amenityRepository;

    public AmenityService(AmenityRepository amenityRepository) {
        this.amenityRepository = amenityRepository;
    }

    public List<Amenity> getAllAmenities() {
        return amenityRepository.findAll();
    }

    @Transactional
    public Amenity createAmenity(String amenityName, String description, SeasonAmenity seasonAmenity) {

        if (amenityName == null || description == null || seasonAmenity == null) {
            log.error("Amenity name or description cannot be null!");
            throw new IllegalArgumentException("Name, description and season are mandatory");
        }
        if (amenityRepository.findByName(amenityName).isPresent()) {
            log.error("Amenity with this name already exists!");
            throw new AmenityNameAlreadyExistsException("Amenity with name " + amenityName + " already exists");
        }

        Amenity amenity = Amenity.builder()
                .name(amenityName)
                .description(description)
                .season(seasonAmenity)
                .usageCount(0)
                .enabled(false)
                .build();

        amenityRepository.save(amenity);

        log.info("Amenity with name " + amenityName + " has been created");

        return amenity;
    }

}
