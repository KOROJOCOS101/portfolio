import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { OrderHistory } from './entities/order-history.entity';
import { OrderService } from './order.service';
import { OrderController } from './order.controller';

@Module({
  imports: [TypeOrmModule.forFeature([OrderHistory])],
  providers: [OrderService],
  controllers: [OrderController],
})
export class OrderModule {}