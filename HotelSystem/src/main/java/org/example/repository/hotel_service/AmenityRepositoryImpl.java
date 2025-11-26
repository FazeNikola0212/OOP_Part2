package org.example.repository.hotel_service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.model.amenity.Amenity;
import org.example.repository.baserepository.GenericRepositoryImpl;

import java.util.Optional;

public class AmenityRepositoryImpl extends GenericRepositoryImpl<Amenity, Long> implements AmenityRepository {
    private static final EntityManagerFactory emf = Persistence
            .createEntityManagerFactory("myPU");

    public AmenityRepositoryImpl() {
        super(Amenity.class);
    }

    @Override
    public Optional<Amenity> findByName(String name) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            return em.createQuery("SELECT a FROM Amenity a WHERE a.name = :name", Amenity.class)
                    .setParameter("name", name)
                    .getResultList().stream()
                    .findFirst();

        } finally {
            em.getTransaction().commit();
            em.close();
        }
    }
}
