# Spring Boot Microservices — Banking Backend

## Overview

This repository contains a **Spring Boot microservices backend** for a demo **banking system**.
It demonstrates core microservice patterns such as **service discovery (Eureka)**, **independent services for accounts, customers, transactions, admin operations**, and **notifications/OTP handling**. Each service is modular, scalable, and communicates via REST.

## Features

* ✅ **Account Management** — create, query, deactivate accounts
* ✅ **Transaction Processing** — transfer, history, mini-statements, fraud checks
* ✅ **Customer Management** — registration, login, KYC upload, support tickets
* ✅ **Admin Operations** — approve/reject KYC, view compliance reports
* ✅ **Notifications & OTP** — email OTPs for verification
* ✅ **Service Discovery** — Eureka server for microservice registry
* ✅ **Integrations** — SMTP for email, optional chatbot integration placeholder

---

## Architecture

The architecture follows microservice best practices:

* **Eureka Server** — service registry
* **Independent Business Services**:

  * `springrestaccount` / `Case-Study-Bank-Project-main` — Account Service
  * `CustomerService` — Customer Service
  * `Transaction` — Transaction Service
  * `admin-server` — Admin Service
  * `02_NotificationsModule` — Notifications/OTP Service

**Startup order:**

1. Start **Eureka Server**
2. Start **Admin**, **Notifications**, **Customer**, **Account**, and **Transaction** services
3. Ensure the **database** is up before starting services that need persistence

---

## Services (Modules)

### 1. `02_NotificationsModule`

* Handles OTP and notifications
* Default Port: **8085**
* Endpoints:

  * `POST /otp/send-otp?email=...`
  * `POST /otp/verify-otp?email=...&otp=...`

### 2. `admin-server`

* Manages KYC and admin operations
* Default Port: **8086**
* Endpoints:

  * `POST /admin/send-kyc-details`
  * `GET /admin/kyc/pending`
  * `POST /admin/kyc/{id}/approve`
  * `POST /admin/kyc/{id}/reject`

### 3. `UserAppService`

* Handles user login and registration
* Default Port: **8081**
* Endpoints:

  * `POST /register`
  * `POST /login`
  * `POST /setNewPassword`

### 4. `CustomerService`

* Handles customer registration, login, KYC, chatbot, and support tickets
* Default Port: **8082**
* Endpoints:

  * `POST /customer/register`
  * `POST /customer/login`
  * `POST /customer/uploadKyc`
  * `POST /customer/support-ticket`

### 5. `springrestaccount`

* Another account service (branch/lock handling)
* Default Port: **8083**
* Endpoints:

  * `POST /accounts/createAccount`
  * `PUT /accounts/deactivate/customer/{customerId}`

### 6. `Transaction`

* Handles money transfers and history
* Default Port: **8084**
* Endpoints:

  * `POST /transactions`
  * `GET /transactions/account/{accountId}/mini-statement`

---

## Example Usage

### Create Transaction

```bash
curl -X POST "http://localhost:8084/transactions" -H "Content-Type: application/json" \
  -d '{
    "fromAccountNumber":"1001001",
    "toAccountNumber":"1002002",
    "amount":1500,
    "type":"TRANSFER",
    "pin":"1234"
  }'
```

### Register Customer

```bash
curl -X POST "http://localhost:8082/customer/register" -H "Content-Type: application/json" \
  -d '{
    "firstName":"Alice",
    "lastName":"Smith",
    "email":"alice@example.com",
    "password":"ReplaceWithSecurePassword"
  }'
```

### Send OTP

```bash
curl -X POST "http://localhost:8085/otp/send-otp?email=alice@example.com"
```

### Verify OTP

```bash
curl -X POST "http://localhost:8085/otp/verify-otp?email=alice@example.com&otp=123456"
```

---

## Configuration

This project uses Oracle Database as its primary data source. Update your application.properties or application.yml files with the following configuration (adjust values as needed):

```Environment variables (recommended):
export SPRING_DATASOURCE_URL=jdbc:oracle:thin:@//localhost:1521/ORCLPDB1
export SPRING_DATASOURCE_USERNAME=bank_user
export SPRING_DATASOURCE_PASSWORD=StrongPasswordHere
```

```properties
spring.datasource.url=jdbc:oracle:thin:@//localhost:1521/ORCLPDB1
spring.datasource.username=bank_user
spring.datasource.password=StrongPasswordHere
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver


# Hibernate & JPA settings
spring.jpa.database-platform=org.hibernate.dialect.Oracle12cDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## Setup

1. **Install prerequisites**: JDK 17+, Maven 3.6+, Oracle DB or H2
2. **Build services**:

   ```bash
   mvn clean install -DskipTests
   ```
3. **Run Eureka**:

   ```bash
   cd eureka-server && mvn spring-boot:run
   ```
4. **Run services** in order (Admin, Notifications, Customer, Account, Transaction)

---

## Docker (Optional)

Example Dockerfile:

```dockerfile
FROM eclipse-temurin:17-jdk-jammy
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

Build and run:

```bash
mvn package -DskipTests
docker build -t bank-service .
docker run -p 8080:8080 bank-service
```

---

## Database Entities

Entities include:

* Account, AccountLock, BalanceHistory
* Admin, AdminActivityLog, AdminKyc
* Branch, ComplianceReport
* Customer, CustomerLoginHistory
* Transaction, KycDocument, SupportTicket
* OTP, Notifications, TwoFactorAuth

---

## Testing

Run tests for each service:

```bash
cd <module>
mvn test
```

---

## Troubleshooting

* ❌ **Eureka not registering services** → check `eureka.client.service-url.defaultZone`
* ❌ **DB errors** → verify `spring.datasource` properties
* ❌ **Email not sending** → confirm SMTP credentials and app passwords

---

## Contributing

1. Fork repo
2. Create branch: `feature/my-feature`
3. Add feature + tests
4. Submit PR

---

## License

This project currently has **no license**. Add one (MIT recommended) if needed.

## Contact

For queries, raise an issue in the GitHub repository or contact the maintainer.
