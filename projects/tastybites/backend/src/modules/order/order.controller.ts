import { Controller, Get, Post, Put, Delete, Param, Body } from '@nestjs/common';
import { OrderService } from './order.service';
import { OrderHistory } from './entities/order-history.entity';
import { OrderHistoryDto } from './dto/orderhistory.dto';

@Controller('orders')
export class OrderController {
  constructor(private readonly orderService: OrderService) {}

  /** Create a new order (validated via DTO) */
  @Post()
  async createOrder(@Body() orderData: OrderHistoryDto): Promise<OrderHistory> {
    return this.orderService.createOrder(orderData);
  }

  /** Get all orders */
  @Get()
  async getAllOrders(): Promise<OrderHistory[]> {
    return this.orderService.getAllOrders();
  }

  /** Get a specific order by its ID */
  @Get(':id')
  async getOrderById(@Param('id') id: number): Promise<OrderHistory | null> {
    return this.orderService.getOrderById(id);
  }

  /** Get all orders for a specific customer */
  @Get('customer/:customerId')
  async getOrdersByCustomer(@Param('customerId') customerId: number): Promise<OrderHistory[]> {
    return this.orderService.getOrdersByCustomer(customerId);
  }

  /** Update a specific order (validated via DTO) */
  @Put(':id')
  async updateOrder(
    @Param('id') id: number,
    @Body() updateData: OrderHistoryDto,
  ): Promise<OrderHistory | null> {
    return this.orderService.updateOrder(id, updateData);
  }

  /** Delete an order */
  @Delete(':id')
  async deleteOrder(@Param('id') id: number): Promise<boolean> {
    return this.orderService.deleteOrder(id);
  }
}
