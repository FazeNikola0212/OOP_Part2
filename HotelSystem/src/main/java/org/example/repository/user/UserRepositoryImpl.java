package org.example.repository.user;

import org.example.model.user.User;
import org.example.repository.baserepository.GenericRepositoryImpl;

public class UserRepositoryImpl extends GenericRepositoryImpl<User, Long> {
    public UserRepositoryImpl() {
        super(User.class);
    }
}
