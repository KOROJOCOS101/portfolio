import React from "react";
import theme from "../styles/theme"; // Import theme for styling

interface InputFieldProps {
  type: string;
  placeholder: string;
  value: string;
  onChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
}

const Input: React.FC<InputFieldProps> = ({ type, placeholder, value, onChange }) => {
  return (
    <input
      type={type}
      placeholder={placeholder}
      value={value}
      onChange={onChange}
      style={{
        padding: "8px",
        border: `1px solid ${theme.colors.primary}`,
        backgroundColor: theme.colors.backgroundLight,
        color: theme.colors.secondary,
        marginBottom: "8px",
        width: "100%",
      }}
    />
  );
};

export default Input;