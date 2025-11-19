package org.example.repository.baserepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

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
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
        em.close();
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
        List<T> result =  em.createNamedQuery("FROM " + entityClass.getSimpleName(), entityClass)
                .getResultList();
        return result;
    }

    @Override
    public void deleteById(ID id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        T entity = em.find(entityClass, id);
        if (entity != null) em.remove(entity);
        em.getTransaction().commit();
        em.close();
    }



}
