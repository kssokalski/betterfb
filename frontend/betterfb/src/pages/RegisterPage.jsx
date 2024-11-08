import { useState } from "react";
import { useNavigate } from "react-router-dom";

export function RegisterPage() {
  const [login, setLogin] = useState("");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");
  const [error, setError] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    const userData = {
      username: login,
      password: password,
      email: email,
    };

    try {
      const response = await fetch(
        "http://localhost:8080/backend/api/auth/register",
        {
          method: "POST",
          headers: { "Content-type": "application/json" },
          body: JSON.stringify(userData),
        }
      );

      if (response.ok) {
        setSuccessMessage("Rejestracja przebiegła pomyślnie");
        setTimeout(() => {
          navigate("/");
        }, 200);
      } else {
        setError("Błąd w trakcie rejestracji");
      }
    } catch (error) {
      setError("Błąd w połączeniu z serwerem");
    }
  };

  return (
    <>
      <h1>Rejestracja</h1>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Login"
          value={login}
          onChange={(e) => setLogin(e.target.value)}
        />
        <input
          type="password"
          placeholder="Hasło"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <input
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        {error && <div>{error}</div>}
        {successMessage && <div>{successMessage}</div>}
        <button type="submit">Zarejestruj</button>
      </form>
    </>
  );
}
