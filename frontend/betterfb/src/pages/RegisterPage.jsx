import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { ThemeProvider, useTheme } from "../components/ThemeContext";
import "./Forms.css";

/**
 * A functional component representing the registration page.
 *
 * This component allows users to create a new account by providing a username, password,
 * and email address. It validates the input fields, sends the registration data to the server,
 * and handles errors or success messages accordingly.
 *
 * @returns {JSX.Element} The registration page component.
 */
export function RegisterPage() {
  // State to hold the login (username) input value.
  const [login, setLogin] = useState("");

  // State to hold the password input value.
  const [password, setPassword] = useState("");

  // State to hold the email input value.
  const [email, setEmail] = useState("");

  // State to hold error messages (if any).
  const [error, setError] = useState("");

  // State to hold success messages (if any).
  const [successMessage, setSuccessMessage] = useState("");

  // Hook for navigation to other pages.
  const navigate = useNavigate();

  /**
   * Validates the email format using a regular expression.
   *
   * @returns {boolean} True if the email is valid, false otherwise.
   */
  const validEmail = () => {
    const regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return regex.test(email);
  };

  /**
   * Validates the password format using a regular expression.
   * The password must contain at least one uppercase letter, one number,
   * one special character, and be at least 8 characters long.
   *
   * @returns {boolean} True if the password is valid, false otherwise.
   */
  const validPassword = () => {
    const regex = /^(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,}$/;
    return regex.test(password);
  };

  /**
   * Handles the form submission for user registration.
   *
   * First, validates the email and password inputs. If valid, sends the registration
   * data to the backend via a POST request. Displays either a success message or an error message.
   *
   * @param {React.FormEvent} e - The form submission event.
   */
  const handleSubmit = async (e) => {
    e.preventDefault(); // Prevents the default form submission behavior.
    setError(""); // Reset error state before validating.

    // Validate the password format.
    if (!validPassword(password)) {
      setError(
        "Nieprawidłowe hasło. Co najmniej 8 znaków, duże i małe litery, cyfry i znaki specjalne"
      );
      return;
    }

    // Validate the email format.
    if (!validEmail(email)) {
      setError("Nieprawidłowy adres email.");
      return;
    }

    // Prepare user data for registration.
    const userData = {
      username: login,
      password: password,
      email: email,
    };

    try {
      // Send registration data to the backend server.
      const response = await fetch(
        "http://localhost:8080/backend/api/auth/register",
        {
          method: "POST",
          headers: { "Content-type": "application/json" },
          body: JSON.stringify(userData),
        }
      );

      // Handle successful registration.
      if (response.ok) {
        setSuccessMessage("Rejestracja przebiegła pomyślnie");
        setTimeout(() => {
          navigate("/"); // Navigate to login page after successful registration.
        }, 2000);
      } else {
        // Handle errors from the backend response.
        setError("Błąd w trakcie rejestracji");
      }
    } catch (error) {
      // Handle server connection errors.
      setError("Błąd w połączeniu z serwerem");
    }
  };

  // Destructure theme state and toggler from ThemeContext.
  const { darkMode, toggleTheme } = useTheme();

  return (
    <div className={darkMode ? "formsD" : "formsL"}>
      {/* Registration Page Header */}
      <h1>Rejestracja</h1>

      {/* Registration Form */}
      <form onSubmit={handleSubmit}>
        <div>
          {/* Input for username */}
          <input
            type="text"
            placeholder="Login"
            value={login}
            onChange={(e) => setLogin(e.target.value)}
            required
          />
        </div>

        <div>
          {/* Input for password */}
          <input
            type="password"
            placeholder="Hasło"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>

        <div>
          {/* Input for email */}
          <input
            type="text"
            placeholder="Adres e-mail"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>

        {/* Display error messages */}
        {error && (
          <div className={darkMode ? "darkError" : "lightError"}>{error}</div>
        )}

        {/* Display success messages */}
        {successMessage && (
          <div className={darkMode ? "darkSuccess" : "lightSuccess"}>
            {successMessage}
          </div>
        )}

        {/* Submit button for registration */}
        <button type="submit">Zarejestruj</button>
      </form>

      {/* Button to toggle light/dark mode */}
      <button onClick={toggleTheme}>
        Przełącz na {darkMode ? "Jasny" : "Ciemny"} tryb
      </button>
    </div>
  );
}
