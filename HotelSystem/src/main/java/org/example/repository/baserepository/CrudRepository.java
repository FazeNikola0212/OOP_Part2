package org.example.repository.baserepository;

import java.util.List;

public interface CrudRepository<T, ID> {
    void save(T entity);
    T findById(ID id);
    List<T> findAll();
    void deleteById(ID id);
    void update(T entity);
}
