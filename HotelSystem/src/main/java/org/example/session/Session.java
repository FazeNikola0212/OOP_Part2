package org.example.session;

import org.example.model.user.User;

import java.util.logging.Logger;

public class Session {

    private static final Logger logger = Logger.getLogger(Session.class.getName());
    private static Session session;
    private User loggedUser;

    private Session() {}

    public static Session getSession() {
        if (session == null) {
            session = new Session();
        }
        return session;
    }
    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
        logger.info("Successfully logged in " + loggedUser.getUsername());
    }
    public User getLoggedUser() {
        return loggedUser;
    }
    public void clearSession() {
        logger.info("Logged out " + loggedUser.getUsername());
        loggedUser = null;
    }

}
