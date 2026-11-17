import { IsBoolean, IsDateString, IsOptional, IsString } from 'class-validator';

export class UpdateCustomerDto {
  @IsString()
  @IsOptional()
  first_name?: string;

  @IsString()
  @IsOptional()
  surname?: string;

  @IsString()
  @IsOptional()
  middle_name?: string;

  @IsDateString()
  @IsOptional()
  date_of_birth?: string;

  @IsString()
  @IsOptional()
  home_address?: string;

  @IsDateString()
  @IsOptional()
  date_of_registration?: string;

  @IsBoolean()
  @IsOptional()
  matriculation_number_flag?: boolean;
}