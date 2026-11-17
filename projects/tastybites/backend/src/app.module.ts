import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { databaseConfig } from './config/database.config';
import { CustomerModule } from './modules/customer/customer.module';
import { OrderModule } from './modules/order/order.module';

@Module({
  imports: [
    TypeOrmModule.forRoot(databaseConfig),
    CustomerModule,
    OrderModule,  // Load PostgreSQL settings
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}