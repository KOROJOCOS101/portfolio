// src/types.ts

export type Customer = {
  id: number;
  firstName: string;
  surname: string;
  middleName?: string;
  dateOfBirth: string;
  homeAddress: string;
  dateOfRegistration: string;
  matricNo: boolean;
};

export type Order = {
  id: number;
  orderDate: string;
  menuItem: string;
  specialInstructions?: string;
  paymentMethod: string;
  nextReservationDate?: string;
};