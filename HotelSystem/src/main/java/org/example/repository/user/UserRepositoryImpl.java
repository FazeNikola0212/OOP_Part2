package org.example.repository.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.user.Role;
import org.example.model.user.User;
import org.example.repository.baserepository.GenericRepositoryImpl;

import java.util.List;

public class UserRepositoryImpl extends GenericRepositoryImpl<User, Long> implements UserRepository {
    private static final EntityManagerFactory emf = Persistence
            .createEntityManagerFactory("myPU");
    private static final Logger log = LogManager.getLogger(UserRepositoryImpl.class);

    public UserRepositoryImpl() {
        super(User.class);
    }

    public User findByUsername(String username) {
        EntityManager em = emf.createEntityManager();
        List<User> result = em
                .createQuery
                        ("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getResultList();
        em.close();
        return result.isEmpty() ? null : result.get(0);
    }



    @Override
    public List<User> findAllManagers() {
        EntityManager em = emf.createEntityManager();
        List<User> managers = em.createQuery("SELECT u FROM User u WHERE u.role = :role", User.class)
                .setParameter("role", Role.MANAGER)
                .getResultList();
        em.close();
        return managers;
    }


    @Override
    public List<User> findReceptionistByHotelId(Long hotelId) {
        EntityManager em = emf.createEntityManager();
        try {
             return em.createQuery("SELECT u FROM User u WHERE u.assignedHotel.id = :hotelId AND u.role = :role", User.class)
                     .setParameter("role",  Role.RECEPTIONIST)
                    .setParameter("hotelId", hotelId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<User> findAllNotAssignedReceptionists() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.assignedHotel IS null AND u.role = :role", User.class)
                    .setParameter("role", Role.RECEPTIONIST)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
