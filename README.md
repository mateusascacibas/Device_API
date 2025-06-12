# Device Management API

A RESTful API for managing devices, built with Spring Boot 3, Java 21, and PostgreSQL.  
Includes Swagger documentation, Docker support, and basic test coverage.

---

## Features

- Register, update, delete, and list devices
- Input validation based on device state
- OpenAPI (Swagger) documentation
- PostgreSQL database persistence
- Docker and Docker Compose support

---

## Tech Stack

- Java 21
- Spring Boot 3
- PostgreSQL
- Maven
- Docker / Docker Compose
- Springdoc OpenAPI
- JUnit 5

---

## Getting Started

### 1. Clone the project

```bash
git clone https://github.com/mateusascacibas/Device_API.git
cd Device_API/
```

### 2. Build the project (generate the `.jar` file)

```bash
./mvnw clean package -DskipTests
```

This will generate the file:  
`target/device-api-0.0.1-SNAPSHOT.jar`

### 3. Run with Docker

Make sure Docker is installed and running. Then execute:

```bash
docker-compose up --build
```

This will:

- Start a PostgreSQL container on port `5432`
- Build and run the API on port `8080`

---

## API Documentation

Once the app is running, access the documentation at:

```
http://localhost:8080/swagger-ui/index.html
```

---

## Running Tests

You can run tests using:

```bash
./mvnw test
```

## Test Coverage with JaCoCo

To generate a coverage report using JaCoCo, run:

```bash
./mvnw test verify
```

After running the tests, open the following file in your browser to view the coverage report:

```bash
target/site/jacoco/index.html
```

You will be able to see coverage for classes, methods, lines, and branches.

---

## CI/CD with GitHub Actions + Deploy via Render
This project uses an automated pipeline configured with GitHub Actions on the main branch. Every time a new push is made, the application is:

```bash
 Built with Maven

 Packaged into a Docker image

 Published to Docker Hub: docker.io/mateusascacibas/device-api:latest

 Automatically deployed to Render via a deploy webhook

 Published to Docker Hub: docker.io/mateusascacibas/device-api:latest
```

Automatically deployed to Render via a deploy webhook

## Live Application
The API is publicly available at:

```bash
📎 Base URL: https://device-api-latest.onrender.com
📘 Swagger UI: https://device-api-latest.onrender.com/swagger-ui/index.html
```


