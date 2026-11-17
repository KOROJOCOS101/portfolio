import { Entity, PrimaryGeneratedColumn, Column, ManyToOne } from 'typeorm';
import { Customer } from '../../customer/entities/customer.entity';

@Entity()
export class OrderHistory {
  @PrimaryGeneratedColumn()
  id: number;

  @ManyToOne(() => Customer, (customer) => customer.orders)
  customer: Customer;

  @Column()
  orderDate: Date;

  @Column()
  menuItem: string;

  @Column({ nullable: true })
  specialInstructions: string;

  @Column()
  paymentMethod: string;

  @Column({ nullable: true })
  nextReservationDate: Date;
}