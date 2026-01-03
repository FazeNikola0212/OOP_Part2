package org.example.service;

import org.example.DTO.CreateClientRequest;
import org.example.exceptions.EmailIsExistingException;
import org.example.exceptions.PhoneNumberIsExistingException;
import org.example.model.client.Client;
import org.example.model.hotel.Hotel;
import org.example.model.user.User;
import org.example.repository.client.ClientRepository;
import org.example.service.client.ClientService;
import org.example.session.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    private CreateClientRequest request;

    @BeforeEach
    void setUp() {
        request = new CreateClientRequest();
        request.setFirstName("Ivan");
        request.setLastName("Ivanov");
        request.setPhoneNumber("0888123456");
        request.setEmail("ivan@test.com");
    }

    @Test
     void getAllClientsByHotel_shouldReturnClients() {
        Hotel hotel = new Hotel();
        when(clientRepository.findAllClientsByHotel(hotel))
                .thenReturn(List.of(new Client()));

        List<Client> result =
                clientService.getAllClientsByHotel(hotel);

        assertEquals(1, result.size());
    }
    @Test
    void createClient_shouldCreateClientSuccessfully() {
        Hotel hotel = new Hotel();
        User user = new User();
        user.setAssignedHotel(hotel);

        try (MockedStatic<Session> sessionMock =
                     Mockito.mockStatic(Session.class)) {

            Session session = mock(Session.class);
            when(session.getLoggedUser()).thenReturn(user);
            sessionMock.when(Session::getSession).thenReturn(session);

            when(clientRepository.findByPhoneNumber(any()))
                    .thenReturn(null);
            when(clientRepository.findByEmail(any()))
                    .thenReturn(null);

            Client client = clientService.createClient(request);

            assertEquals("Ivan", client.getFirstName());
            assertEquals(hotel, client.getHotel());
            verify(clientRepository).save(any(Client.class));
        }
    }

    @Test
    void createClient_shouldThrowPhoneNumberIsExistingException() {
        when(clientRepository.findByPhoneNumber(any()))
                .thenReturn(new Client());

        assertThrows(PhoneNumberIsExistingException.class,
                () -> clientService.createClient(request));
    }

    @Test
    void createClient_shouldThrowEmailIsExistingException() {
        when(clientRepository.findByPhoneNumber(any()))
                .thenReturn(null);
        when(clientRepository.findByEmail(any()))
                .thenReturn(new Client());

        assertThrows(EmailIsExistingException.class,
                () -> clientService.createClient(request));
    }

    @Test
    void createClient_shouldThrowException_whenPhoneIsEmpty() {
        request.setPhoneNumber("");

        assertThrows(IllegalArgumentException.class,
                () -> clientService.createClient(request));
    }


}
