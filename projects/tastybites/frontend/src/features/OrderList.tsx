import React from "react";
import type { Order } from "../types";
import { deleteOrder, updateOrder } from "../services/orderService";

const OrderList: React.FC<{
  orders: Order[];
  onOrderDeleted: (id: number) => void;
  onOrderUpdated: (order: Order) => void;
}> = ({ orders, onOrderDeleted, onOrderUpdated }) => {
  const handleDelete = async (id: number) => {
    const confirmed = window.confirm("Are you sure you want to delete this order?");
    if (confirmed) {
      const success = await deleteOrder(id);
      if (success) {
        onOrderDeleted(id);
      }
    }
  };

  const handleEdit = async (order: Order) => {
    const updatedItem = prompt("Edit menu item:", order.menuItem);
    if (!updatedItem) return;

    const updatedOrder = await updateOrder(order.id, {
      ...order,
      menuItem: updatedItem,
    });

    if (updatedOrder) {
      onOrderUpdated(updatedOrder);
    }
  };

  return (
    <div className="p-4 bg-orange-100">
      <h3 className="text-lg font-bold text-orange-600">Order History</h3>
      <ul className="space-y-4">
        {orders.map((order) => (
          <li key={order.id} className="bg-white rounded shadow p-4">
            <p><strong>Menu Item:</strong> {order.menuItem}</p>
            <p><strong>Payment Method:</strong> {order.paymentMethod}</p>
            <p><strong>Special Instructions:</strong> {order.specialInstructions || "None"}</p>
            <p><strong>Order Date:</strong> {order.orderDate}</p>
            <p><strong>Next Reservation:</strong> {order.nextReservationDate || "Not set"}</p>

            <div className="mt-2 space-x-2">
              <button
                onClick={() => handleEdit(order)}
                className="px-3 py-1 bg-blue-500 text-white rounded hover:bg-blue-600"
              >
                Edit
              </button>
              <button
                onClick={() => handleDelete(order.id)}
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

export default OrderList;
