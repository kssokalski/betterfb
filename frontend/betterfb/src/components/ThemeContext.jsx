import React, { createContext, useContext, useState } from "react";

// Create a context for theme management.
const ThemeContext = createContext();

/**
 * A provider component that manages the theme state (light/dark mode)
 * and provides the toggle function to its children components.
 *
 * @param {React.ReactNode} children - The child components that will have access to the theme context.
 * @returns {JSX.Element} A ThemeProvider wrapping its children.
 */
export const ThemeProvider = ({ children }) => {
  // State to track whether dark mode is enabled.
  const [darkMode, setDarkMode] = useState(false);

  /**
   * Toggles the theme between light and dark mode.
   */
  const toggleTheme = () => {
    setDarkMode(!darkMode);
  };

  return (
    // Provide the theme state and toggle function to all children components.
    <ThemeContext.Provider value={{ darkMode, toggleTheme }}>
      {children}
    </ThemeContext.Provider>
  );
};

/**
 * A custom hook to access the theme context.
 *
 * This hook provides the current theme state (`darkMode`) and the function to toggle the theme (`toggleTheme`).
 * It ensures that the hook is used within a valid `ThemeProvider`.
 *
 * @throws {Error} If used outside of a `ThemeProvider`.
 * @returns {object} An object containing `darkMode` and `toggleTheme`.
 */
export const useTheme = () => {
  const context = useContext(ThemeContext);

  // Ensure the hook is used within a ThemeProvider.
  if (!context) {
    throw new Error("useTheme must be used within a ThemeProvider");
  }

  return context;
};
