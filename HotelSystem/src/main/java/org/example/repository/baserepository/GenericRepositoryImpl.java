package org.example.repository.baserepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

/* Base Repository implementation for the
base operations like save, delete, read.
Using EntityManagerFactory with Persistence configuration.
Also benefits from the Generics to make suitable for any entity */

public abstract class GenericRepositoryImpl<T, ID> implements CrudRepository<T, ID> {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("myPU");

    private Class<T> entityClass;
    protected GenericRepositoryImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public void save(T entity) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(entity);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public T findById(ID id) {
        EntityManager em = emf.createEntityManager();
        T result =  em.find(entityClass, id);
        em.close();
        return result;
    }

    @Override
    public List<T> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            List<T> result = em.createQuery("FROM " + entityClass.getSimpleName(), entityClass)
                    .getResultList();
            return result;
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteById(ID id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T entity = em.find(entityClass, id);
            if (entity != null) em.remove(entity);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }



}
