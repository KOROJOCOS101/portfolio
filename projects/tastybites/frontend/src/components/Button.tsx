import React from "react";
import theme from "../styles/theme"; // Import theme for consistent colors

interface ButtonProps {
  label: string;
  onClick: () => void;
}

const Button: React.FC<ButtonProps> = ({ label, onClick }) => {
  return (
    <button
      style={{
        backgroundColor: theme.colors.primary,
        color: theme.colors.text,
        padding: "8px 12px",
        border: "none",
        cursor: "pointer",
        transition: "background-color 0.3s",
      }}
      onClick={onClick}
      onMouseOver={(e) => (e.currentTarget.style.backgroundColor = theme.colors.secondary)}
      onMouseOut={(e) => (e.currentTarget.style.backgroundColor = theme.colors.primary)}
    >
      {label}
    </button>
  );
};

export default Button;