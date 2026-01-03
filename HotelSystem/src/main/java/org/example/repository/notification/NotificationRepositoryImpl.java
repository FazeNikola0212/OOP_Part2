package org.example.repository.notification;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.notification.Notification;
import org.example.repository.baserepository.GenericRepositoryImpl;

import java.util.List;

public class NotificationRepositoryImpl extends GenericRepositoryImpl<Notification, Long> implements NotificationRepository {
    private static final Logger log = LogManager.getLogger(NotificationRepositoryImpl.class);
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPU");

    public NotificationRepositoryImpl() {
        super(Notification.class);
    }

    @Override
    public List findByUserId(Long userId) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createNativeQuery("SELECT * FROM hotel.notifications n " +
                    "WHERE n.createdAt = (" +
                    "SELECT MAX(n2.createdAt)" +
                    "FROM hotel.notifications n2 " +
                    "WHERE n2.message = n.message " +
                    "AND n2.user_id = :userId)", Notification.class)
                    .setParameter("userId", userId).getResultList();

        } finally {
            em.close();
        }

    }
}
