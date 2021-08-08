package org.acme.lifecycle;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import Database.DatabaseHandler;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;

@ApplicationScoped
public class AppLifecycleBean {

    private static final Logger LOGGER = Logger.getLogger("ListenerBean");

    void onStart(@Observes StartupEvent ev) {
        String result = DatabaseHandler.establishConnection();
        LOGGER.info(result);
    }

    /*void onStop(@Observes ShutdownEvent ev) {
        *//*DatabaseHandler.closeConnection();*//*
        LOGGER.info("Connection with the database closed.");
        System.exit(0);
    }*/

}