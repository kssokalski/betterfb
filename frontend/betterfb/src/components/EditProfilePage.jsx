import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useTheme } from "../components/ThemeContext";
import "../styles/Profile.css";

/**
 * A functional component representing the edit profile page.
 *
 * This component allows users to edit their profile information.
 * It fetches the current user data from the server, displays it in a form,
 * and allows the user to update and save changes.
 *
 * @returns {JSX.Element} The edit profile page component.
 */
export function EditProfilePage() {
  // Get the username from the URL parameters
  const { username } = useParams();

  // Hook for navigation to other pages
  const navigate = useNavigate();

  // State to hold the current user data
  const [user, setUser] = useState(null);

  // State to hold the form data
  const [formData, setFormData] = useState({
    username: "",
    name: "",
    surname: "",
    email: "",
  });

  // State to hold success or error messages
  const [message, setMessage] = useState("");

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
          // Set the form data with the fetched user data
          setFormData({
            username: data.username,
            name: data.name,
            surname: data.surname,
            email: data.email,
          });
        })
        .catch((error) => console.error("Error fetching user data:", error));
    }
  }, [username]);

  /**
   * Handles changes to the form inputs.
   *
   * Updates the corresponding field in the form data state.
   *
   * @param {React.ChangeEvent<HTMLInputElement>} e - The input change event.
   */
  const handleChange = (e) => {
    const { id, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [id]: value,
    }));
  };

  /**
   * Handles saving the updated profile data.
   *
   * Sends the updated data to the server via a POST request.
   * On success, displays a message and navigates to the profile page after a delay.
   */
  const handleSave = () => {
    fetch("http://localhost:8080/backend/api/auth/user-edit", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ ...formData, userId: user.id }),
    })
      .then((response) => {
        console.log("HTTP response:", response);
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.text();
      })
      .then((text) => {
        console.log("Response text:", text);
        try {
          const jsonData = JSON.parse(text);
          console.log("Parsed JSON:", jsonData);
          setMessage("Profil zaktualizowany pomyślnie!");
        } catch (e) {
          console.log("Error parsing JSON:", e);
          setMessage(text);
        }
        setTimeout(() => {
          navigate(`/profile/${formData.username}`);
        }, 2000);
      })
      .catch((error) => {
        console.error("Error updating user data:", error);
        setMessage("Wystąpił błąd podczas aktualizacji profilu.");
      });
  };

  // Display a loading message while fetching user data
  if (!user) {
    return <div>Loading...</div>;
  }

  // Destructure theme context values
  const { darkMode, toggleTheme } = useTheme();

  return (
    // Render the edit profile form
    <div className={darkMode ? "profileD" : "profileL"}>
      <h1>Edytuj Profil {user.username}</h1>
      <div>
        <input
          id="username"
          type="text"
          value={formData.username}
          onChange={handleChange}
        />
        <br />
        <input
          id="name"
          type="text"
          value={formData.name}
          onChange={handleChange}
        />
        <br />
        <input
          id="surname"
          type="text"
          value={formData.surname}
          onChange={handleChange}
        />
        <br />
        <input
          id="email"
          type="text"
          value={formData.email}
          onChange={handleChange}
        />
        <br />
        <button onClick={handleSave}>Zapisz</button>
        {message && <p>{message}</p>}
      </div>
    </div>
  );
}
