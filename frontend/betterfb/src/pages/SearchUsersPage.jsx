import React, { useState } from "react";
import { useLocation } from "react-router-dom";
import { useTheme } from "../components/ThemeContext";
import "../styles/Friends.css";

export function SearchUsersPage() {
  const location = useLocation();
  const { userId } = location.state || {}; // Pobierz userId ze state

  const [query, setQuery] = useState("");
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSearch = () => {
    if (!userId) {
      setError("Brak ID użytkownika. Spróbuj ponownie zalogować się.");
      return;
    }

    setLoading(true);
    setError(null);

    const url = new URL("http://localhost:8080/backend/api/auth/search-users");
    url.searchParams.append("query", query);
    url.searchParams.append("loggedInUserId", userId);

    fetch(url, {
      method: "GET",
      headers: { "Content-Type": "application/json" },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Błąd podczas wyszukiwania użytkowników");
        }
        return response.json();
      })
      .then((data) => setResults(data))
      .catch((error) => setError(error.message))
      .finally(() => setLoading(false));
  };

  const sendFriendRequest = (receiverId) => {
    if (!userId) {
      setError("Brak ID użytkownika. Spróbuj ponownie zalogować się.");
      return;
    }

    setLoading(true);
    setError(null);

    fetch("http://localhost:8080/backend/api/auth/send-friend-request", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        senderId: userId,
        receiverId,
      }),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Błąd podczas wysyłania zaproszenia.");
        }
        // Jeśli odpowiedź to zwykły tekst
        return response.text();
      })
      .then((message) => {
        console.log(message); // "Friend request sent"
        alert(message); // Wyświetl komunikat użytkownikowi
      })
      .catch((error) => setError(error.message))
      .finally(() => setLoading(false));
  };

  const { darkMode, toggleTheme } = useTheme();

  return (
    <div className={darkMode ? "friendsD" : "friendsL"}>
      <h1>Znajdź znajomych</h1>
      <input
        type="text"
        placeholder="Wpisz nazwę użytkownika"
        value={query}
        onChange={(e) => setQuery(e.target.value)}
      />
      <button onClick={handleSearch}>Szukaj</button>

      {loading && <p>Ładowanie...</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}

      <ul>
        {results.map((user) => (
          <li key={user.id}>
            {user.username}{" "}
            <button onClick={() => sendFriendRequest(user.id)}>
              Wyślij zaproszenie
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}
