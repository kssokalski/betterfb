package com.betterfb;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.*;

@Path("/auth")
public class UserController {

    @Inject
    private UserRepository userRepository;

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(User user) {
        try {
            userRepository.save(user);

            // Return a successful response with the user data or a message
            return Response.status(Response.Status.CREATED)
                           .entity(user)
                           .build();
        } catch (Exception e) {
            // Handle any exceptions and return an error response
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Error: " + e.getMessage())
                           .build();
        }
    }
}