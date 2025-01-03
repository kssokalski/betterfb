import React, { useState } from "react";
import "./Forms.css";
import { ThemeProvider, useTheme } from "../components/ThemeContext";

export function ResetPasswordRequest() {
  const [email, setEmail] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

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
        setMessage("Wysłano e-mail z linkiem do resetu hasła.");
      } else {
        const errorText = await response.text();
        setError(`Błąd: ${errorText}`);
      }
    } catch (error) {
      setError(`Błąd: ${error.message}`);
    }
  };

  const { darkMode, toggleTheme } = useTheme();

  return (
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
