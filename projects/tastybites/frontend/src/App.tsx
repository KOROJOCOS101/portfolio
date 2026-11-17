import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Customers from "./pages/Customers";
import Orders from "./pages/Orders";
import Navbar from "./components/NavigationBar"; // Import Navbar component
import "./App.css";

function App() {
  return (
    <Router>
      <div>
        <Navbar /> {/* Using the Navbar component instead of hardcoded links */}

        <Routes>
          <Route path="/customers" element={<Customers />} />
          <Route path="/orders" element={<Orders />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;