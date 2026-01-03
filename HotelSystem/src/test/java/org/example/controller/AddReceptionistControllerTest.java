package org.example.controller;

import org.example.model.hotel.Hotel;
import org.example.model.user.User;
import org.example.service.hotel.HotelService;
import org.example.service.user.UserService;
import org.example.session.SelectedHotelHolder;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddReceptionistControllerTest {
    @Mock
    private HotelService hotelService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AddReceptionistController controller;


    @Test
    void initialize_shouldLoadReceptionists() {
        User receptionist1 = User.builder().fullName("Damian").build();
        User receptionist2 = User.builder().fullName("SSS").build();
        User receptionist3 = User.builder().fullName("KKK").build();



        controller.initialize();

    }
}
