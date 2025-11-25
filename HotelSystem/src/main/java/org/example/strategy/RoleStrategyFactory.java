package org.example.strategy;

import org.example.model.user.Role;

/* With the help by Strategy pattern we organize well
the permission and privileges between different user roles.
Also manages clean adding and removing features for various role */

public class RoleStrategyFactory {
    public static RoleStrategy getStrategy(Role role) {
        return switch (role) {
            case MANAGER -> new ManagerStrategy();
            case RECEPTIONIST -> new ReceptionistStrategy();
            case OWNER -> new OwnerStrategy();
            default -> new AdminStrategy();
        };
    }
}
