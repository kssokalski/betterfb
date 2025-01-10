import React, { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import { useTheme } from "../components/ThemeContext";
import "../styles/Friends.css";

export function FriendRequestsPage() {
  const [friendRequests, setFriendRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Pobieramy userId z lokalizacji (zakładając, że jest przekazywane przez Router)
  const location = useLocation();
  const userId = location.state?.userId;

  useEffect(() => {
    if (userId) {
      // Wywołanie API po załadowaniu komponentu
      fetch(
        `http://localhost:8080/backend/api/auth/friend-requests?userId=${userId}`
      )
        .then((response) => response.json())
        .then((data) => {
          setFriendRequests(data);
          setLoading(false); // Zakończenie ładowania
        })
        .catch((error) => {
          console.error("Error fetching friend requests:", error);
          setLoading(false); // Zakończenie ładowania w przypadku błędu
        });
    }
  }, [userId]);

  const handleAccept = (requestId) => {
    fetch("http://localhost:8080/backend/api/auth/accept-friend-request", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ requestId: requestId }), // Wysyłamy requestId
    })
      .then((response) => {
        // Sprawdzamy, czy odpowiedź nie jest błędna
        if (!response.ok) {
          return response.text().then((text) => {
            console.error("Error response:", text); // Logujemy odpowiedź tekstową w przypadku błędu
            throw new Error(text);
          });
        }

        // Logujemy odpowiedź, aby sprawdzić, co jest zwrócone
        console.log("Response received:", response);

        // Próba sparsowania odpowiedzi jako JSON
        return response.json().catch((error) => {
          console.error("Error parsing JSON:", error); // Logujemy błąd, jeśli odpowiedź nie jest poprawnym JSON-em
          throw new Error("Response is not valid JSON");
        });
      })
      .then((data) => {
        if (data.message) {
          setFriendRequests(
            friendRequests.filter((request) => request.id !== requestId)
          );
        } else if (data.error) {
          setError(data.error); // Obsługujemy błąd zwrócony z serwera
        }
      })
      .catch((error) => {
        console.error("Error accepting friend request:", error);
        setError("An error occurred while accepting the request.");
      });
  };

  if (loading) {
    return <div>Loading...</div>; // Wyświetlanie komunikatu o ładowaniu
  }

  const { darkMode, toggleTheme } = useTheme();

  return (
    <div className={darkMode ? "friendsD" : "friendsL"}>
      <h1>Friend Requests</h1>
      {error && <p style={{ color: "red" }}>{error}</p>}
      {friendRequests.length === 0 ? (
        <p>No friend requests received.</p>
      ) : (
        <ul>
          {friendRequests.map((friend) => (
            <li key={friend.id}>
              {friend.username}{" "}
              <button onClick={() => handleAccept(friend.id)}>Accept</button>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
