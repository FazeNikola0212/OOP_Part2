package org.example.repository.hotel;

import org.example.model.hotel.Hotel;
import org.example.model.user.User;
import org.example.repository.baserepository.CrudRepository;

import java.util.List;

public interface HotelRepository extends CrudRepository<Hotel, Long> {
    boolean existsByName(String name);
    boolean existsByAddress(String address);
    Hotel findByReceptionist(User receptionist);
    Hotel findByManager(User manager);
    void addReceptionist(Long hotelId, User receptionist);
    void removeReceptionist(Long hotelId, User receptionist);
    List<Hotel> findAllByOwner(User owner);

}
