package org.example.repository.reservation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.reservation.ReservationAmenity;
import org.example.repository.baserepository.CrudRepository;
import org.example.repository.baserepository.GenericRepositoryImpl;

import java.util.List;

public class ReservationAmenityRepositoryImpl extends GenericRepositoryImpl<ReservationAmenity, Long> implements ReservationAmenityRepository {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPU");
    private static final Logger log = LogManager.getLogger(ReservationAmenityRepositoryImpl.class);


    public ReservationAmenityRepositoryImpl() {
        super(ReservationAmenity.class);
    }

    public List<String> findAmenityNamesByReservationId(Long reservationId) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                            "SELECT DISTINCT ra.amenity.name FROM ReservationAmenity ra " +
                                    "WHERE ra.reservation.id = :reservationId",
                            String.class
                    )
                    .setParameter("reservationId", reservationId)
                    .getResultList();

        } finally {
            em.close();
        }
    }

}
