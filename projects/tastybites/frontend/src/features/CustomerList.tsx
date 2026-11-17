// src/features/CustomerList.tsx
import React from "react";
import type { Customer } from "../types";

interface Props {
  customers: Customer[];
  onEdit: (customer: Customer) => void;
  onDelete: (id: number) => void;
}

const CustomerList: React.FC<Props> = ({ customers, onEdit, onDelete }) => {
  return (
    <div className="p-4 bg-orange-100">
      <h3 className="text-lg font-bold text-orange-600">Customer List</h3>
      <ul className="space-y-4">
        {customers.map((customer) => (
          <li key={customer.id} className="bg-white rounded shadow p-4">
            <p><strong>Full Name:</strong> {customer.firstName} {customer.middleName} {customer.surname}</p>
            <p><strong>Date of Birth:</strong> {customer.dateOfBirth}</p>
            <p><strong>Home Address:</strong> {customer.homeAddress}</p>
            <p><strong>Date of Registration:</strong> {customer.dateOfRegistration}</p>
            <p><strong>Matric No:</strong> {customer.matricNo ? "Yes" : "No"}</p>

            <div className="mt-2 space-x-2">
              <button
                onClick={() => onEdit(customer)}
                className="px-3 py-1 bg-blue-500 text-white rounded hover:bg-blue-600"
              >
                Edit
              </button>
              <button
                onClick={() => {
                const confirmed = window.confirm("Are you sure you want to delete this customer?");
                if (confirmed) onDelete(customer.id);
                }}
                className="px-3 py-1 bg-red-500 text-white rounded hover:bg-red-600"
              >
                Delete
              </button>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default CustomerList;
