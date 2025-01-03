import React, { useState } from "react";
import { useSearchParams } from "react-router-dom";
import "./Forms.css";
import { ThemeProvider, useTheme } from "../components/ThemeContext";

export function ResetPassword() {
  const [newPassword, setNewPassword] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [searchParams] = useSearchParams();
  const token = searchParams.get("token");

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
        setMessage("Hasło zostało zresetowane.");
      } else {
        const errorText = await response.text();
        setError(`Błąd: ${errorText}`);
      }
    } catch (error) {
      setError(`Błąd: ${error.message}`);
    }
  };

  if (!token) {
    return <p>Błąd: Brak tokena resetu.</p>;
  }

  const { darkMode, toggleTheme } = useTheme();

  return (
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
