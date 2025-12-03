package org.example.service.amenity;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.DTO.EditAmenityDTO;
import org.example.exceptions.AmenityNameAlreadyExistsException;
import org.example.model.amenity.Amenity;
import org.example.model.amenity.SeasonAmenity;
import org.example.model.hotel.Hotel;
import org.example.repository.amenity.AmenityRepository;
import org.example.repository.hotel.HotelRepository;

import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class AmenityService {
    private static final Logger log = LogManager.getLogger(AmenityService.class);
    private final AmenityRepository amenityRepository;
    private final  HotelRepository hotelRepository;

    public AmenityService(AmenityRepository amenityRepository, HotelRepository hotelRepository) {
        this.amenityRepository = amenityRepository;
        this.hotelRepository = hotelRepository;
    }

    public List<Amenity> getAllAmenities() {
        return amenityRepository.findAll();
    }

    public List<Amenity> getAllAmenitiesByHotel(Hotel hotel) {
        return amenityRepository.getAllAmenitiesByHotel(hotel);
    }

    @Transactional
    public Amenity createAmenity(String amenityName, String description, SeasonAmenity season, Hotel hotel) {
        if (amenityName == null || description == null || season == null) {
            throw new IllegalArgumentException("Name, description, and season are mandatory");
        }

        Amenity amenity = amenityRepository.findByName(amenityName)
                .orElseGet(() -> {
                    Amenity newAmenity = Amenity.builder()
                            .name(amenityName)
                            .description(description)
                            .season(season)
                            .usageCount(0)
                            .enabled(false)
                            .hotels(new HashSet<>())
                            .build();
                    amenityRepository.save(newAmenity);
                    return newAmenity;
                });

        Hotel managedHotel = hotelRepository.fetchByHotelId(hotel.getId());

        if (managedHotel.getAmenities() == null) {
            managedHotel.setAmenities(new HashSet<>());
        }

        if (!managedHotel.getAmenities().contains(amenity)) {
            managedHotel.getAmenities().add(amenity);
        }

        if (!amenity.getHotels().contains(managedHotel)) {
            amenity.getHotels().add(managedHotel);
        }

        hotelRepository.update(managedHotel);
        amenityRepository.update(amenity);

        log.info("Amenity '{}' added to Hotel '{}'", amenity.getName(), managedHotel.getName());
        return amenity;
    }

    public EditAmenityDTO updateAmenity(EditAmenityDTO dto) {
        Amenity amenity =  amenityRepository.findById(dto.getId());
        if (amenity == null) {
            throw new IllegalArgumentException("Amenity with ID " + dto.getId() + " not found");
        }

        amenity.setName(dto.getName());
        amenity.setDescription(dto.getDescription());
        amenity.setSeason(dto.getSeasonAmenity());
        amenity.setEnabled(dto.isEnabled());

        amenityRepository.update(amenity); // or save depending on your repo method

        log.info("Amenity '{}' updated successfully", amenity.getName());

        return mapToDTO(amenity);
    }

    public EditAmenityDTO mapToDTO(Amenity amenity) {
        return new EditAmenityDTO(
                amenity.getId(),
                amenity.getName(),
                amenity.getDescription(),
                amenity.getSeason(),
                amenity.isEnabled()
        );
    }
}
