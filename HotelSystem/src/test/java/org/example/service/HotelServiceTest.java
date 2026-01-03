package org.example.service;

import org.example.DTO.CreateHotelRequest;
import org.example.exceptions.ExistingHotelException;
import org.example.exceptions.InvalidRoleException;
import org.example.model.hotel.Hotel;
import org.example.model.user.Role;
import org.example.model.user.User;
import org.example.repository.hotel.HotelRepository;
import org.example.repository.user.UserRepository;
import org.example.service.hotel.HotelService;
import org.example.session.SelectedHotelHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HotelServiceTest {
    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private HotelService hotelService;

    private User owner;
    private User manager;
    private User receptionist;
    private Hotel hotel;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setId(1L);
        owner.setRole(Role.OWNER);

        manager = new User();
        manager.setId(2L);
        manager.setRole(Role.MANAGER);

        receptionist = new User();
        receptionist.setId(3L);
        receptionist.setRole(Role.RECEPTIONIST);

        hotel = new Hotel();
        hotel.setId(10L);
        hotel.setOwner(owner);
        hotel.setManager(owner);
    }

    @Test
    void getHotelByName_shouldReturnHotel() {
        when(hotelRepository.findByHotelName("Test"))
                .thenReturn(hotel);

        Hotel result = hotelService.getHotelByName("Test");

        assertEquals(hotel, result);
    }

    @Test
    void getHotelByName_shouldThrowException() {
        when(hotelRepository.findByHotelName("Test"))
                .thenReturn(null);

        assertThrows(ExistingHotelException.class,
                () -> hotelService.getHotelByName("Test"));
    }

    @Test
    void createHotel_shouldCreateHotel() {
        CreateHotelRequest request = new CreateHotelRequest();
        request.setName("Hotel A");
        request.setAddress("Address");
        request.setOwner(owner);
        request.setManager(manager);

        when(hotelRepository.existsByName("Hotel A")).thenReturn(false);
        when(hotelRepository.existsByAddress("Address")).thenReturn(false);

        Hotel result = hotelService.createHotel(request);

        assertEquals("Hotel A", result.getName());
        verify(hotelRepository).save(any(Hotel.class));
        verify(userRepository).update(manager);
    }

    @Test
    void addReceptionist_shouldAddReceptionist() {
        when(hotelRepository.findById(10L))
                .thenReturn(hotel);

        hotelService.addReceptionist(10L, receptionist);

        assertTrue(receptionist.isActive());
        verify(userRepository).update(receptionist);
        verify(hotelRepository).addReceptionist(10L, receptionist);
    }

    @Test
    void addReceptionist_shouldThrowInvalidRole() {
        receptionist.setRole(Role.MANAGER);

        when(hotelRepository.findById(10L))
                .thenReturn(hotel);

        assertThrows(InvalidRoleException.class,
                () -> hotelService.addReceptionist(10L, receptionist));
    }

    @Test
    void removeManager_shouldRemoveManager() {
        when(hotelRepository.findById(10L))
                .thenReturn(hotel);

        try (MockedStatic<SelectedHotelHolder> mocked =
                     Mockito.mockStatic(SelectedHotelHolder.class)) {

            hotelService.removeManager(10L, manager);

            assertFalse(manager.isActive());
            verify(userRepository).update(manager);
            verify(hotelRepository).update(hotel);
        }
    }

    @Test
    void assignManager_shouldAssignNewManager() {
        when(hotelRepository.findById(10L))
                .thenReturn(hotel);
        when(userRepository.findById(2L))
                .thenReturn(manager);

        try (MockedStatic<SelectedHotelHolder> mocked =
                     Mockito.mockStatic(SelectedHotelHolder.class)) {

            hotelService.assignManager(10L, 2L);

            assertEquals(manager, hotel.getManager());
            verify(userRepository).update(manager);
            verify(hotelRepository).update(hotel);
        }
    }


}
