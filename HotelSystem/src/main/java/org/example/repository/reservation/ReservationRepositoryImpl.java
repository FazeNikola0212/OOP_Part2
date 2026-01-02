package org.example.repository.reservation;

import jakarta.persistence.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.client.Client;
import org.example.model.hotel.Hotel;
import org.example.model.reservation.Reservation;
import org.example.model.reservation.ReservationStatus;
import org.example.model.reservation.TerminationType;
import org.example.repository.baserepository.GenericRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationRepositoryImpl extends GenericRepositoryImpl<Reservation, Long> implements ReservationRepository {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPU");
    private static final Logger log = LogManager.getLogger(ReservationRepositoryImpl.class);

    public ReservationRepositoryImpl() {
        super(Reservation.class);
    }

    @Override
    public List<Reservation> findAllByReservationStatus(ReservationStatus reservationStatus) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery("SELECT r FROM Reservation r WHERE r.status = :status", Reservation.class)
                    .setParameter("status", reservationStatus)
                    .getResultList();

        } finally {
            em.close();
        }
    }

    @Override
    public int expireNoShowReservations(LocalDateTime threshold) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            int result = em.createQuery("""
                UPDATE Reservation r 
                SET r.status = :expired,
                    r.terminationType = :terminationType
                WHERE r.status = :active
                  AND r.isCheckedIn = false
                  AND EXISTS (
                      SELECT rr FROM ReservationRoom rr
                      WHERE rr.reservation = r
                      AND rr.startDate < :threshold
                 )
            """)
                    .setParameter("active", ReservationStatus.ACTIVE)
                    .setParameter("expired", ReservationStatus.EXPIRED)
                    .setParameter("terminationType", TerminationType.NO_SHOW)
                    .setParameter("threshold", threshold)
                    .executeUpdate();

            tx.commit();
            log.info("Expired reservations: {}", result);
            return result;

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }


    @Override
    public List<Client> findClientsWithExpiredNoShows(LocalDateTime threshold) {
        EntityManager em = emf.createEntityManager();

        String jpql = """
                    SELECT DISTINCT r.mainClient
                    FROM Reservation r
                    JOIN ReservationRoom rr ON rr.reservation = r
                    WHERE r.status = :expired
                        AND r.terminationType = :terminationType
                        AND rr.startDate < :threshold 
                """;

        return em.createQuery(jpql, Client.class)
                .setParameter("expired", ReservationStatus.EXPIRED)
                .setParameter("terminationType", TerminationType.NO_SHOW)
                .setParameter("threshold", threshold)
                .getResultList();
    }

    @Override
    public List<Reservation> findAllByHotel(Hotel hotel) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery("SELECT DISTINCT r " +
                    "FROM Reservation r " +
                    "LEFT JOIN FETCH r.mainClient " +
                    "LEFT JOIN FETCH r.receptionist " +
                    "LEFT JOIN FETCH r.guests " +
                    "LEFT JOIN FETCH r.rooms rr " +
                    "LEFT JOIN FETCH rr.room " +
                    "WHERE r.hotel = :hotel " +
                            "ORDER BY r.createdAt DESC", Reservation.class)
                    .setParameter("hotel", hotel)
                    .getResultList();

        } finally {
            em.close();
        }
    }

    @Override
    public List<Client> findAllClientsByReservationId(Long reservationId) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery("SELECT c FROM " +
                    "Reservation r " +
                    "JOIN r.guests c " +
                    "WHERE r.id = :reservationId", Client.class
            ).setParameter("reservationId", reservationId).getResultList();

        } finally {
            em.close();
        }
    }

    @Override
    public Reservation findByIdWithClient(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("""
            SELECT r FROM Reservation r
            JOIN FETCH r.mainClient
            WHERE r.id = :id
        """, Reservation.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

}
