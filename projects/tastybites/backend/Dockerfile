# Use the official Node.js image
FROM node:18

# Set working directory
WORKDIR /app

# Copy package.json and install dependencies
COPY package*.json ./
RUN npm install

# Copy the rest of the app's code
COPY . .

# Build the TypeScript code
RUN npm run build

# Expose the port NestJS runs on
EXPOSE 3000

# Run the app
CMD ["npm", "run", "start:prod"]
