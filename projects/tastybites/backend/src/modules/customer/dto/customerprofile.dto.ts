import { IsBoolean, IsDateString, IsNotEmpty, IsString } from 'class-validator';

export class CustomerProfileDto {
  @IsString()
  @IsNotEmpty()
  first_name: string;

  @IsString()
  @IsNotEmpty()
  surname: string;

  @IsString()
  middle_name: string;

  @IsDateString()
  date_of_birth: string;

  @IsString()
  @IsNotEmpty()
  home_address: string;

  @IsDateString()
  date_of_registration: string;

  @IsBoolean()
  matricNo: boolean;
}