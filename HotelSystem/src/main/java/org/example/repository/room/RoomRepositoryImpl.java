package org.example.repository.room;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.DTO.RoomDetailsDTO;
import org.example.model.hotel.Hotel;
import org.example.model.room.Room;
import org.example.model.room.RoomStatus;
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

    @Override
    public List<RoomDetailsDTO> findAllRoomsDetailsByHotel(Hotel hotel) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery("SELECT new org.example.DTO.RoomDetailsDTO(" +
                            "r.number, r.roomCategory, r.pricePerNight, r.roomStatus, r.rating, " +
                            "rr.startDate, rr.endDate" +
                            ") " +
                            "FROM Room r " +
                            "LEFT JOIN ReservationRoom rr " +
                            "  ON rr.room = r " +
                            " AND CURRENT_TIMESTAMP BETWEEN rr.startDate AND rr.endDate " +
                            "WHERE r.hotel = :hotel " +
                            "ORDER BY r.number ASC", RoomDetailsDTO.class)
                    .setParameter("hotel", hotel)
            .getResultList();

        } finally {
            em.close();
        }
    }

    @Override
    public void updateRoomStatus(String roomNumber, RoomStatus status, Hotel hotel) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            em.createQuery(
                            "UPDATE Room r SET r.roomStatus = :status WHERE r.number = :number AND r.hotel = :hotel"
                    )
                    .setParameter("status", status)
                    .setParameter("hotel", hotel)
                    .setParameter("number", roomNumber)
                    .executeUpdate();

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public void updateRoomStatusMoreThan1Room(List<String> roomNumbers, RoomStatus status) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            em.createQuery(
                            "UPDATE Room r SET r.roomStatus = :status WHERE r.number IN :number"
                    )
                    .setParameter("status", status)
                    .setParameter("number", roomNumbers)
                    .executeUpdate();

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }


}
