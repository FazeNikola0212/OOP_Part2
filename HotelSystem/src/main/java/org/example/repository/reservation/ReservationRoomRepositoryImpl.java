package org.example.repository.reservation;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.reservation.Reservation;
import org.example.model.reservation.ReservationRoom;
import org.example.model.room.Room;
import org.example.repository.baserepository.GenericRepositoryImpl;
import org.example.repository.room.RoomRepositoryImpl;

import java.nio.channels.SelectableChannel;
import java.time.LocalDateTime;
import java.util.List;

public class ReservationRoomRepositoryImpl extends GenericRepositoryImpl<ReservationRoom, Long> implements ReservationRoomRepository {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPU");
    private static final Logger log = LogManager.getLogger(ReservationRoomRepositoryImpl.class);


    public ReservationRoomRepositoryImpl() {
        super(ReservationRoom.class);
    }

    @Override
    public List<ReservationRoom> findOverlappingReservations(Room room, LocalDateTime startDate, LocalDateTime endDate) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery("SELECT rr FROM ReservationRoom rr " +
                    "WHERE rr.room = :room " +
                    "AND rr.endDate >= :startDate " +
                    "AND rr.startDate <= :endDate", ReservationRoom.class)
                    .setParameter("room", room)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .getResultList();

        } finally {
            em.close();
        }
    }

    @Override
    public List<ReservationRoom> findRoomsByReservation(Reservation reservation) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery("SELECT rr FROM ReservationRoom rr " +
                    "WHERE rr.reservation = :reservation", ReservationRoom.class)
                    .setParameter("reservation", reservation)
                    .getResultList();

        } finally {
            em.close();
        }
    }
}
