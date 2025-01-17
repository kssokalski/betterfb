import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { ThemeProvider, useTheme } from "../components/ThemeContext";
import ReCAPTCHA from "react-google-recaptcha";
import "./Forms.css";

/**
 * A functional component representing the login page.
 *
 * This component allows users to log in by submitting their username and password.
 * It handles the form submission, sends a POST request to the server, and handles
 * potential errors (e.g., incorrect credentials or connection issues).
 *
 * @returns {JSX.Element} The login page component.
 */
export function LoginPage() {
  // State to hold the login (username) input value.
  const [login, setLogin] = useState("");

  // State to hold the password input value.
  const [password, setPassowrd] = useState("");

  // State to hold error messages (if any).
  const [error, setError] = useState("");

  // Hook for navigation to other pages.
  const navigate = useNavigate();

  // reCAPTCHA verification state
  const [captchaVerified, setCaptchaVerified] = useState(false);

  // reCAPTCHA site key
  const siteKey = "6LfVzJgqAAAAAJhMJmhQkRJNsIMZN2wZoyYLToCj";

  const [attemptedSubmit, setAttemptedSubmit] = useState(false);

  const handleCaptcha = (value) => {
    if (value) {
      setCaptchaVerified(true);
    } else {
      setCaptchaVerified(false);
    }
  };

  /**
   * Handles the form submission for logging in.
   *
   * Sends the login data to the server via a POST request. If the login is successful,
   * the user is redirected to the home page. If the login fails or there is an error,
   * an appropriate error message is displayed.
   *
   * @param {React.FormEvent} e - The form submission event.
   */
  const handleSubmit = async (e) => {
    e.preventDefault();

    setAttemptedSubmit(true);

    if (!captchaVerified) {
      return; // Nie wysyłaj formularza, jeśli captcha nie jest zaznaczona.
    }

    // Prepare user data for login.
    const userData = {
      username: login,
      password: password,
    };

    try {
      // Sending a POST request to the server with user credentials.
      const response = await fetch(
        "http://localhost:8080/backend/api/auth/login",
        {
          method: "POST",
          headers: { "Content-type": "application/json" },
          body: JSON.stringify(userData),
        }
      );

      // Checking if the response is OK (successful login).
      if (response.ok) {
        navigate("/HomePage", { state: { username: login } }); // Navigate to the home page on successful login.
      } else {
        setError("Niepoprawne dane logowania"); // Display an error message if login fails.
      }
    } catch (error) {
      // Handling any connection errors.
      setError("Błąd połączenia z serwerem");
    }
  };

  // Destructure theme context values.
  const { darkMode, toggleTheme } = useTheme();

  return (
    // Render the login form with dynamic theme classes based on darkMode.
    <div className={darkMode ? "formsD" : "formsL"}>
      <h1>LOGOWANIE</h1>
      <form onSubmit={handleSubmit}>
        <div>
          {/* Input for the login (username) */}
          <input
            type="text"
            placeholder="Login"
            value={login}
            onChange={(e) => setLogin(e.target.value)}
          />
        </div>
        <div>
          {/* Input for the password */}
          <input
            type="password"
            placeholder="Hasło"
            value={password}
            onChange={(e) => setPassowrd(e.target.value)}
          />
        </div>
        <div>
          {/* reCAPTCHA widget */}
          <ReCAPTCHA
            className="captcha"
            sitekey={siteKey}
            onChange={handleCaptcha}
          />
        </div>
        {/* Display an error message if any */}
        {!captchaVerified && attemptedSubmit && (
          <div className={darkMode ? "darkCaptchaError" : "lightCaptchaError"}>
            Zaznacz captcha
          </div>
        )}
        {error && (
          <div className={darkMode ? "darkError" : "lightError"}>{error}</div>
        )}
        <button type="submit">Zaloguj</button>
      </form>
      <p id="resetPass">
        {/* Link to the registration page */}
        Zapomniałeś hasła?{" "}
        <Link to="/ResetPasswordRequest">Zresetuj je tutaj</Link>
      </p>
      <p id="noAccount">
        {/* Link to the registration page */}
        Nie masz konta? <Link to="/RegisterPage">Załóż je tutaj</Link>
      </p>
      {/* Button to toggle between light and dark modes */}
      <button onClick={toggleTheme}>
        Przełącz na {darkMode ? "Jasny" : "Ciemny"} tryb
      </button>
    </div>
  );
}
