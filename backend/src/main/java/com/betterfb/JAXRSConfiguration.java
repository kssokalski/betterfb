package com.betterfb;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * This class is used to enable JAX-RS in the application.
 */
@ApplicationPath("/api")
public class JAXRSConfiguration extends Application {

    // No need to override methods; this enables JAX-RS
}

