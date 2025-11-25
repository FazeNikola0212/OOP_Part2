package org.example.strategy;

import org.example.controller.DashboardController;

public class AdminStrategy implements RoleStrategy {

    @Override
    public void applyPermissions(DashboardController controller) {}
}
