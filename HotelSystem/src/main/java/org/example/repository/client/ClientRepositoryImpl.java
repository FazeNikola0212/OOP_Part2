package org.example.repository.client;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.model.client.Client;
import org.example.repository.baserepository.GenericRepositoryImpl;

import java.util.List;
import java.util.Optional;

public class ClientRepositoryImpl extends GenericRepositoryImpl<Client, Long> implements ClientRepository {
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("myPU");

    public ClientRepositoryImpl() {
        super(Client.class);
    }

    @Override
    public Client findByPhoneNumber(String phoneNumber) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            List<Client> client =
                    em.createQuery("SELECT c FROM Client c WHERE c.phoneNumber = :phoneNumber", Client.class)
                            .setParameter("phoneNumber", phoneNumber)
                            .getResultList();
            em.getTransaction().commit();
            return client.isEmpty() ? null : client.get(0);
        } finally {
            em.close();
        }
    }
    @Override
    public Client findByEmail(String email) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            List<Client> client =
                    em.createQuery("SELECT c FROM Client c WHERE c.email = :email", Client.class)
                            .setParameter("email", email)
                            .getResultList();
            em.getTransaction().commit();
            return client.isEmpty() ? null : client.get(0);
        } finally {
            em.close();
        }
    }


}
