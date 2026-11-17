# Use the official Nginx image
FROM nginx:latest

# Copy custom Nginx configuration
COPY nginx.conf /etc/nginx/nginx.conf

# Copy SSL certificates for HTTPS support
COPY certificates/ /etc/nginx/certificates/

# Expose ports for HTTP & HTTPS
EXPOSE 80 443

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]