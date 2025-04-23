# SWIFT Demo API

This project provides a REST API for managing SWIFT codes, banks, and their associated countries. You can create, retrieve, list by country, and delete SWIFT codes — with support for identifying headquarters and branches.

---

## How to Run with Docker

To get started:

1. Ensure you have Docker and Docker Compose installed.
2. In the project root, build and run the containers:

```bash
docker-compose up --build
```
The API will be available at: http://localhost:8080

Note: Ensure port 5432 (PostgreSQL) and 8080 (Spring Boot app) are not used by other services on your machine before running Docker.

---
## API Endpoints
### Get SWIFT Code Details
```http request
GET /v1/swift-codes/{swiftCode}
```
Description: Returns details for a specific SWIFT code.

Response:
```json
{
  "swiftCode": "INGBPLPWXXX",
  "bankName": "ING Bank Śląski",
  "countryName": "Poland",
  "countryISO2": "PL",
  "address": "ul. Sokolska 34, 40-086 Katowice",
  "headquarter": true
}
```
### Get All SWIFT Codes by Country
```http request
GET /v1/swift-codes/country/{countryISO2}
```
Description: Returns all SWIFT codes for a specific country (e.g., PL, US).

Response:
```json
{
  "country": "PL",
  "swiftCodes": [
    {
      "swiftCode": "INGBPLPWXXX",
      "bankName": "ING Bank Śląski",
      "headquarter": true
    },
    {
      "swiftCode": "INGBPLPW002",
      "bankName": "ING Branch Wrocław",
      "headquarter": false
    }
  ]
}

```
### Add a New SWIFT Code
```http request
POST /v1/swift-codes
```
Description: Adds a new SWIFT code (headquarters must be created first).

Request body:
```json
{
  "swiftCode": "INGBPLPW002",
  "bankName": "ING Branch Wrocław",
  "countryISO2": "PL",
  "countryName": "Poland",
  "address": "ul. Wita Stwosza 1-2, 50-148 Wrocław",
  "headquarter": false
}
```
Response:
```json
{
  "message": "SWIFT code successfully added"
}
```
### Delete a SWIFT Code
```http request
DELETE /v1/swift-codes/{swiftCode}
```
Description: Deletes a SWIFT code.

Note: You cannot delete a headquarter if it still has registered branches.

Response:
```json
{
  "message": "SWIFT code successfully deleted"
}
```

### Configuration
```properties
spring.application.name=swift_demo
server.port=8080

spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Tech Stack: Spring Boot · PostgreSQL · Docker · REST API