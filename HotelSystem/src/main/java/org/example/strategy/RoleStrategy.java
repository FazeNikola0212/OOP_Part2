package org.example.strategy;

import org.example.controller.DashboardController;

public interface RoleStrategy {
    void applyPermissions(RoleConfigurable controller);
}
