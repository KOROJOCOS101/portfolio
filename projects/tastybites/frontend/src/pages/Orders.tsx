import React, { useState, useEffect } from "react";
import type { Order } from "../types";
import { fetchOrders } from "../services/orderService";
import OrderForm from "../features/OrderForm";
import OrderList from "../features/OrderList";

const Orders: React.FC = () => {
  const [orders, setOrders] = useState<Order[]>([]);

  useEffect(() => {
    fetchOrders().then((data) => setOrders(data));
  }, []);

  const handleOrderPlaced = (newOrder: Order) => {
    setOrders([...orders, newOrder]);
  };

  const handleOrderDeleted = (id: number) => {
    setOrders(orders.filter((o) => o.id !== id));
  };

  const handleOrderUpdated = (updated: Order) => {
    setOrders(orders.map((o) => (o.id === updated.id ? updated : o)));
  };

  return (
    <div className="p-4 bg-orange-100">
      <h2 className="text-xl font-bold text-orange-600">Order History</h2>
      <OrderForm onOrderPlaced={handleOrderPlaced} />
      <OrderList
        orders={orders}
        onOrderDeleted={handleOrderDeleted}
        onOrderUpdated={handleOrderUpdated}
      />
    </div>
  );
};

export default Orders;
