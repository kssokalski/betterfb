import React, { useState, useEffect } from "react";
import { useLocation, Link } from "react-router-dom";
import { ThemeProvider, useTheme } from "../components/ThemeContext";
import "../styles/Home.css";

export function HomePage() {
  const location = useLocation();
  const { username } = location.state || {};
  const [user, setUser] = useState(null);

  useEffect(() => {
    if (username) {
      fetch("http://localhost:8080/backend/api/auth/user-info", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username }),
      })
        .then((response) => response.json())
        .then((data) => setUser(data))
        .catch((error) => console.error("Error fetching user data:", error));
    }
  }, [username]);

  if (!user) {
    return <div>Loading...</div>;
  }

  const { darkMode, toggleTheme } = useTheme();

  return (
    <div className={darkMode ? "homeD" : "homeL"}>
      <div id={darkMode ? "userWelcomeD" : "userWelcomeL"}>
        <h1>
          Witaj {<Link to={`/profile/${user.username}`}>{user.username}</Link>}
        </h1>
      </div>
      <button onClick={toggleTheme}>
        Przełącz na {darkMode ? "Jasny" : "Ciemny"} tryb
      </button>
    </div>
  );
}
