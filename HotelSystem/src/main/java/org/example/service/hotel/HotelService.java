package org.example.service.hotel;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.DTO.CreateHotelRequest;
import org.example.exceptions.ExistingHotelException;
import org.example.model.hotel.Hotel;
import org.example.model.user.User;
import org.example.repository.hotel.HotelRepository;
import org.example.repository.hotel.HotelRepositoryImpl;

public class HotelService {
    private static final Logger log = LogManager.getLogger(HotelService.class);
    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
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

        hotelRepository.save(hotel);
        log.info("Hotel with name {} has been created", hotel.getName());
        return hotel;
    }

    public void addReceptionist(Long hotelId, User receptionist) {
        hotelRepository.addReceptionist(hotelId, receptionist);
    }

}
