package org.example.repository.init;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.example.model.user.Role;
import org.example.model.user.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class Initializer {
    private final EntityManagerFactory emf;

    public Initializer(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public static void initialize() {
        String url = "jdbc:mysql://localhost:3306/?allowMultiQueries=true";
        String user = "root";
        String pass = "rootpass";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE DATABASE IF NOT EXISTS hotel
                CHARACTER SET utf8mb4
                COLLATE utf8mb4_unicode_ci
            """);

        } catch (SQLException e) {
            throw new RuntimeException("DB init failed", e);
        }
    }


    public void initAdminUser() {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Long count = em.createQuery(
                            "SELECT COUNT(u) FROM User u WHERE u.username = :username",
                            Long.class
                    )
                    .setParameter("username", "admin")
                    .getSingleResult();

            if (count == 0) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(BCrypt.hashpw("admin", BCrypt.gensalt()));
                admin.setRole(Role.ADMIN);
                admin.setActive(true);
                admin.setCreatedAt(LocalDateTime.now());

                em.persist(admin);
            }

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
