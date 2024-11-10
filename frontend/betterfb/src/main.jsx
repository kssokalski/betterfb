import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import App from "./App.jsx";

/**
 * The entry point for the React application.
 *
 * This file renders the main `App` component inside a `StrictMode` wrapper,
 * which helps identify potential problems in the app during development.
 *
 * `createRoot` is used to create a root React element that mounts the app to the DOM.
 *
 * @returns {void} No return value as it directly renders the app to the DOM.
 */
createRoot(document.getElementById("root")).render(
  <StrictMode>
    <App />
  </StrictMode>
);
