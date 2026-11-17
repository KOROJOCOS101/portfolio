import { IsDateString, IsNotEmpty, IsOptional, IsString } from 'class-validator';

export class OrderHistoryDto {
  @IsDateString()
  orderDate: Date;

  @IsString()
  @IsNotEmpty()
  menuItem: string;

  @IsString()
  @IsOptional()
  specialInstructions?: string;

  @IsString()
  @IsNotEmpty()
  paymentMethod: string;

  @IsDateString()
  @IsOptional()
  nextReservationDate?: Date;
}