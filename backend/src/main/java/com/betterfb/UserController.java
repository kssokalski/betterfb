package com.betterfb;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        // Verify that the login, password and email are not empty
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Login is required")
                    .build();
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Password is required")
                    .build();
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Email is required")
                    .build();
        }

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
     * Requests a password reset for the user with the specified email address.
     *
     * This method processes a password reset request by retrieving the user from the
     * database using the provided email address. If the user is not found, a 404 Not
     * Found response is returned. Otherwise, a reset token is generated, stored in the
     * database, and sent to the user via email. The method returns a successful response
     * with a message indicating that the password reset email has been sent.
     *
     * @param requestBody a Map containing the email address of the user to request a
     *                    password reset for
     * @return a Response indicating the outcome of the password reset request
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

        String resetLink = "http://localhost:3000/#/ResetPassword?token=" + token;
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
     * This method processes a password reset request by retrieving the user from the
     * database using the provided reset token. If the user is not found, a 404 Not
     * Found response is returned. Otherwise, the method updates the user's password
     * to the new password and resets the reset token and expiration date. If the
     * reset token is invalid or expired, a 401 Unauthorized response is returned.
     * If the user is found and the password is reset successfully, a 200 OK response
     * is returned with a message indicating that the password was reset successfully.
     *
     * @param requestBody a Map containing the reset token and new password
     * @return a Response indicating the outcome of the password reset request
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

    /**
     * Returns the user information for the user with the given username.
     *
     * @param userRequest a User object with the username to search for
     * @return a Response with the user information if the user was found, or a 404 Not Found response if the user wasn't found
     */
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

    /**
     * Edits the user information for the user with the given userId.
     *
     * Requires the userId, username, name, surname and email to be provided in the request body.
     * If any of the fields are not provided, they will not be updated.
     *
     * @param requestBody a Map containing the userId, username, name, surname and email to be updated
     * @return a Response with the updated user information if the user was found, or a 404 Not Found response if the user wasn't found
     */
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

    /**
     * Searches for users which match the given search query in their username, name or surname.
     * The search is case-insensitive and the search query is treated as a substring to be found.
     * The user with the id given as the second parameter is excluded from the search results.
     *
     * @param query the search query to be searched for in the usernames, names and surnames of the users
     * @param loggedInUserId the id of the user which should be excluded from the search results
     * @return a list of users which match the search query
     */
    @GET
    @Path("/search-users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchUsers(@QueryParam("query") String query, @QueryParam("loggedInUserId") Long loggedInUserId) {
        List<User> users = userRepository.findUsersForFriendSearch(query, loggedInUserId);
        return Response.ok(users).build();
    }

    /**
     * Sends a friend request to the user with the given ID.
     *
     * @param requestBody a map containing the IDs of the sender and receiver
     * @return a response indicating the outcome of the request
     */
    @POST
    @Path("/send-friend-request")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendFriendRequest(Map<String, Long> requestBody) {
        Long senderId = requestBody.get("senderId");
        Long receiverId = requestBody.get("receiverId");
        User sender = userRepository.findById(senderId);
        User receiver = userRepository.findById(receiverId);
        
        if (sender == null || receiver == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid user IDs").build();
        }

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(sender);
        friendRequest.setReceiver(receiver);
        friendRequest.setSentTime(LocalDateTime.now());
        friendRequest.setAccepted(false);

        try {
            userRepository.saveFriendRequest(friendRequest);
            return Response.ok().entity("Friend request sent").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e.getMessage()).build();
        }
    }

    /**
     * Accepts a friend request with the given ID.
     *
     * @param requestBody a map containing the ID of the friend request to accept
     * @return a response indicating the outcome of the request
     */
    @POST
    @Path("/accept-friend-request")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response acceptFriendRequest(Map<String, Long> requestBody) {
    Long requestId = requestBody.get("requestId");
    FriendRequest friendRequest = userRepository.findFriendRequestById(requestId);

    if (friendRequest == null) {
        // Zwróć błąd w formacie JSON, a nie zwykły tekst
        return Response.status(Response.Status.NOT_FOUND)
                       .entity("{\"error\": \"Friend request not found\"}")
                       .build();
    }

    friendRequest.setAccepted(true);
    try {
        userRepository.updateFriendRequest(friendRequest);
        return Response.ok("{\"message\": \"Friend request accepted\"}").build();
    } catch (Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                       .entity("{\"error\": \"Error: " + e.getMessage() + "\"}")
                       .build();
    }
}



    /**
     * Gets a list of all friends of the user with the given ID.
     *
     * @param userId the ID of the user whose friends are to be retrieved
     * @return a response containing a list of the user's friends
     */
    @GET
    @Path("/friends")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFriends(@QueryParam("userId") Long userId) {
        User user = userRepository.findById(userId);

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }

        List<User> friendsSent = userRepository.findFriendsSent(userId);
        List<User> friendsReceived = userRepository.findFriendsReceived(userId);

        Set<User> allFriends = new HashSet<>();
        allFriends.addAll(friendsSent);
        allFriends.addAll(friendsReceived);

        return Response.ok(new ArrayList<>(allFriends)).build();
    }

    @GET
    @Path("/friend-requests")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFriendsRequest(@QueryParam("userId") Long userId) {
        User user = userRepository.findById(userId);

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }

        List<User> friendsReceived = userRepository.findFriendRequests(userId);

        Set<User> allFriends = new HashSet<>();
        allFriends.addAll(friendsReceived);

        return Response.ok(new ArrayList<>(allFriends)).build();
    }




}


