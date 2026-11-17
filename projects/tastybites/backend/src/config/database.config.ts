import { TypeOrmModuleOptions } from '@nestjs/typeorm';

export const databaseConfig: TypeOrmModuleOptions = {
  type: 'postgres',
  host: process.env.DB_HOST || 'localhost',
  port: Number(process.env.DB_PORT) || 5432,
  username: process.env.DB_USER || 'nestuser',
  password: process.env.DB_PASS || 'nestpassword',
  database: process.env.DB_NAME || 'tastybites',
  autoLoadEntities: true,  // Automatically load entities from modules
  synchronize: true, // Change to false in production to prevent data loss
};