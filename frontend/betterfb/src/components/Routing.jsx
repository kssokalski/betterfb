import { HashRouter as Router, Routes, Route } from "react-router-dom";
import { LoginPage } from "../pages/LoginPage";
import { HomePage } from "../pages/HomePage";
import { RegisterPage } from "../pages/RegisterPage";
import { ResetPasswordRequest } from "../pages/ResetPasswordRequest";
import { ResetPassword } from "../pages/ResetPassword";
import { ThemeProvider } from "./ThemeContext";

/**
 * A functional component responsible for routing in the application.
 *
 * This component defines the navigation structure of the application by setting up routes
 * for the login page, home page, and registration page. It uses `react-router-dom` for managing
 * client-side routing and wraps the application in a `ThemeProvider` to provide theme context globally.
 *
 * The structure includes:
 * - `/` - The login page.
 * - `/HomePage` - The main/home page after login.
 * - `/RegisterPage` - The registration page for new users.
 *
 * Wrapping the application in a `ThemeProvider` ensures that all child components
 * have access to theme-related state and functions (e.g., toggling between dark and light mode).
 *
 * @returns {JSX.Element} The routing component that handles navigation between pages.
 */
export function Routing() {
  return (
    <ThemeProvider>
      <Router>
        <Routes>
          {/* Route for the login page */}
          <Route path="/" element={<LoginPage />} />

          {/* Route for the home page */}
          <Route path="/HomePage" element={<HomePage />} />

          {/* Route for the registration page */}
          <Route path="/RegisterPage" element={<RegisterPage />} />

          {/* Routes for the password reset pages */}
          <Route
            path="/ResetPasswordRequest"
            element={<ResetPasswordRequest />}
          />
          <Route path="/ResetPassword" element={<ResetPassword />} />
        </Routes>
      </Router>
    </ThemeProvider>
  );
}
