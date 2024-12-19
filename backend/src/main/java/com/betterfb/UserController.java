package com.betterfb;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.*;

/**
 * Handles user authentication, including registration.
 */
@Path("/auth")
public class UserController {

    @Inject
    private UserRepository userRepository;

    /**
     * Registers a new user.
     *
     * @param user the user to register
     * @return a response containing the registered user data or an error message
     */
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(User user) {
        try {
            // Save the user to the database
            userRepository.save(user);

            // Return a successful response with the user data
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

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(UserLoginRequest request) {
        try {
            if (request.getUsername() == null || request.getPassword() == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Nazwa użytkownika i hasło nie mogą być puste").build();
            }
            User user;
            try {
                user = userRepository.findByUsername(request.getUsername());
                if (user == null) {
                    return Response.status(Response.Status.UNAUTHORIZED).entity("Niepoprawne dane logowania").build();
                }
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e.getMessage()).build();
            }
            if (user.getPassword().equals(request.getPassword())) {
                return Response.ok().build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).entity("Niepoprawne dane logowania").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e.getMessage()).build();
        }
    }
}

