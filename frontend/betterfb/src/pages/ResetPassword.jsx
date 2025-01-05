import React, { useState } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import "./Forms.css";
import { useTheme } from "../components/ThemeContext";

/**
 * A functional component representing the reset password page.
 *
 * This component allows users to reset their password by submitting a new password.
 * It handles the form submission, sends a POST request to the server with the new password
 * and token, and displays appropriate messages based on the response.
 *
 * @returns {JSX.Element} The reset password page component.
 */
export function ResetPassword() {
  // State to hold the new password input value
  const [newPassword, setNewPassword] = useState("");

  // State to hold success or information messages
  const [message, setMessage] = useState("");

  // State to hold error messages
  const [error, setError] = useState("");

  // Hook to access URL search parameters
  const [searchParams] = useSearchParams();

  // Hook for navigation to other pages
  const navigate = useNavigate();

  // Get the reset token from URL search parameters
  const token = searchParams.get("token");

  /**
   * Handles the form submission for resetting the password.
   *
   * Sends the new password and token to the server via a POST request.
   * Displays a success message if the password is reset successfully,
   * otherwise displays an error message.
   *
   * @param {React.FormEvent} e - The form submission event.
   */
  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage("");
    setError("");

    try {
      const response = await fetch(
        "http://localhost:8080/backend/api/auth/reset-password",
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ token, newPassword }),
        }
      );

      if (response.ok) {
        setMessage("Hasło zostało zresetowane."); // Display success message
        setTimeout(() => {
          navigate("/"); // Redirect to the login page after 2 seconds
        }, 2000);
      } else {
        const errorText = await response.text();
        setError(`Błąd: ${errorText}`); // Display error message from server
      }
    } catch (error) {
      setError(`Błąd: ${error.message}`); // Display connection error message
    }
  };

  // Display an error message if the token is missing
  if (!token) {
    return <p>Błąd: Brak tokena resetu.</p>;
  }

  // Destructure theme context values
  const { darkMode, toggleTheme } = useTheme();

  return (
    // Render the reset password form with dynamic theme classes based on darkMode
    <div className={darkMode ? "formsD" : "formsL"}>
      <h1>Ustaw nowe hasło</h1>
      <form onSubmit={handleSubmit}>
        <div>
          <input
            type="password"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
            required
            placeholder="Nowe hasło"
          />
        </div>
        <button type="submit">Zresetuj Hasło</button>
      </form>
      {message && <p style={{ color: "green" }}>{message}</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}
      <button onClick={toggleTheme}>
        Przełącz na {darkMode ? "Jasny" : "Ciemny"} tryb
      </button>
    </div>
  );
}
