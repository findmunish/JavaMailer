# Swagger API Documentation

This document provides comprehensive information about the Java Mailer API documentation using Swagger/OpenAPI.

## 📋 Overview

The Java Mailer application includes interactive API documentation powered by Swagger UI and OpenAPI 3.0 specification. This allows developers to explore, test, and understand all available endpoints.

## 🔗 Access Points

### **Swagger UI Interface**
- **URL**: `http://localhost:8080/swagger-ui.html`
- **Description**: Interactive web interface for API testing
- **Features**: 
  - Live API testing
  - Authentication support
  - Request/response examples
  - Schema validation

### **OpenAPI JSON Specification**
- **URL**: `http://localhost:8080/api-docs`
- **Description**: Machine-readable API specification
- **Format**: JSON
- **Use Cases**: Code generation, API client creation

## 🚀 Getting Started

### **1. Start the Application**
```bash
# Using Docker
docker-compose up -d --build

# Using Maven
mvn spring-boot:run
```

### **2. Access Swagger UI**
1. Open your browser
2. Navigate to: `http://localhost:8080/swagger-ui.html`
3. Explore the API documentation

### **3. Authentication Setup**
1. Click the **"Authorize"** button in Swagger UI
2. Enter credentials:
   - **Username**: `admin`
   - **Password**: `change-this-password` (or your custom password)
3. Click **"Authorize"** to authenticate

## 📚 API Endpoints Documentation

### **Public Endpoints**

#### **Welcome Endpoint**
- **URL**: `GET /api/`
- **Authentication**: Not required
- **Description**: Returns welcome message
- **Response Example**:
```json
{
  "message": "Welcome to Java Mailer Application!",
  "status": "success"
}
```

### **Protected Endpoints (Authentication Required)**

#### **Send Email via SMTP**
- **URL**: `POST /api/send-email-smtp`
- **Authentication**: Required (HTTP Basic Auth)
- **Description**: Send email using Gmail SMTP (Java Mailer)
- **Request Body**:
```json
{
  "to": "recipient@example.com",
  "subject": "Test Email via SMTP",
  "body": "This is a test email sent via Java Mailer SMTP"
}
```
- **Response Example**:
```json
{
  "message": "Email sent successfully via SMTP (Java Mailer)!",
  "status": "success",
  "method": "SMTP"
}
```

#### **Send Email via SendGrid**
- **URL**: `POST /api/send-email-sendgrid`
- **Authentication**: Required (HTTP Basic Auth)
- **Description**: Send email using SendGrid API
- **Request Body**:
```json
{
  "to": "recipient@example.com",
  "subject": "Test Email via SendGrid",
  "body": "This is a test email sent via SendGrid"
}
```
- **Response Example**:
```json
{
  "message": "Email sent successfully via SendGrid!",
  "status": "success",
  "method": "SendGrid"
}
```

#### **Send Email via Amazon SES**
- **URL**: `POST /api/send-email-ses`
- **Authentication**: Required (HTTP Basic Auth)
- **Description**: Send email using Amazon Simple Email Service
- **Request Body**:
```json
{
  "to": "recipient@example.com",
  "subject": "Test Email via Amazon SES",
  "body": "This is a test email sent via Amazon SES"
}
```
- **Response Example**:
```json
{
  "message": "Email sent successfully via Amazon SES!",
  "status": "success",
  "method": "Amazon SES"
}
```

## 🔐 Authentication

### **HTTP Basic Authentication**
All email endpoints require HTTP Basic Authentication:

- **Username**: `admin` (configurable)
- **Password**: `change-this-password` (configurable)

### **Configuration**
Update credentials in `application.properties`:
```properties
spring.security.user.name=${SPRING_USER:admin}
spring.security.user.password=${SPRING_PASSWORD:change-this-password}
```

### **Environment Variables**
For Docker deployment, set environment variables:
```bash
SPRING_USER=admin
SPRING_PASSWORD=your-secure-password
```

## 📊 Request/Response Schemas

### **EmailRequest Schema**
```json
{
  "type": "object",
  "required": ["to", "subject", "body"],
  "properties": {
    "to": {
      "type": "string",
      "format": "email",
      "description": "Recipient email address",
      "example": "recipient@example.com"
    },
    "subject": {
      "type": "string",
      "maxLength": 200,
      "description": "Email subject line",
      "example": "Test Email"
    },
    "body": {
      "type": "string",
      "maxLength": 10000,
      "description": "Email body content",
      "example": "This is a test email"
    }
  }
}
```

### **Error Response Schema**
```json
{
  "type": "object",
  "properties": {
    "message": {
      "type": "string",
      "description": "Error message"
    },
    "status": {
      "type": "string",
      "enum": ["error"],
      "description": "Response status"
    },
    "method": {
      "type": "string",
      "description": "Email service method used"
    }
  }
}
```

## 🛡️ Security Features

### **Rate Limiting**
- **Limit**: 10 requests per minute per IP address
- **Response**: HTTP 429 when limit exceeded
- **Protection**: Prevents spam and abuse

### **Input Validation**
- **Email Format**: Validated email addresses only
- **Length Limits**: 
  - Subject: 200 characters maximum
  - Body: 10,000 characters maximum
- **Required Fields**: All fields mandatory
- **XSS Protection**: Input sanitization

### **CORS Configuration**
- **Allowed Origins**: Specific domains only
- **Methods**: GET, POST, PUT, DELETE, OPTIONS
- **Headers**: All headers allowed
- **Credentials**: Supported

## 🧪 Testing with Swagger UI

### **1. Interactive Testing**
1. Open Swagger UI: `http://localhost:8080/swagger-ui.html`
2. Click "Authorize" and enter credentials
3. Expand any endpoint
4. Click "Try it out"
5. Fill in request parameters
6. Click "Execute"
7. View response

### **2. Example Test Cases**

#### **Test Welcome Endpoint**
- **Method**: GET
- **URL**: `/api/`
- **Authentication**: Not required
- **Expected Response**: 200 OK with welcome message

#### **Test Email Endpoint**
- **Method**: POST
- **URL**: `/api/send-email-smtp`
- **Authentication**: Required
- **Request Body**:
```json
{
  "to": "test@example.com",
  "subject": "Swagger Test",
  "body": "Testing from Swagger UI"
}
```

## 📈 API Documentation Features

### **Swagger UI Features**
- ✅ **Interactive Testing** - Test APIs directly from browser
- ✅ **Authentication Support** - Built-in Basic Auth testing
- ✅ **Request Validation** - Real-time validation
- ✅ **Response Examples** - See expected responses
- ✅ **Schema Documentation** - Complete data models
- ✅ **Error Handling** - Comprehensive error responses

### **OpenAPI Specification**
- ✅ **OpenAPI 3.0** - Latest specification
- ✅ **Machine Readable** - JSON format
- ✅ **Code Generation** - Client SDK generation
- ✅ **API Gateway** - Integration ready
- ✅ **Testing Tools** - Postman, Insomnia support

## 🔧 Configuration

### **Swagger Configuration**
```properties
# Swagger/OpenAPI Configuration
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
springdoc.swagger-ui.tagsSorter=alpha
springdoc.swagger-ui.tryItOutEnabled=true
```

### **Security Configuration**
```java
// Allow public access to Swagger endpoints
.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api-docs/**").permitAll()
```

## 🚀 Advanced Usage

### **Code Generation**
Use the OpenAPI specification to generate client code:

```bash
# Generate Java client
openapi-generator generate -i http://localhost:8080/api-docs -g java -o ./client

# Generate JavaScript client
openapi-generator generate -i http://localhost:8080/api-docs -g javascript -o ./client
```

### **API Testing Tools**
- **Postman**: Import OpenAPI spec from `/api-docs`
- **Insomnia**: Import OpenAPI spec from `/api-docs`
- **curl**: Use examples from Swagger UI

### **Integration Testing**
```bash
# Test API endpoints
curl -u admin:password -X POST http://localhost:8080/api/send-email-smtp \
  -H "Content-Type: application/json" \
  -d '{"to":"test@example.com","subject":"Test","body":"Test email"}'
```

## 📋 Troubleshooting

### **Common Issues**

#### **1. Swagger UI Not Loading**
- **Check**: Application is running on port 8080
- **Solution**: Restart application
- **URL**: `http://localhost:8080/swagger-ui.html`

#### **2. Authentication Failing**
- **Check**: Credentials in `application.properties`
- **Solution**: Update username/password
- **Test**: Use curl with Basic Auth

#### **3. API Endpoints Not Working**
- **Check**: Security configuration
- **Solution**: Verify endpoint permissions
- **Test**: Check application logs

#### **4. Rate Limiting Issues**
- **Check**: Request frequency
- **Solution**: Wait for rate limit reset
- **Limit**: 10 requests per minute per IP

## 📚 Additional Resources

### **Documentation Links**
- [SpringDoc OpenAPI Documentation](https://springdoc.org/)
- [OpenAPI Specification](https://swagger.io/specification/)
- [Swagger UI Documentation](https://swagger.io/tools/swagger-ui/)

### **Related Documentation**
- [Build and Run Guide](BUILD_AND_RUN.md)
- [Docker Deployment](DOCKER_DEPLOYMENT.md)
- [Security Assessment](SECURITY_ASSESSMENT.md)

## 🎯 Best Practices

### **API Testing**
1. **Use Swagger UI** for interactive testing
2. **Test all endpoints** with different scenarios
3. **Validate responses** against schemas
4. **Test authentication** with valid/invalid credentials

### **Documentation Maintenance**
1. **Keep schemas updated** when adding new fields
2. **Update examples** for new use cases
3. **Maintain security documentation**
4. **Test documentation** regularly

### **Production Deployment**
1. **Disable Swagger UI** in production (optional)
2. **Use environment variables** for credentials
3. **Implement proper logging**
4. **Monitor API usage**

---

**Swagger UI**: `http://localhost:8080/swagger-ui.html`
**OpenAPI Spec**: `http://localhost:8080/api-docs`
**Application**: `http://localhost:8080/api/`