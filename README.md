# Java Mailer Spring Boot Application

A secure Spring Boot application with triple email functionality using Gmail SMTP (Java Mailer), SendGrid, and Amazon SES.

## Features

- **Welcome endpoint** at root (`/api/`) - Public access
- **Send email via SMTP** at `/api/send-email-smtp` - Requires authentication
- **Send email via SendGrid** at `/api/send-email-sendgrid` - Requires authentication  
- **Send email via Amazon SES** at `/api/send-email-ses` - Requires authentication
- **Security features**: Authentication, rate limiting, input validation
- **Docker support**: Complete containerization with Docker Compose
- **Triple email service integration**

## Setup Instructions

### 1. Gmail Configuration (for SMTP)

1. Enable 2-Factor Authentication on your Gmail account
2. Generate an App Password:
   - Go to Google Account settings
   - Security → 2-Step Verification → App passwords
   - Generate a new app password for "Mail"
   - Use this password in `application.properties`

### 2. SendGrid Configuration

1. Create a SendGrid account at [sendgrid.com](https://sendgrid.com)
2. Generate an API Key:
   - Go to Settings → API Keys
   - Create a new API key with "Mail Send" permissions
3. Verify a sender identity:
   - Go to Settings → Sender Authentication
   - Verify a single sender or domain

### 3. Amazon SES Configuration

1. Create an AWS account at [aws.amazon.com](https://aws.amazon.com)
2. Enable Amazon SES:
   - Go to Amazon SES console
   - Verify your email address or domain
3. Create IAM credentials:
   - Go to IAM → Users → Create user
   - Attach policy: `AmazonSESFullAccess`
   - Create access key and secret key

### 4. Configuration

Update `src/main/resources/application.properties`:

```properties
# Gmail SMTP Configuration
spring.mail.username=your-actual-email@gmail.com
spring.mail.password=your-16-character-app-password

# SendGrid Configuration
sendgrid.api.key=your-sendgrid-api-key
sendgrid.from.email=your-verified-sender@example.com

# Amazon SES Configuration
aws.ses.access-key=your-aws-access-key
aws.ses.secret-key=your-aws-secret-key
aws.ses.region=us-east-1
aws.ses.from.email=your-verified-sender@example.com
```

### 5. Running the Application

#### Option A: Using Docker (Recommended)

```bash
# Copy environment template
cp env.example .env

# Edit .env with your credentials
# Then run with Docker Compose
docker-compose up --build

# Or run in detached mode
docker-compose up -d --build
```

#### Option B: Using Maven

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Welcome Endpoint
- **URL**: `GET /api/`
- **Response**: 
```json
{
  "message": "Welcome to Java Mailer Application!",
  "status": "success"
}
```

### Send Email via SMTP (Java Mailer) - Requires Authentication
- **URL**: `POST /api/send-email-smtp`
- **Authentication**: HTTP Basic Auth required
- **Request Body**:
```json
{
  "to": "recipient@example.com",
  "subject": "Test Email via SMTP",
  "body": "This is a test email sent via Java Mailer SMTP"
}
```

- **Response**:
```json
{
  "message": "Email sent successfully via SMTP (Java Mailer)!",
  "status": "success",
  "method": "SMTP"
}
```

### Send Email via SendGrid - Requires Authentication
- **URL**: `POST /api/send-email-sendgrid`
- **Authentication**: HTTP Basic Auth required
- **Request Body**:
```json
{
  "to": "recipient@example.com",
  "subject": "Test Email via SendGrid",
  "body": "This is a test email sent via SendGrid"
}
```

- **Response**:
```json
{
  "message": "Email sent successfully via SendGrid!",
  "status": "success",
  "method": "SendGrid"
}
```

### Send Email via Amazon SES - Requires Authentication
- **URL**: `POST /api/send-email-ses`
- **Authentication**: HTTP Basic Auth required
- **Request Body**:
```json
{
  "to": "recipient@example.com",
  "subject": "Test Email via Amazon SES",
  "body": "This is a test email sent via Amazon SES"
}
```

- **Response**:
```json
{
  "message": "Email sent successfully via Amazon SES!",
  "status": "success",
  "method": "Amazon SES"
}
```

## Testing with curl

```bash
# Test welcome endpoint (public)
curl http://localhost:8080/api/

# Test SMTP email endpoint (requires authentication)
curl -u admin:password -X POST http://localhost:8080/api/send-email-smtp \
  -H "Content-Type: application/json" \
  -d '{
    "to": "recipient@example.com",
    "subject": "Test Email via SMTP",
    "body": "This is a test email sent via Java Mailer SMTP"
  }'

# Test SendGrid email endpoint (requires authentication)
curl -u admin:password -X POST http://localhost:8080/api/send-email-sendgrid \
  -H "Content-Type: application/json" \
  -d '{
    "to": "recipient@example.com",
    "subject": "Test Email via SendGrid",
    "body": "This is a test email sent via SendGrid"
  }'

# Test Amazon SES email endpoint (requires authentication)
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
- **Email Endpoints**: Require HTTP Basic Authentication
- **Welcome Endpoint**: Public access

### Rate Limiting
- **Limit**: 10 requests per minute per IP address
- **Protection**: Prevents spam and abuse attacks
- **Response**: HTTP 429 status when limit exceeded

### Input Validation
- **Email Format**: Validated email addresses only
- **Length Limits**: Subject (200 chars), Body (10,000 chars)
- **Required Fields**: All fields are mandatory
- **XSS Protection**: Input sanitization

### Docker Security
- **Non-root User**: Container runs as non-privileged user
- **Health Checks**: Built-in container health monitoring
- **Environment Variables**: Secure credential management
- **Network Isolation**: Containerized deployment

## Email Service Comparison

| Feature | Java Mailer (SMTP) | SendGrid | Amazon SES |
|---------|-------------------|----------|------------|
| Setup | Gmail App Password | API Key + Verified Sender | AWS Credentials + Verified Sender |
| Reliability | Good | Excellent | Excellent |
| Analytics | Basic | Advanced | Advanced |
| Templates | No | Yes | Yes |
| Rate Limits | Gmail limits | SendGrid limits | AWS limits |
| Cost | Free (Gmail) | Free tier available | Pay per email |
| AWS Integration | No | No | Native |
| Scalability | Limited | High | Very High |

## Docker Deployment

### Quick Start with Docker

```bash
# Clone the repository
git clone <your-repository-url>
cd java_mailer

# Copy environment template
cp env.example .env

# Edit .env with your credentials
# Then run with Docker Compose
docker-compose up --build

# Or run in detached mode
docker-compose up -d --build
```

### Docker Commands

```bash
# View running containers
docker-compose ps

# View logs
docker-compose logs -f

# Stop the application
docker-compose down

# Rebuild and restart
docker-compose up --build
```

## Documentation

- **[Build and Run Guide](Documentation/BUILD_AND_RUN.md)** - Complete setup and deployment instructions
- **[Docker Deployment](Documentation/DOCKER_DEPLOYMENT.md)** - Comprehensive Docker guide
- **[Security Assessment](Documentation/SECURITY_ASSESSMENT.md)** - Security analysis and recommendations