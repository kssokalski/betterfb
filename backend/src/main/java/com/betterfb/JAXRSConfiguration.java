package com.betterfb;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api") 
public class JAXRSConfiguration extends Application {
    // No need to override methods; this enables JAX-RS
}

