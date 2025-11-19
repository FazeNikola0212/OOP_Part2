package org.example.authorization;

import org.example.model.user.Role;
import org.example.model.user.User;
import org.example.session.Session;

public class AuthorizationService {

    public static boolean hasRole(Role... allowedRoles) {
        User user = Session.getSession().getLoggedUser();
        System.out.println(user.getUsername());
        Role userRole = Session.getSession().getLoggedUser().getRole();
        for (Role role : allowedRoles) {
            if (role == userRole) {
                return true;
            }
        }
        return false;
    }
}
