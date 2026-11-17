// src/pages/Customers.tsx
import React, { useState, useEffect } from "react";
import type { Customer } from "../types";
import {
  fetchCustomers,
  addCustomer,
  updateCustomer,
  deleteCustomer,
} from "../services/customerService";
import CustomerForm from "../features/CustomerForm";
import CustomerList from "../features/CustomerList";

const Customers: React.FC = () => {
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [editingCustomer, setEditingCustomer] = useState<Customer | null>(null);

  useEffect(() => {
    fetchCustomers().then(setCustomers);
  }, []);

  const handleCustomerAdded = async (customerData: Partial<Customer>) => {
    if (editingCustomer) {
      const updated = await updateCustomer(editingCustomer.id, customerData);
      if (updated) {
        setCustomers((prev) =>
          prev.map((c) => (c.id === updated.id ? updated : c))
        );
        setEditingCustomer(null);
      }
    } else {
      const newCustomer = await addCustomer(customerData);
      if (newCustomer) {
        setCustomers((prev) => [...prev, newCustomer]);
      }
    }
  };

  const handleDelete = async (id: number) => {
    const success = await deleteCustomer(id);
    if (success) {
      setCustomers((prev) => prev.filter((c) => c.id !== id));
    }
  };

  const handleEdit = (customer: Customer) => {
    setEditingCustomer(customer);
  };

  return (
    <div className="p-4 bg-orange-100">
      <h2 className="text-xl font-bold text-orange-600">Customers</h2>
      <CustomerForm
        onCustomerAdded={handleCustomerAdded}
        editingCustomer={editingCustomer}
      />
      <CustomerList
        customers={customers}
        onEdit={handleEdit}
        onDelete={handleDelete}
      />
    </div>
  );
};

export default Customers;
