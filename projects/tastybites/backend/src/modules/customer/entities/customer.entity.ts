import { Entity, PrimaryGeneratedColumn, Column, OneToMany } from 'typeorm';
import { OrderHistory } from '../../order/entities/order-history.entity';

@Entity()
export class Customer {
  @PrimaryGeneratedColumn()
  id: number;

  @Column()
  firstName: string;

  @Column()
  surname: string;

  @Column({ nullable: true })
  middleName: string;

  @Column()
  dateOfBirth: Date;

  @Column()
  homeAddress: string;

  @Column()
  dateOfRegistration: Date;

  @Column({ type: 'boolean' })
  matricNo: boolean;

  @OneToMany(() => OrderHistory, (order) => order.customer)
  orders: OrderHistory[];
}