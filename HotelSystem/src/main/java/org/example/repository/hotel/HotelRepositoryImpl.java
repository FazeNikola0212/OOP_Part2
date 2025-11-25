package org.example.repository.hotel;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.model.hotel.Hotel;
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
        em.getTransaction().begin();
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
        em.getTransaction().begin();
        try {
            Long count = em.createQuery("SELECT COUNT(h) FROM Hotel h WHERE h.address = :address", Long.class)
                    .setParameter("address", address)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

}
