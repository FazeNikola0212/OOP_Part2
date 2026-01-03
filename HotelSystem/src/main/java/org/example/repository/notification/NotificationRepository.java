package org.example.repository.notification;

import org.example.model.notification.Notification;
import org.example.repository.baserepository.CrudRepository;

import java.util.List;

public interface NotificationRepository extends CrudRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);
}
