package org.example.repository.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.model.user.Role;
import org.example.model.user.User;
import org.example.repository.baserepository.GenericRepositoryImpl;

import java.util.List;

public class UserRepositoryImpl extends GenericRepositoryImpl<User, Long> implements UserRepository {
    private static final EntityManagerFactory emf = Persistence
            .createEntityManagerFactory("myPU");

    public UserRepositoryImpl() {
        super(User.class);
    }

    public User findByUsername(String username) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        List<User> result = em
                .createQuery
                        ("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getResultList();
        em.close();
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        User user = em.createQuery("SELECT u FROM User u WHERE " +
                "u.username = :username AND u.password = :password ", User.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .getSingleResult();
        em.close();
        return user;
    }

    @Override
    public List<User> findAllManagers() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        List<User> managers = em.createQuery("SELECT u FROM User u WHERE u.role = :role", User.class)
                .setParameter("role", Role.MANAGER)
                .getResultList();
        em.close();
        return managers;
    }
}
