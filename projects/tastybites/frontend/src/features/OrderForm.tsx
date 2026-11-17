import React, { useEffect, useState } from "react";
import type { Order } from "../types";
import { placeOrder, updateOrder } from "../services/orderService";

interface Props {
  onOrderPlaced: (order: Order) => void;
  editingOrder?: Order | null;
  clearEditing?: () => void;
}

const OrderForm: React.FC<Props> = ({ onOrderPlaced, editingOrder, clearEditing }) => {
  const [formData, setFormData] = useState<Partial<Order>>({});

  // Load order into form when editing
  useEffect(() => {
    if (editingOrder) {
      setFormData({
        id: editingOrder.id,
        menuItem: editingOrder.menuItem,
        paymentMethod: editingOrder.paymentMethod,
        orderDate: editingOrder.orderDate,
        specialInstructions: editingOrder.specialInstructions,
        nextReservationDate: editingOrder.nextReservationDate,
      });
    } else {
      setFormData({});
    }
  }, [editingOrder]);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async () => {
    if (formData.menuItem && formData.paymentMethod && formData.orderDate) {
      let result: Order | null = null;

      if (editingOrder && formData.id) {
        result = await updateOrder(formData.id, formData);
      } else {
        result = await placeOrder(formData);
      }

      if (result) {
        onOrderPlaced(result);
        setFormData({});
        clearEditing?.();
      }
    } else {
      console.warn("Required fields missing.");
    }
  };

  return (
    <div className="p-4 bg-orange-100">
      <h3 className="text-lg font-bold text-orange-600">
        {editingOrder ? "Edit Order" : "Place Order"}
      </h3>

      <input
        type="text"
        name="menuItem"
        placeholder="Menu Item"
        value={formData.menuItem || ""}
        onChange={handleInputChange}
      />
      <input
        type="text"
        name="paymentMethod"
        placeholder="Payment Method"
        value={formData.paymentMethod || ""}
        onChange={handleInputChange}
      />

      <textarea
        name="specialInstructions"
        placeholder="Special Instructions"
        value={formData.specialInstructions || ""}
        onChange={handleInputChange}
        style={{ height: "20px", verticalAlign: "middle" }}
      ></textarea>

      <div className="mt-2">
        <label htmlFor="orderDate" className="block font-semibold text-gray-700">
          Order Date
        </label>
        <input
          type="date"
          name="orderDate"
          id="orderDate"
          value={formData.orderDate || ""}
          onChange={handleInputChange}
        />
      </div>

      <div className="mt-2">
        <label htmlFor="nextReservationDate" className="block font-semibold text-gray-700">
          Next Reservation
        </label>
        <input
          type="date"
          name="nextReservationDate"
          id="nextReservationDate"
          value={formData.nextReservationDate || ""}
          onChange={handleInputChange}
        />
      </div>

      <button onClick={handleSubmit} className="mt-2 bg-orange-600 text-white px-3 py-1 rounded">
        {editingOrder ? "Update" : "Submit"}
      </button>

      {editingOrder && (
        <button
          onClick={() => {
            clearEditing?.();
            setFormData({});
          }}
          className="mt-2 ml-2 bg-gray-400 text-white px-3 py-1 rounded"
        >
          Cancel
        </button>
      )}
    </div>
  );
};

export default OrderForm;
