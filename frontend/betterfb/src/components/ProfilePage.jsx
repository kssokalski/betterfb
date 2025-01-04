import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { ThemeProvider, useTheme } from "../components/ThemeContext";
import "../styles/Profile.css";

export function ProfilePage() {
  const { username } = useParams();
  const [user, setUser] = useState(null);

  useEffect(() => {
    if (username) {
      console.log("Sending request to server with username:", username);
      fetch("http://localhost:8080/backend/api/auth/user-info", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username }),
      })
        .then((response) => response.json())
        .then((data) => {
          console.log("Data received:", data);
          setUser(data);
        })
        .catch((error) => console.error("Error fetching user data:", error));
    }
  }, [username]);

  if (!user) {
    return <div>Loading...</div>;
  }

  const { darkMode, toggleTheme } = useTheme();

  return (
    <div className={darkMode ? "profileD" : "profileL"}>
      <button onClick={toggleTheme}>
        Przełącz na {darkMode ? "Jasny" : "Ciemny"} tryb
      </button>
      <h1>Profil użytkownika {user.username} </h1>
      <p id="username">Username: {user.username}</p>
      <p id="name">Imię: {user.name}</p>
      <p id="surname">Nazwisko: {user.surname}</p>
      <p id="email">Adres e-mail: {user.email}</p>
      <hr />
      <p id="postsInfo">Posty użytkownika</p>
    </div>
  );
}
