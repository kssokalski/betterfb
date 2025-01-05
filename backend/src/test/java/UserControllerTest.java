import  com.betterfb.User;
import com.betterfb.UserLoginRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

class UserControllerTest {

    @BeforeAll
    static void setup() {
        // Ustawienie podstawowego URL dla API
        RestAssured.baseURI = "http://localhost:8080";
        RestAssured.basePath = "/backend/api/auth";
    }
/*
    // Test rejestracji użytkownika
    @Test
    void testRegisterUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("TestowehasloTakiedlugieWoW123!!!!");
        user.setEmail("testuser@example.com");

        given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/register")
                .then()
                .statusCode(201)
                .body("username", equalTo("testuser"))
                .body("email", equalTo("testuser@example.com"));
    }

    // Test rejestracji z brakującymi danymi
    @Test
    void testRegisterUserWithMissingData() {
        User user = new User();
        user.setUsername("incompleteuser");

        given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/register")
                .then()
                .statusCode(400);  // Oczekiwany status to 400 Bad Request, ponieważ brakuje hasła i emaila
    }
    @Test
    void testRegisterUserWithEmptyUsername() {
        User user = new User();
        user.setUsername("   ");
        user.setPassword("testuser");
        user.setEmail("testuser@example.com");

        given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/register")
                .then()
                .statusCode(400)
                .body(equalTo("Nazwa użytkownika nie może być pusta lub zawierać tylko spacje."));
    }
*/
    // Test logowania użytkownika
//    @Test
//    void testLoginUser() {
//        UserLoginRequest request = new UserLoginRequest();
//        request.setUsername("testuser");
//        request.setPassword("TestowehasloTakiedlugieWoW123!!!!");
//
//        given()
//                .contentType(ContentType.JSON)
//                .body(request)
//                .when()
//                .post("/login")
//                .then()
//                .statusCode(200);  // Oczekiwany status to 200 OK
//    }
//
//    // Test logowania z nieprawidłowymi danymi
//    @Test
//    void testLoginWithInvalidCredentials() {
//        UserLoginRequest request = new UserLoginRequest();
//        request.setUsername("wronguser");
//        request.setPassword("wrongpassword");
//
//        given()
//                .contentType(ContentType.JSON)
//                .body(request)
//                .when()
//                .post("/login")
//                .then()
//                .statusCode(401)  // Oczekiwany status to 401 Unauthorized
//                .body(equalTo("Niepoprawne dane logowania"));
//    }

}
