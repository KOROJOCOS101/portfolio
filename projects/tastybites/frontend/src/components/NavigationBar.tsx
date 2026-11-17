import React from "react";
import { Link } from "react-router-dom";
import theme from "../styles/theme"; // Import theme for styling

const NavigationBar: React.FC = () => {
  return (
    <nav style={{ backgroundColor: theme.colors.primary, padding: "10px" }}>
      <Link to="/customers" style={{ color: theme.colors.text, marginRight: "15px" }}>Customers</Link>
      <Link to="/orders" style={{ color: theme.colors.text }}>Orders</Link>
    </nav>
  );
};

export default NavigationBar;