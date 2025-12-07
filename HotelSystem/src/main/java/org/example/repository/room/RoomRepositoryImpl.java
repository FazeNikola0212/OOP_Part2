package org.example.repository.room;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.hotel.Hotel;
import org.example.model.room.Room;
import org.example.repository.baserepository.GenericRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;

public class RoomRepositoryImpl extends GenericRepositoryImpl<Room, Long> implements RoomRepository {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPU");
    private static final Logger log = LogManager.getLogger(RoomRepositoryImpl.class);

    public RoomRepositoryImpl() {
        super(Room.class);
    }

    @Override
    public boolean existsRoomByNumber(String number, Hotel hotel) {
        EntityManager em = emf.createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(r) FROM Room r " +
                            "WHERE r.number = :number AND r.hotel.id = :hotel", Long.class)
                    .setParameter("number", number)
                    .setParameter("hotel", hotel.getId())
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Room> findAvailableRooms(LocalDateTime start, LocalDateTime end) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                            "SELECT r FROM Room r " +
                                    "WHERE NOT EXISTS ( " +
                                    "    SELECT rr FROM ReservationRoom rr " +
                                    "    WHERE rr.room = r " +
                                    "      AND rr.startDate < :endDate " +
                                    "      AND rr.endDate > :startDate " +
                                    ")", Room.class)
                    .setParameter("endDate", end)
                    .setParameter("startDate", start)
                    .getResultList();

        } finally {
            em.close();
        }
    }


    @Override
    public List<Room> findAllRoomsByHotel(Hotel hotel) {
        EntityManager em = emf.createEntityManager();

        try {
          return em.createQuery("SELECT r FROM Room r WHERE r.hotel.id = :hotelId", Room.class)
                  .setParameter("hotelId", hotel.getId())
                  .getResultList();

        } finally {
            em.close();
        }
    }
}
