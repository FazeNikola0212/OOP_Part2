package org.example.service.hotel;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.DTO.CreateHotelRequest;
import org.example.exceptions.ExistingHotelException;
import org.example.exceptions.InvalidRoleException;
import org.example.model.hotel.Hotel;
import org.example.model.user.Role;
import org.example.model.user.User;
import org.example.repository.hotel.HotelRepository;
import org.example.repository.user.UserRepository;

import java.util.List;


public class HotelService {
    private static final Logger log = LogManager.getLogger(HotelService.class);
    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;

    public HotelService(HotelRepository hotelRepository, UserRepository userRepository) {
        this.hotelRepository = hotelRepository;
        this.userRepository = userRepository;
    }

    public List<Hotel> getAllHotels(User owner) {
        return hotelRepository.findAllByOwner(owner);
    }

    public Hotel getHotelByName(String hotelName) {
        if (hotelRepository.findByHotelName(hotelName) == null) {
            log.error("Hotel with this name does not exist");
            throw new ExistingHotelException("Hotel with name " + hotelName + " does not exist");
        }
        return hotelRepository.findByHotelName(hotelName);
    }

    @Transactional
    public Hotel createHotel(CreateHotelRequest request) {
        if (hotelRepository.existsByName(request.getName())) {
            log.error("Hotel with name {} already exists", request.getName());
            throw new ExistingHotelException("Hotel with name " + request.getName() + " already exists");
        }
        if (hotelRepository.existsByAddress(request.getAddress())) {
            log.error("Hotel with address " + request.getAddress() + " already exists");
            throw new ExistingHotelException("Hotel with address " + request.getAddress() + " already exists");
        }

        Hotel hotel = Hotel.builder()
                .name(request.getName())
                .address(request.getAddress())
                .city(request.getCity())
                .manager(request.getManager())
                .owner(request.getOwner())
                .build();

        User manager = request.getManager();
        manager.setActive(true);
        hotelRepository.save(hotel);

        manager.setAssignedHotel(hotel);
        userRepository.update(manager);



        log.info("Hotel with name {} has been created", hotel.getName());
        return hotel;
    }

    @Transactional
    public void addReceptionist(Long hotelId, User receptionist) {
        validatingHotelReceptionist(hotelId, receptionist);
        receptionist.setActive(true);
        userRepository.update(receptionist);
        hotelRepository.addReceptionist(hotelId, receptionist);
        log.info("Successfully added receptionist with name: " + receptionist.getFullName());
    }

    @Transactional
    public void removeReceptionist(Long hotelId, User receptionist) {
        validatingHotelReceptionist(hotelId, receptionist);
        receptionist.setActive(false);
        userRepository.update(receptionist);
        hotelRepository.removeReceptionist(hotelId, receptionist);
        log.info("Successfully removed receptionist with name: " + receptionist.getFullName());
    }


    private void validatingHotelReceptionist(Long hotelId, User receptionist) {
        Hotel hotel = hotelRepository.findById(hotelId);
        if (hotel == null) {
            log.error("Hotel with id {} does not exist", hotelId);
            throw new ExistingHotelException("Hotel with id " + hotelId + " does not exist");
        }

        if (!receptionist.getRole().equals(Role.RECEPTIONIST)) {
            log.error("The user {} does not have role Receptionist", receptionist.getUsername());
            throw new InvalidRoleException("The user " + receptionist.getUsername() + " does not have role Receptionist");
        }
    }

}
