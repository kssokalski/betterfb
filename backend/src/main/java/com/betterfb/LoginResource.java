package com.betterfb;


import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
public class LoginResource {

    private UserService userService = new UserService();

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest loginRequest ) {
        User user = userService.authenticate(loginRequest.getIdentifier(), loginRequest.getPassword());
        if (user != null) {
            // Generowanie tokenu JWT
            String token = JwtUtility.generateToken(user.getUsername());
            return Response.ok(new LoginResponse(token)).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid username or password")
                    .build();
        }
    }


}