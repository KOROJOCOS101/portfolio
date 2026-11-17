import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Customer } from './entities/customer.entity';
import { CustomerProfileDto } from './dto/customerprofile.dto';
import { UpdateCustomerDto } from './dto/updatecustomer.dto';

@Injectable()
export class CustomerService {
  constructor(
    @InjectRepository(Customer)
    private customerRepository: Repository<Customer>,
  ) {}

  // Create a new customer (Uses DTO for validation)
  async createCustomer(customerData: CustomerProfileDto): Promise<Customer> {
    const newCustomer = this.customerRepository.create(customerData);
    return this.customerRepository.save(newCustomer);
  }

  // Get all customers
  async getAllCustomers(): Promise<Customer[]> {
    return this.customerRepository.find();
  }

  // Get a customer by ID
  async getCustomerById(id: number): Promise<Customer | null> {
    return this.customerRepository.findOneBy({ id });
  }

  // Update a customer (Uses DTO for structured updates)
  async updateCustomer(id: number, updateData: UpdateCustomerDto): Promise<Customer | null> {
    await this.customerRepository.update(id, updateData);
    return this.getCustomerById(id);
  }

  // Delete a customer
  async deleteCustomer(id: number): Promise<boolean> {
    const result = await this.customerRepository.delete(id);
    return result.affected ? result.affected > 0 : false;
  }
}