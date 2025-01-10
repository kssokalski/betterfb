import React, { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import { useTheme } from "../components/ThemeContext";
import "../styles/Friends.css";

export function FriendsList() {
  const location = useLocation();
  const { userId } = location.state || {};

  const [friends, setFriends] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (userId) {
      fetch(`http://localhost:8080/backend/api/auth/friends?userId=${userId}`)
        .then((response) => response.json())
        .then((data) => setFriends(data))
        .catch((error) => {
          console.error("Error fetching friends:", error);
          setError("Nie udało się pobrać listy znajomych");
        });
    }
  }, [userId]);

  if (error) {
    return <div>{error}</div>;
  }

  const { darkMode, toggleTheme } = useTheme();

  return (
    <div className={darkMode ? "friendsD" : "friendsL"}>
      <h1>Znajomi</h1>
      <ul>
        {friends.length === 0 ? (
          <li>Brak znajomych</li>
        ) : (
          friends.map((friend) => (
            <li key={friend.id}>
              {friend.username} {friend.firstName} {friend.lastName}
            </li>
          ))
        )}
      </ul>
    </div>
  );
}
