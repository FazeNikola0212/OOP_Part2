package org.example.repository.user;

import org.example.model.user.User;
import org.example.repository.baserepository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User,Long> {
    User findByUsername(String username);
    User findByUsernameAndPassword(String username, String password);
    List<User> findAllManagers();
}
