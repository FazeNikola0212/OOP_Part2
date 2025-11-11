package org.example.repository.user;

import org.example.model.user.User;
import org.example.repository.baserepository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {
    User findByUsername(String username);
}
