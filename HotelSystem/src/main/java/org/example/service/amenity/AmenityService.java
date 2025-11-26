package org.example.service.amenity;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exceptions.AmenityNameAlreadyExistsException;
import org.example.model.amenity.Amenity;
import org.example.model.amenity.SeasonAmenity;
import org.example.repository.hotel_service.AmenityRepository;
import org.example.service.client.ClientService;

public class AmenityService {
    private static final Logger log = LogManager.getLogger(AmenityService.class);
    private AmenityRepository amenityRepository;

    public AmenityService(AmenityRepository amenityRepository) {
        this.amenityRepository = amenityRepository;
    }

    @Transactional
    public Amenity createAmenity(String amenityName, String description, SeasonAmenity seasonAmenity) {
        if (amenityName == null || description == null || seasonAmenity == null) {
            throw new IllegalArgumentException("Name, description and season are mandatory");
        }
        if (amenityRepository.findByName(amenityName).isPresent()) {
            throw new AmenityNameAlreadyExistsException("Amenity with name " + amenityName + " already exists");
        }

        Amenity amenity = Amenity.builder()
                .name(amenityName)
                .description(description)
                .season(seasonAmenity)
                .usageCount(0)
                .build();
        amenityRepository.save(amenity);
        return amenity;
    }
}
