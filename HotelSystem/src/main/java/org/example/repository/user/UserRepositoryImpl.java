package org.example.repository.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
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
        List<User> result = em
                .createQuery
                        ("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getResultList();
        em.close();
        return result.isEmpty() ? null : result.get(0);
    }

}
