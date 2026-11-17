import { Controller, Get, Post, Put, Delete, Param, Body } from '@nestjs/common';
import { CustomerService } from './customer.service';
import { Customer } from './entities/customer.entity';
import { CustomerProfileDto } from './dto/customerprofile.dto';
import { UpdateCustomerDto } from './dto/updatecustomer.dto';

@Controller('customers')
export class CustomerController {
  constructor(private readonly customerService: CustomerService) {}

  // Create a new customer (Uses DTO for validation)
  @Post()
  async createCustomer(@Body() customerData: CustomerProfileDto): Promise<Customer> {
    return this.customerService.createCustomer(customerData);
  }

  // Get all customers
  @Get()
  async getAllCustomers(): Promise<Customer[]> {
    return this.customerService.getAllCustomers();
  }

  // Get a single customer by ID
  @Get(':id')
  async getCustomerById(@Param('id') id: number): Promise<Customer | null> {
    return this.customerService.getCustomerById(id);
  }

  // Update a customer (Uses DTO for validation)
  @Put(':id')
  async updateCustomer(
    @Param('id') id: number,
    @Body() updateData: UpdateCustomerDto,
  ): Promise<Customer | null> {
    return this.customerService.updateCustomer(id, updateData);
  }

  // Delete a customer
  @Delete(':id')
  async deleteCustomer(@Param('id') id: number): Promise<boolean> {
    return this.customerService.deleteCustomer(id);
  }
}