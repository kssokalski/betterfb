import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useTheme } from "../components/ThemeContext";
import "../styles/Profile.css";
import { Link } from "react-router-dom";
import { jsPDF } from "jspdf";

/**
 * A functional component representing the profile page.
 *
 * This component fetches and displays user profile information.
 * It also allows the user to toggle between light and dark mode and provides
 * a link to edit the profile.
 *
 * @returns {JSX.Element} The profile page component.
 */
export function ProfilePage() {
  // Get the username from the URL parameters
  const { username } = useParams();

  // Hook for navigation to other pages
  const navigate = useNavigate();

  // State to hold the current user data
  const [user, setUser] = useState(null);

  // Effect to fetch user data when the component mounts or the username changes
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

  // Display a loading message while fetching user data
  if (!user) {
    return <div>Loading...</div>;
  }

  // Destructure theme context values
  const { darkMode, toggleTheme } = useTheme();

  const generatePDF = () => {
    const doc = new jsPDF();

    doc.setFontSize(16);
    doc.text(`Profil uzytkownika: ${user.username}`, 10, 10);
    doc.text(`Imie: ${user.name}`, 10, 20);
    doc.text(`Nazwisko: ${user.surname}`, 10, 30);
    doc.text(`Adres e-mail: ${user.email}`, 10, 40);

    doc.save(`${user.username}_profil.pdf`);
  };

  return (
    <div className={darkMode ? "profileD" : "profileL"}>
      <button onClick={toggleTheme}>
        Przełącz na {darkMode ? "Jasny" : "Ciemny"} tryb
      </button>
      <h1>Profil użytkownika {user.username} </h1>
      <Link to={`/EditProfile/${username}`}>
        <button>Edytuj profil</button>
      </Link>
      <p id="username">Username: {user.username}</p>
      <p id="name">Imię: {user.name}</p>
      <p id="surname">Nazwisko: {user.surname}</p>
      <p id="email">Adres e-mail: {user.email}</p>
      <hr />
      <p id="postsInfo">Posty użytkownika</p>
      <button
        onClick={() =>
          navigate("/HomePage", { state: { username: user.username } })
        }
      >
        Przejdź na główną stronę
      </button>
      <button onClick={generatePDF}>Generuj PDF z profilu</button>
    </div>
  );
}
