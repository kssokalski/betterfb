import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { LoginPage } from "../pages/LoginPage";
import { HomePage } from "../pages/HomePage";
import { RegisterPage } from "../pages/RegisterPage";

/**
 * A functional component responsible for routing in the application.
 *
 * This component defines the routes for the login page, home page, and registration page.
 * It uses the `BrowserRouter` to handle routing and wraps the routes inside a `Routes` component.
 * Each route is mapped to a specific component which is rendered when the corresponding path is accessed.
 *
 * @returns {JSX.Element} The routing component that handles navigation between pages.
 */
export function Routing() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route path="/HomePage" element={<HomePage />} />
        <Route path="/RegisterPage" element={<RegisterPage />} />
      </Routes>
    </Router>
  );
}
