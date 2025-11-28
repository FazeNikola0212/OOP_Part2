package org.example.repository.hotel;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.model.hotel.Hotel;
import org.example.model.user.User;
import org.example.repository.baserepository.GenericRepositoryImpl;

public class HotelRepositoryImpl extends GenericRepositoryImpl<Hotel, Long> implements HotelRepository {
    private final static EntityManagerFactory emf = Persistence
            .createEntityManagerFactory("myPU");

    public HotelRepositoryImpl() {
        super(Hotel.class);
    }

    @Override
    //Returns True if the Hotel is existing by name
    public boolean existsByName(String name) {
        EntityManager em = emf.createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(h) FROM Hotel h WHERE h.name = :name", Long.class)
                    .setParameter("name", name).getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    @Override
    //Returns True if the Hotel is existing by address
    public boolean existsByAddress(String address) {
        EntityManager em = emf.createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(h) FROM Hotel h WHERE h.address = :address", Long.class)
                    .setParameter("address", address)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    @Override
    public Hotel findByReceptionist(User receptionist) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.find(Hotel.class, receptionist.getAssignedHotel());
        } finally {
            em.close();
        }
    }

    @Override
    public Hotel findByManager(User manager) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Hotel.class, manager.getAssignedHotel());
        } finally {
            em.close();
        }
    }

    @Override
    public void addReceptionist(Long hotelId, User receptionist) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Hotel hotel = em.find(Hotel.class, hotelId);
            receptionist = em.find(User.class, receptionist.getId());

            receptionist.setAssignedHotel(hotel);

            /*em.createNativeQuery("INSERT INTO receptionists (hotel_id, receptionists_id) " +
                    "VALUES (?, ?)")
                    .setParameter(1, hotelId)
                    .setParameter(2, receptionist.getId())
                    .executeUpdate();*/
            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }

    @Override
    public void removeReceptionist(Long hotelId, User receptionist) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            em.find(Hotel.class, hotelId);
            receptionist = em.find(User.class, receptionist.getId());
            receptionist.setAssignedHotel(null);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
