import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { OrderHistory } from './entities/order-history.entity';
import { OrderHistoryDto } from './dto/orderhistory.dto';

@Injectable()
export class OrderService {
  constructor(
    @InjectRepository(OrderHistory)
    private orderRepository: Repository<OrderHistory>,
  ) {}

  // Create a new order (Ensures correct DTO-to-entity mapping)
  async createOrder(orderData: OrderHistoryDto): Promise<OrderHistory> {
    const newOrder = this.orderRepository.create({
      orderDate: orderData.orderDate, 
      menuItem: orderData.menuItem, 
      specialInstructions: orderData.specialInstructions, 
      paymentMethod: orderData.paymentMethod, 
      nextReservationDate: orderData.nextReservationDate, 
    });

    return this.orderRepository.save(newOrder);
  }

  // Get all orders
  async getAllOrders(): Promise<OrderHistory[]> {
    return this.orderRepository.find({ relations: ['customer'] });
  }

  // Get order by ID
  async getOrderById(id: number): Promise<OrderHistory | null> {
    return this.orderRepository.findOne({
      where: { id },
      relations: ['customer'],
    });
  }

  // Get all orders for a specific customer
  async getOrdersByCustomer(customerId: number): Promise<OrderHistory[]> {
    return this.orderRepository.find({
      where: { customer: { id: customerId } },
      relations: ['customer'],
    });
  }

  // Update an order (Explicitly handles DTO mapping)
  async updateOrder(id: number, updateData: Partial<OrderHistoryDto>): Promise<OrderHistory | null> {
    await this.orderRepository.update(id, updateData);
    return this.getOrderById(id);
  }

  // Delete an order
  async deleteOrder(id: number): Promise<boolean> {
    const result = await this.orderRepository.delete(id);
    return result.affected ? result.affected > 0 : false;
  }
}