import { Routing } from "./components/Routing";
import "./App.css";

/**
 * A functional component representing the main application.
 *
 * This component serves as the root of the React application. It primarily renders
 * the `Routing` component, which manages the navigation and rendering of different
 * pages based on the application's routing configuration.
 *
 * @returns {JSX.Element} The main application component that renders the routing logic.
 */
function App() {
  return <Routing />;
}

export default App;
