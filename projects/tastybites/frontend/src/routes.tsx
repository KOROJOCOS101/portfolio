import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Customers from "./pages/Customers";
import Orders from "./pages/Orders";

const AppRoutes = () => {
  return (
    <Router>
      <Routes>
        <Route path="/customers" element={<Customers />} />
        <Route path="/orders" element={<Orders />} />
      </Routes>
    </Router>
  );
};

export default AppRoutes;