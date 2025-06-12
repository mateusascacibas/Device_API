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
git clone https://github.com/your-username/device-api.git
cd device-api
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
./mvnw test
```

After running the tests, open the following file in your browser to view the coverage report:

```bash
target/site/jacoco/index.html
```

You will be able to see coverage for classes, methods, lines, and branches.

---

## 💡 Future Improvements

- Add pagination and filtering on device listing
- Add user authentication and authorization
- Improve error handling and exception messages
- Add test coverage for edge cases and exceptions
- CI/CD pipeline for automated builds
