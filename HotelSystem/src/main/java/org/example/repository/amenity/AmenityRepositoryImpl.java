package org.example.repository.amenity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.model.amenity.Amenity;
import org.example.model.hotel.Hotel;
import org.example.repository.baserepository.GenericRepositoryImpl;

import java.util.List;
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
            return em.createQuery("SELECT a FROM Amenity a WHERE a.name = :name", Amenity.class)
                    .setParameter("name", name)
                    .getResultList().stream()
                    .findFirst();

        } finally {
            em.close();
        }
    }

    @Override
    public List<Amenity> getAllAmenitiesByHotel(Hotel hotel) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT a FROM Hotel h JOIN h.amenities a WHERE h.id = :hotelId", Amenity.class)
                    .setParameter("hotelId", hotel.getId())
                    .getResultList();
        } finally {
            em.close();
        }
    }




}
