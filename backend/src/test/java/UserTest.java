import com.betterfb.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void testUserId() {
        User user = new User();
        user.setId(1L);

        assertEquals(1L, user.getId());
    }

    @Test
    void testUsername() {
        User user = new User();
        user.setUsername("testuser");

        assertEquals("testuser", user.getUsername());
    }

    @Test
    void testPassword() {
        User user = new User();
        user.setPassword("password123");

        assertEquals("password123", user.getPassword());
    }

    @Test
    void testEmail() {
        User user = new User();
        user.setEmail("testuser@example.com");

        assertEquals("testuser@example.com", user.getEmail());
    }
}
