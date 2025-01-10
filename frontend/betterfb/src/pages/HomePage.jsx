import React, { useState, useEffect } from "react";
import { useLocation, Link } from "react-router-dom";
import { useTheme } from "../components/ThemeContext";
import "../styles/Home.css";

/**
 * A functional component representing the home page.
 *
 * This component fetches and displays a welcome message for the logged-in user.
 * It also allows the user to toggle between light and dark mode and provides
 * a link to the profile page.
 *
 * @returns {JSX.Element} The home page component.
 */
export function HomePage() {
  // Get the username from the location state
  const location = useLocation();
  const { username } = location.state || {};

  // State to hold the current user data
  const [user, setUser] = useState(null);

  // Effect to fetch user data when the component mounts or the username changes
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

  // Display a loading message while fetching user data
  if (!user) {
    return <div>Loading...</div>;
  }

  // Destructure theme context values
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
      <button>{<Link to={"/"}>Wyloguj</Link>}</button>
      <button>
        <Link to="/SearchUsers" state={{ userId: user.id }}>
          Znajdź znajomych
        </Link>
      </button>
      <button>
        <Link to="/FriendRequests" state={{ userId: user.id }}>
          Zaproszenia
        </Link>
      </button>
      <button>
        <Link to="/Friends" state={{ userId: user.id }}>
          Lista znajomych
        </Link>
      </button>
    </div>
  );
}
