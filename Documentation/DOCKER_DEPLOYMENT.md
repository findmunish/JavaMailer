# Docker Deployment Guide

This guide explains how to run the Java Mailer application using Docker containers.

## Prerequisites

- **Docker** - [Download Docker Desktop](https://www.docker.com/products/docker-desktop/)
- **Docker Compose** - Usually included with Docker Desktop

### Verify Installation

```bash
# Check Docker version
docker --version

# Check Docker Compose version
docker-compose --version
```

## Quick Start

### 1. Clone and Navigate

```bash
git clone <your-repository-url>
cd java_mailer
```

### 2. Configure Environment Variables

Copy the environment template and update with your credentials:

```bash
# Copy the environment template
cp env.example .env
```

Edit `.env` file with your actual credentials:

```bash
# Gmail SMTP Configuration
GMAIL_USERNAME=your-email@gmail.com
GMAIL_APP_PASSWORD=your-gmail-app-password

# SendGrid Configuration
SENDGRID_API_KEY=your-sendgrid-api-key
SENDGRID_FROM_EMAIL=your-verified-sender@example.com

# Amazon SES Configuration
AWS_SES_ACCESS_KEY=your-aws-access-key
AWS_SES_SECRET_KEY=your-aws-secret-key
AWS_SES_REGION=us-east-1
AWS_SES_FROM_EMAIL=your-verified-sender@example.com
```

### 3. Build and Run

```bash
# Build and start the application
docker-compose up --build

# Or run in detached mode
docker-compose up -d --build
```

## Docker Commands

### Build the Image

```bash
# Build the Docker image
docker build -t java-mailer .

# Build with specific tag
docker build -t java-mailer:latest .
```

### Run the Container

```bash
# Run with environment variables
docker run -p 8080:8080 \
  -e GMAIL_USERNAME=your-email@gmail.com \
  -e GMAIL_APP_PASSWORD=your-app-password \
  -e SENDGRID_API_KEY=your-sendgrid-key \
  -e SENDGRID_FROM_EMAIL=your-sender@example.com \
  -e AWS_SES_ACCESS_KEY=your-aws-key \
  -e AWS_SES_SECRET_KEY=your-aws-secret \
  -e AWS_SES_FROM_EMAIL=your-sender@example.com \
  java-mailer

# Run with environment file
docker run -p 8080:8080 --env-file .env java-mailer
```

### Docker Compose Commands

```bash
# Start services
docker-compose up

# Start in detached mode
docker-compose up -d

# Stop services
docker-compose down

# View logs
docker-compose logs -f

# Rebuild and start
docker-compose up --build

# Scale services (if needed)
docker-compose up --scale java-mailer=2
```

## Application Access

Once the container is running:

- **Application URL**: http://localhost:8080
- **Welcome Endpoint**: http://localhost:8080/api/
- **Health Check**: http://localhost:8080/api/

## Testing the Application

### 1. Check Application Status

```bash
# Test welcome endpoint
curl http://localhost:8080/api/

# Check container health
docker ps
```

### 2. Test Email Endpoints

```bash
# Test SMTP email
curl -X POST http://localhost:8080/api/send-email-smtp \
  -H "Content-Type: application/json" \
  -d '{
    "to": "recipient@example.com",
    "subject": "Test Email via SMTP",
    "body": "This is a test email sent via Java Mailer SMTP"
  }'

# Test SendGrid email
curl -X POST http://localhost:8080/api/send-email-sendgrid \
  -H "Content-Type: application/json" \
  -d '{
    "to": "recipient@example.com",
    "subject": "Test Email via SendGrid",
    "body": "This is a test email sent via SendGrid"
  }'

# Test Amazon SES email
curl -X POST http://localhost:8080/api/send-email-ses \
  -H "Content-Type: application/json" \
  -d '{
    "to": "recipient@example.com",
    "subject": "Test Email via Amazon SES",
    "body": "This is a test email sent via Amazon SES"
  }'
```

## Container Management

### View Logs

```bash
# View application logs
docker-compose logs -f java-mailer

# View specific service logs
docker logs java-mailer-app

# Follow logs in real-time
docker logs -f java-mailer-app
```

### Container Shell Access

```bash
# Access container shell
docker exec -it java-mailer-app /bin/bash

# Run commands in container
docker exec java-mailer-app ls -la
```

### Stop and Cleanup

```bash
# Stop the application
docker-compose down

# Stop and remove volumes
docker-compose down -v

# Remove the image
docker rmi java-mailer

# Clean up all containers and images
docker system prune -a
```

## Production Deployment

### 1. Environment Variables

For production, use secure environment variable management:

```bash
# Use Docker secrets (recommended for production)
docker run -p 8080:8080 \
  --secret gmail_password \
  --secret sendgrid_key \
  --secret aws_secret \
  java-mailer
```

### 2. Multi-Stage Build (Optional)

Create a more optimized production image:

```dockerfile
# Multi-stage build
FROM openjdk:17-jdk-slim as builder
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jre-slim
WORKDIR /app
COPY --from=builder /app/target/java-mailer-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```

### 3. Docker Swarm Deployment

```bash
# Initialize swarm
docker swarm init

# Deploy stack
docker stack deploy -c docker-compose.yml java-mailer-stack

# Scale services
docker service scale java-mailer-stack_java-mailer=3
```

## Troubleshooting

### Common Issues

#### 1. Port Already in Use
```bash
# Error: Port 8080 is already in use
# Solution: Use different port
docker run -p 8081:8080 java-mailer
```

#### 2. Environment Variables Not Working
```bash
# Check environment variables in container
docker exec java-mailer-app env | grep SPRING

# Verify .env file is loaded
docker-compose config
```

#### 3. Container Won't Start
```bash
# Check container logs
docker logs java-mailer-app

# Check container status
docker ps -a

# Restart container
docker restart java-mailer-app
```

#### 4. Build Issues
```bash
# Clean build
docker-compose down
docker system prune -f
docker-compose up --build

# Check Dockerfile syntax
docker build --no-cache -t java-mailer .
```

### Debugging Commands

```bash
# Inspect container
docker inspect java-mailer-app

# Check container resources
docker stats java-mailer-app

# View container processes
docker exec java-mailer-app ps aux

# Check network connectivity
docker exec java-mailer-app curl -f http://localhost:8080/api/
```

## Security Best Practices

1. **Never commit `.env` files** with real credentials
2. **Use Docker secrets** for production deployments
3. **Run containers as non-root user** (already configured)
4. **Use specific image tags** instead of `latest`
5. **Regularly update base images** for security patches
6. **Scan images for vulnerabilities** using tools like Trivy

## Performance Optimization

### 1. Multi-Stage Builds
- Reduces final image size
- Excludes build dependencies from runtime

### 2. Layer Caching
- Copy `pom.xml` before source code
- Use `.dockerignore` to exclude unnecessary files

### 3. Resource Limits
```yaml
# In docker-compose.yml
services:
  java-mailer:
    deploy:
      resources:
        limits:
          memory: 512M
          cpus: '0.5'
```

## Monitoring and Logging

### 1. Health Checks
- Built-in health check endpoint
- Docker health check configuration
- Container restart policies

### 2. Log Management
```bash
# Configure log rotation
docker run --log-opt max-size=10m --log-opt max-file=3 java-mailer
```

### 3. Monitoring
```bash
# Monitor container resources
docker stats

# Monitor logs
docker-compose logs -f --tail=100
```

This Docker setup provides a complete containerized solution for your Java Mailer application with all three email services! 🐳
