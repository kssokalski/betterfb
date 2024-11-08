import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "./LoginPage.css";

export function LoginPage() {
  const [login, setLogin] = useState("");
  const [password, setPassowrd] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    const userData = {
      username: login,
      password: password,
    };

    try {
      const response = await fetch("", {
        method: "POST",
        headers: { "Content-type": "application/json" },
        body: JSON.stringify(userData),
      });

      if (response.ok) {
        navigate("/HomePage");
      } else {
        setError("Niepoprawne dane logowania");
      }
    } catch (error) {
      setError("Błąd połączenia z serwerm");
    }
  };

  return (
    <div id="loginPage">
      <h1>LOGOWANIE</h1>
      <form onSubmit={handleSubmit}>
        <div>
          <input
            type="text"
            placeholder="Login"
            value={login}
            onChange={(e) => setLogin(e.target.value)}
          />
        </div>
        <div>
          <input
            type="password"
            placeholder="Hasło"
            value={password}
            onChange={(e) => setPassowrd(e.target.value)}
          />
        </div>
        {error && <div>{error}</div>}
        <button type="submit">Zaloguj</button>
      </form>
      <p>
        Nie masz konta? <Link to="/RegisterPage">Załóż je tutaj</Link>
      </p>
    </div>
  );
}
