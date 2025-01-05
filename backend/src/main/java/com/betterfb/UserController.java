package com.betterfb;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.mail.MessagingException;
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

            //dodane przez Lysego. W razie czego do usuniecia

//            if (user.getUsername() == null || user.getUsername().trim() == null) {
//                return Response.status(Response.Status.BAD_REQUEST)
//                        .entity("Nazwa użytkownika jest pusta")
//                        .build();
//            }
//            if (user.getPassword() == null || user.getPassword().trim() == null) {
//                return Response.status(Response.Status.BAD_REQUEST)
//                        .entity("Hasło nie może być puste")
//                        .build();
//            }
//            if (user.getEmail() == null || user.getEmail().trim() == null) {
//                return Response.status(Response.Status.BAD_REQUEST)
//                        .entity("E-mail nie może być pusty")
//                        .build();
//            }

            // do tego miejsca


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

    /**
     * Authenticates a user based on the provided login credentials.
     *
     * This method processes a login request by validating the supplied username and password.
     * It checks for null values and responds with an appropriate error message if any are found.
     * If valid credentials are provided, the method retrieves the user from the database using
     * the username. If the user is not found or the password does not match, a 401 Unauthorized
     * response is returned. Otherwise, an OK response is returned, indicating successful login.
     *
     * @param request the login request containing the username and password
     * @return a Response indicating the outcome of the login attempt
     */
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

    
    /**
     * Requests a password reset for the user with the given email address.
     *
     * Generates a reset token and expiration date, updates the user's
     * reset token and expiration date in the database, and sends an
     * email containing a link to the reset password page with the
     * generated token. The email is sent to the user's email address.
     *
     * @param email the email address of the user to reset the password for
     * @return a response containing a message indicating the outcome of the request
     */
    @POST
    @Path("/reset-password-request")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response requestPasswordReset(Map<String, String> requestBody) {
        String email = requestBody.get("email");
        if (email == null || email.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Email is required").build();
        }

        User user = userRepository.findByEmail(email);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }

        String token = UUID.randomUUID().toString();
        LocalDateTime expiration = LocalDateTime.now().plusHours(2);
        userRepository.updateResetToken(token, expiration, user.getId());

        String resetLink = "localhost:3000/#/ResetPassword?token=" + token; // "http://your-frontend-site.com/reset-password?token=" + token;
        String subject = "Password Reset Request";
        String body = "To reset your password, click the following link: " + resetLink;

        try {
            EmailUtil.sendEmail(email, subject, body);
        } catch (MessagingException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error sending email: " + e.getMessage()).build();
        }

        return Response.ok().entity("Password reset email sent").build();
    }



    /**
     * Verifies a reset token and redirects to the reset password page if the
     * token is valid and not expired.
     *
     * @param token the reset token to verify
     * @return a 302 Found response with a location header pointing to the
     *         reset password page if the token is valid, a 401 Unauthorized
     *         response with a message indicating that the token is invalid or
     *         expired if the token is invalid or expired
     */
    @GET
    @Path("/verify-reset-token")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifyResetToken(@QueryParam("token") String token) {
        User user = userRepository.findByResetToken(token);
        if (user == null) {
            System.out.println("Invalid token: " + token);
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();
        }

        LocalDateTime now = LocalDateTime.now();
        System.out.println("Token: " + token);
        System.out.println("Stored Token: " + user.getResetToken());
        System.out.println("Token Expiration: " + user.getResetTokenExpiration());
        System.out.println("Current Time: " + now);

        if (user.getResetTokenExpiration().isBefore(now)) {
            System.out.println("Token has expired");
            return Response.status(Response.Status.UNAUTHORIZED).entity("Expired token").build();
        }

        // Redirect to the reset password page
        return Response.seeOther(URI.create("http://your-frontend-site.com/reset-password-page")).build();
    }


    /**
     * Resets the password of a user given a valid reset token and new password.
     *
     * @param token the reset token obtained via the /reset-password-request endpoint
     * @param newPassword the new password for the user
     * @return a Response indicating the outcome of the request
     */
    @POST
    @Path("/reset-password")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response resetPassword(Map<String, String> requestBody) {
        String token = requestBody.get("token");
        String newPassword = requestBody.get("newPassword");

        System.out.println("Received token: " + token);
        System.out.println("Received new password: " + newPassword);

        User user = userRepository.findByResetToken(token);
        if (user == null) {
            System.out.println("Invalid token");
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or expired token").build();
        }

        LocalDateTime now = LocalDateTime.now();
        if (user.getResetTokenExpiration().isBefore(now)) {
            System.out.println("Token has expired");
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or expired token").build();
        }

        // Update the user's password
        user.setPassword(newPassword);
        user.setResetToken(null);
        user.setResetTokenExpiration(null);

        // Save the updated user
        try {
            userRepository.updateUser(user);
        } catch (Exception e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error updating user").build();
        }

        return Response.ok().entity("Password reset successful").build();
    }

    @POST
    @Path("/user-info")
    public Response getUserByLogin(User userRequest) {
        // searching by username
        User user = userRepository.findByUsername(userRequest.getUsername());

        if (user != null) {
            return Response.ok(user).build();  // if user was found, return response 200 OK with user
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();  // if user wasn't found, return 404 Not Found
        }
    }

    @POST
    @Path("/user-edit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editUser(Map<String, String> requestBody) {
        // Get data from request
        String userId = requestBody.get("userId");
        String username = requestBody.get("username");
        String name = requestBody.get("name");
        String surname = requestBody.get("surname");
        String email = requestBody.get("email");

        System.out.println("Received request to edit user with ID: " + userId);
        System.out.println("Received request body: " + requestBody);

        // Validation userId
        Long userIdLong = null;
        if (userId != null && !userId.trim().isEmpty()) {
            try {
                userIdLong = Long.valueOf(userId);
            } catch (NumberFormatException e) {
                System.err.println("Invalid userId format: " + e.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid userId format").build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("userId is required").build();
        }

        // Searching the user in database
        User user = userRepository.findById(Long.valueOf(userId));
        if (user == null) {
            System.out.println("User not found");
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }

        // Update user data
        if (username != null && !username.trim().isEmpty()) {
            user.setUsername(username);
        }
        if (name != null && !name.trim().isEmpty()) {
            user.setName(name);
        }
        if (surname != null && !surname.trim().isEmpty()) {
            user.setSurname(surname);
        }
        if (email != null && !email.trim().isEmpty()) {
            user.setEmail(email);
        }

        // Save changed data
        try {
            userRepository.updateUser(user);
        } catch (Exception e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error updating user").build();
        }

        return Response.ok().entity("User updated successfully").build();
    }
}


