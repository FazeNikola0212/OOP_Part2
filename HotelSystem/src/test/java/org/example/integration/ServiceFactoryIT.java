package org.example.integration;

import org.example.factory.ServiceFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(ApplicationExtension.class)
public class ServiceFactoryIT {

    @Test
    void servicesAreCreatedCorrectly() {
        assertNotNull(ServiceFactory.getUserService());
        assertNotNull(ServiceFactory.getReservationService());
        assertNotNull(ServiceFactory.getNotificationService());
    }
}
