package org.example.service.client;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.DTO.CreateClientRequest;
import org.example.exceptions.EmailIsExistingException;
import org.example.exceptions.PhoneNumberIsExistingException;
import org.example.model.client.Client;
import org.example.repository.client.ClientRepository;
import org.example.repository.hotel.HotelRepository;
import org.example.session.Session;

import java.time.LocalDateTime;

public class ClientService {

    private static final Logger log = LogManager.getLogger(ClientService.class);
    private ClientRepository clientRepository;
    private HotelRepository hotelRepository;

    public ClientService(ClientRepository clientRepository, HotelRepository hotelRepository) {
        this.clientRepository = clientRepository;
        this.hotelRepository = hotelRepository;
    }

    @Transactional
    public Client createClient(CreateClientRequest request) {
        if (request.getPhoneNumber() == null || request.getPhoneNumber().isEmpty()) {
            log.error("Phone number is null or empty");
            throw new IllegalArgumentException("Phone number is required");
        }
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            log.error("Email is null or empty");
            throw new IllegalArgumentException("Email is required");
        }
        if (clientRepository.findByPhoneNumber(request.getPhoneNumber()) != null) {
            log.error("Phone number already exists");
            throw new PhoneNumberIsExistingException("Phone number already exists");
        }
        if (clientRepository.findByEmail(request.getEmail()) != null) {
            log.error("Email already exists");
            throw new EmailIsExistingException("Email already exists");
        }

        Client client = Client.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .rating(0)
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .hotel(hotelRepository.findByReceptionist(Session.getSession().getLoggedUser()))
                .build();

        clientRepository.save(client);
        log.info("Client " + request.getFirstName() + " " + request.getLastName() + " has been created");
        return client;
    }



}
