# Java Mailer - Build and Run Guide
-----------------------------------

This document provides step-by-step instructions to build and run the Java Mailer Spring Boot application.

## Prerequisites

Before building and running the application, ensure you have the following installed:

- **Java 17 or higher** - [Download from Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)
- **Maven 3.6+** - [Download from Apache Maven](https://maven.apache.org/download.cgi)
- **Git** (optional) - [Download from Git](https://git-scm.com/downloads)

### Verify Installation

```bash
# Check Java version
java -version

# Check Maven version
mvn -version

# Check Git version (optional)
git --version
```

## Project Setup

### 1. Clone the Repository

```bash
git clone <your-repository-url>
cd java_mailer
```

### 2. Configure Email Services

Copy the template configuration file and update with your credentials:

```bash
# Copy the template file
cp src/main/resources/application.properties.template src/main/resources/application.properties
```

Edit `src/main/resources/application.properties` with your actual credentials:

```properties
# Gmail SMTP Configuration (for /api/send-email-smtp)
spring.mail.username=your-email@gmail.com
spring.mail.password=your-gmail-app-password

# SendGrid Configuration (for /api/send-email-sendgrid)
sendgrid.api.key=your-sendgrid-api-key
sendgrid.from.email=your-verified-sender@example.com

# Amazon SES Configuration (for /api/send-email-ses)
aws.ses.access-key=your-aws-access-key
aws.ses.secret-key=your-aws-secret-key
aws.ses.region=us-east-1
aws.ses.from.email=your-verified-sender@example.com
```

## Build Instructions

### 1. Clean and Compile

```bash
# Clean previous builds
mvn clean

# Compile the project
mvn compile
```

### 2. Run Tests (Optional)

```bash
# Run all tests
mvn test

# Run tests with coverage report
mvn test jacoco:report
```

### 3. Package the Application

```bash
# Create executable JAR file
mvn package

# The JAR file will be created in target/ directory
# File: target/java-mailer-0.0.1-SNAPSHOT.jar
```

## Run Instructions

### Option 1: Using Docker (Recommended)

```bash
# Copy environment template
cp env.example .env

# Edit .env with your credentials
# Then run with Docker Compose
docker-compose up --build

# Or run in detached mode
docker-compose up -d --build
```

### Option 2: Using Maven (Development)

```bash
# Run the application using Maven
mvn spring-boot:run
```

### Option 3: Using JAR File (Production)

```bash
# Run the packaged JAR file
java -jar target/java-mailer-0.0.1-SNAPSHOT.jar
```

### Option 4: Using Maven with Profile

```bash
# Run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Run with custom port
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

## Application Access

Once the application is running, you can access:

- **Application URL**: http://localhost:8080
- **Welcome Endpoint**: http://localhost:8080/api/
- **API Documentation**: Available in README.md

## API Endpoints

### 1. Welcome Endpoint (Public)
```bash
curl http://localhost:8080/api/
```

### 2. Send Email via SMTP (Gmail) - Requires Authentication
```bash
curl -u admin:password -X POST http://localhost:8080/api/send-email-smtp \
  -H "Content-Type: application/json" \
  -d '{
    "to": "recipient@example.com",
    "subject": "Test Email via SMTP",
    "body": "This is a test email sent via Java Mailer SMTP"
  }'
```

### 3. Send Email via SendGrid - Requires Authentication
```bash
curl -u admin:password -X POST http://localhost:8080/api/send-email-sendgrid \
  -H "Content-Type: application/json" \
  -d '{
    "to": "recipient@example.com",
    "subject": "Test Email via SendGrid",
    "body": "This is a test email sent via SendGrid"
  }'
```

### 4. Send Email via Amazon SES - Requires Authentication
```bash
curl -u admin:password -X POST http://localhost:8080/api/send-email-ses \
  -H "Content-Type: application/json" \
  -d '{
    "to": "recipient@example.com",
    "subject": "Test Email via Amazon SES",
    "body": "This is a test email sent via Amazon SES"
  }'
```

## Security Features

### Authentication
- **Default Credentials**: `admin/change-this-password`
- **Change Password**: Update in `application.properties`
- **Email Endpoints**: Require authentication
- **Welcome Endpoint**: Public access

### Rate Limiting
- **Limit**: 10 requests per minute per IP
- **Protection**: Prevents spam and abuse
- **Response**: 429 status when exceeded

### Input Validation
- **Email Format**: Validated email addresses
- **Length Limits**: Subject (200 chars), Body (10,000 chars)
- **Required Fields**: All fields mandatory

## Troubleshooting

### Common Issues

#### 1. Port Already in Use
```bash
# Error: Port 8080 is already in use
# Solution: Use a different port
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

#### 2. Java Version Issues
```bash
# Error: Unsupported class file major version
# Solution: Ensure you're using Java 17+
java -version
```

#### 3. Maven Dependencies Issues
```bash
# Clean and re-download dependencies
mvn clean install -U
```

#### 4. Email Service Configuration Issues

**Gmail SMTP Issues:**
- Ensure 2FA is enabled on Gmail
- Use App Password, not regular password
- Check Gmail SMTP settings

**SendGrid Issues:**
- Verify API key has "Mail Send" permissions
- Ensure sender email is verified in SendGrid

**Amazon SES Issues:**
- Verify sender email/domain in SES console
- Check IAM user has `AmazonSESFullAccess` policy
- Ensure AWS credentials are correct

### Logs and Debugging

```bash
# Run with debug logging
mvn spring-boot:run -Dspring-boot.run.arguments=--logging.level.com.example.javamailer=DEBUG

# Check application logs
tail -f logs/application.log
```

## Development Tips

### 1. Hot Reload (Development)
```bash
# Run with Spring Boot DevTools for hot reload
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.devtools.restart.enabled=true"
```

### 2. Environment-Specific Configuration
```bash
# Run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### 3. Custom Configuration
```bash
# Override configuration at runtime
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081 --logging.level.root=INFO"
```

## Production Deployment

### 1. Docker Deployment (Recommended)

```bash
# Build and run with Docker Compose
docker-compose up --build -d

# Check container status
docker-compose ps

# View logs
docker-compose logs -f
```

### 2. Build Production JAR
```bash
mvn clean package -Pprod
```

### 3. Run with Production Profile
```bash
java -jar target/java-mailer-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### 4. Docker Individual Commands
```bash
# Build Docker image
docker build -t java-mailer .

# Run Docker container with environment variables
docker run -p 8080:8080 --env-file .env java-mailer
```

## Security Notes

- Never commit `application.properties` with real credentials
- Use environment variables for production
- Keep API keys and passwords secure
- Regularly rotate access credentials
- Use HTTPS in production environments

## Support

For issues and questions:
1. Check the troubleshooting section above
2. Review the main README.md file
3. Check application logs for error details
4. Verify email service configurations


