// src/services/orderService.ts
import type { Order } from "../types";

const API_URL = "/api/orders";

export const fetchOrders = async (): Promise<Order[]> => {
  try {
    const response = await fetch(API_URL);
    if (!response.ok) throw new Error(`Error fetching orders: ${response.status}`);
    return await response.json();
  } catch (error) {
    console.error("Fetch Orders Error:", error);
    return [];
  }
};

export const placeOrder = async (order: Partial<Order>): Promise<Order | null> => {
  try {
    const response = await fetch(API_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(order),
    });
    if (!response.ok) throw new Error(`Error placing order: ${response.status}`);
    return await response.json();
  } catch (error) {
    console.error("Place Order Error:", error);
    return null;
  }
};

export const updateOrder = async (id: number, updatedData: Partial<Order>): Promise<Order | null> => {
  try {
    const response = await fetch(`${API_URL}/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(updatedData),
    });
    if (!response.ok) throw new Error(`Error updating order: ${response.status}`);
    return await response.json();
  } catch (error) {
    console.error("Update Order Error:", error);
    return null;
  }
};

export const deleteOrder = async (id: number): Promise<boolean> => {
  try {
    const response = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
    return response.ok;
  } catch (error) {
    console.error("Delete Order Error:", error);
    return false;
  }
};
