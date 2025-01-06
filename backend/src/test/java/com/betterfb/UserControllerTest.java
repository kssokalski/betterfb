package com.betterfb;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import jakarta.mail.MessagingException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import jakarta.ws.rs.core.Response;


import java.util.HashMap;
import java.util.Map;


/**
 * Tests for the UserController class
 */
public class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the loginUser method with valid credentials
     */
    @Test
    public void testLoginUser_ValidCredentials() {
        UserLoginRequest request = new UserLoginRequest();
        request.setUsername("validUser");
        request.setPassword("validPassword");

        User user = new User();
        user.setUsername("validUser");
        user.setPassword("validPassword");

        when(userRepository.findByUsername("validUser")).thenReturn(user);

        Response response = userController.loginUser(request);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    /**
     * Tests the loginUser method with invalid credentials
     */
    @Test
    public void testLoginUser_InvalidCredentials() {
        UserLoginRequest request = new UserLoginRequest();
        request.setUsername("invalidUser");
        request.setPassword("invalidPassword");

        when(userRepository.findByUsername("invalidUser")).thenReturn(null);

        Response response = userController.loginUser(request);

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        assertEquals("Niepoprawne dane logowania", response.getEntity());
    }

    /**
     * Tests the registerUser method with a valid user.
     *
     * Verifies that the user is saved to the database and that the response
     * status is 201 Created and the response entity is the saved user.
     */
    @Test
    public void testRegisterUser() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setEmail("test@example.com");
        doNothing().when(userRepository).save(user);

        Response response = userController.registerUser(user);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(user, response.getEntity());
    }


    /**
     * Tests the registerUser method with invalid user credentials.
     *
     * Verifies that the response status is 400 Bad Request and the response entity
     * contains the appropriate error message, depending on which of the required
     * fields (login, password, email) is missing.
     */
    @Test
    public void testRegisterUser_InvalidCredentials() {
        User user = new User();
        user.setUsername("");
        Response response = userController.registerUser(user);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Login is required", response.getEntity());

        user.setUsername("testUser");
        user.setPassword("");
        user.setEmail("email");
        response = userController.registerUser(user);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Password is required", response.getEntity());

        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setEmail("");
        response = userController.registerUser(user);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Email is required", response.getEntity());
    }

    /**
     * Tests the requestPasswordReset method with an empty email
     */
    @Test
    public void testRequestPasswordReset_EmailIsRequired() {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", "");

        Response response = userController.requestPasswordReset(requestBody);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Email is required", response.getEntity());
    }

    /**
     * Tests the requestPasswordReset method with a non-existent user
     */
    @Test
    public void testRequestPasswordReset_UserNotFound() {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", "nonexistent@example.com");

        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(null);

        Response response = userController.requestPasswordReset(requestBody);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("User not found", response.getEntity());
    }

    /**
     * Tests the requestPasswordReset method with a valid user
     */
    @Test
    public void testRequestPasswordReset_Success() {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", "user@example.com");

        User user = new User();
        user.setEmail("user@example.com");

        when(userRepository.findByEmail("user@example.com")).thenReturn(user);

        doNothing().when(userRepository).updateResetToken(anyString(), any(), anyLong());

        try (MockedStatic<EmailUtil> mockedEmailUtil = mockStatic(EmailUtil.class)) {
            mockedEmailUtil.when(() -> EmailUtil.sendEmail(anyString(), anyString(), anyString()))
                .thenAnswer(invocation -> null);

            Response response = userController.requestPasswordReset(requestBody);

            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            assertEquals("Password reset email sent", response.getEntity());
        }
    }
}

