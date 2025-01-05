import React, { useState } from "react";
import "./Forms.css";
import { useTheme } from "../components/ThemeContext";

/**
 * A functional component representing the reset password request page.
 *
 * This component allows users to request a password reset by submitting their email address.
 * It handles the form submission, sends a POST request to the server, and displays
 * appropriate messages based on the response.
 *
 * @returns {JSX.Element} The reset password request page component.
 */
export function ResetPasswordRequest() {
  // State to hold the email input value
  const [email, setEmail] = useState("");

  // State to hold success or information messages
  const [message, setMessage] = useState("");

  // State to hold error messages
  const [error, setError] = useState("");

  /**
   * Handles the form submission for requesting a password reset.
   *
   * Sends the email address to the server via a POST request.
   * Displays a success message if the request is successful,
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
        "http://localhost:8080/backend/api/auth/reset-password-request",
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ email }),
        }
      );

      if (response.ok) {
        setMessage("Wysłano e-mail z linkiem do resetu hasła."); // Display success message
      } else {
        const errorText = await response.text();
        setError(`Błąd: ${errorText}`); // Display error message from server
      }
    } catch (error) {
      setError(`Błąd: ${error.message}`); // Display connection error message
    }
  };

  // Destructure theme context values
  const { darkMode, toggleTheme } = useTheme();

  return (
    // Render the reset password request form with dynamic theme classes based on darkMode
    <div className={darkMode ? "formsD" : "formsL"}>
      <h1>Reset hasła</h1>
      <form onSubmit={handleSubmit}>
        <div>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            placeholder="E-mail"
          />
        </div>
        <button type="submit">Wyślij</button>
      </form>
      {message && <p style={{ color: "green" }}>{message}</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}
    </div>
  );
}
