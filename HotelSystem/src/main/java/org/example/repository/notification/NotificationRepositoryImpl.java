package org.example.repository.notification;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.notification.Notification;
import org.example.repository.baserepository.GenericRepositoryImpl;

public class NotificationRepositoryImpl extends GenericRepositoryImpl<Notification, Long> implements NotificationRepository {
    private static final Logger log = LogManager.getLogger(NotificationRepositoryImpl.class);
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPU");

    public NotificationRepositoryImpl() {
        super(Notification.class);
    }
}
