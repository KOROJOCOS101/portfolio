// src/services/customerService.ts
import type { Customer } from "../types";

const API_URL = "/api/customers";

// GET all customers
export const fetchCustomers = async (): Promise<Customer[]> => {
  try {
    const response = await fetch(API_URL);
    if (!response.ok) throw new Error(`Error fetching customers: ${response.status}`);
    return await response.json();
  } catch (error) {
    console.error("Fetch Customers Error:", error);
    return [];
  }
};

// POST new customer
export const addCustomer = async (customer: Partial<Customer>): Promise<Customer | null> => {
  try {
    const response = await fetch(API_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(customer),
    });
    if (!response.ok) throw new Error(`Error adding customer: ${response.status}`);
    return await response.json();
  } catch (error) {
    console.error("Add Customer Error:", error);
    return null;
  }
};

// PUT update existing customer
export const updateCustomer = async (id: number, updatedData: Partial<Customer>): Promise<Customer | null> => {
  try {
    const response = await fetch(`${API_URL}/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(updatedData),
    });
    if (!response.ok) throw new Error(`Error updating customer: ${response.status}`);
    return await response.json();
  } catch (error) {
    console.error("Update Customer Error:", error);
    return null;
  }
};

// DELETE customer by ID
export const deleteCustomer = async (id: number): Promise<boolean> => {
  try {
    const response = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
    return response.ok;
  } catch (error) {
    console.error("Delete Customer Error:", error);
    return false;
  }
};
