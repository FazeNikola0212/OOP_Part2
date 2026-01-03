package org.example.service;

import org.example.DTO.EditAmenityDTO;
import org.example.model.amenity.Amenity;
import org.example.model.amenity.SeasonAmenity;
import org.example.model.hotel.Hotel;
import org.example.repository.amenity.AmenityRepository;
import org.example.repository.hotel.HotelRepository;
import org.example.service.amenity.AmenityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AmenityServiceTest {

    @Mock
    private AmenityRepository amenityRepository;

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private AmenityService amenityService;

    private Hotel hotel;

    @BeforeEach
    void setUp() {
        hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Test Hotel");
        hotel.setAmenities(new HashSet<>());
    }

    @Test
    void getAllEnabledAmenitiesByHotel_shouldReturnList() {
        Amenity amenity = Amenity.builder().name("Pool").build();

        when(amenityRepository.findAllAmenitiesByHotelAndEnabled(hotel))
                .thenReturn(List.of(amenity));

        List<Amenity> result =
                amenityService.getAllEnabledAmenitiesByHotel(hotel);

        assertEquals(1, result.size());
        assertEquals("Pool", result.get(0).getName());
    }

    @Test
    void createAmenity_shouldCreateAndAssignAmenity() {
        when(amenityRepository.findByName("Spa"))
                .thenReturn(Optional.empty());

        when(hotelRepository.fetchByHotelId(1L))
                .thenReturn(hotel);

        Amenity amenity = amenityService.createAmenity(
                "Spa",
                "Relax area",
                SeasonAmenity.SUMMER,
                hotel
        );

        assertEquals("Spa", amenity.getName());
        assertFalse(amenity.isEnabled());
        assertTrue(hotel.getAmenities().contains(amenity));

        verify(amenityRepository).save(any(Amenity.class));
        verify(hotelRepository).update(hotel);
        verify(amenityRepository).update(amenity);
    }

    @Test
    void createAmenity_shouldThrowException_whenNameIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                amenityService.createAmenity(
                        null,
                        "desc",
                        SeasonAmenity.SUMMER,
                        hotel
                )
        );
    }

    @Test
    void updateAmenity_shouldUpdateAmenity() {
        Amenity amenity = Amenity.builder()
                .id(1L)
                .name("Old")
                .description("Old desc")
                .season(SeasonAmenity.WINTER)
                .enabled(false)
                .build();

        when(amenityRepository.findById(1L))
                .thenReturn(amenity);

        EditAmenityDTO dto = new EditAmenityDTO(
                1L,
                "New",
                "New desc",
                SeasonAmenity.SUMMER,
                true
        );

        EditAmenityDTO result =
                amenityService.updateAmenity(dto);

        assertEquals("New", result.getName());
        assertTrue(result.isEnabled());

        verify(amenityRepository).update(amenity);
    }

    @Test
    void updateAmenity_shouldThrowException_whenNotFound() {
        when(amenityRepository.findById(1L))
                .thenReturn(null);

        EditAmenityDTO dto = new EditAmenityDTO(
                1L, "Name", "Desc", SeasonAmenity.SUMMER, true
        );

        assertThrows(IllegalArgumentException.class,
                () -> amenityService.updateAmenity(dto));
    }



}
