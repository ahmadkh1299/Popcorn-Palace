# Popcorn Palace

## Project Overview

**Popcorn Palace** is a movie ticket booking backend system developed using Java and Spring Boot.  
It provides a RESTful API for managing:

- Movies
- Showtimes
- Ticket Bookings

Users can create movies, schedule showtimes, and book specific seats for showtimes via the exposed endpoints.

---

## Build Requirements

To build and run this project, you’ll need:

- Java 17 or higher
- Maven 3.8+
- IntelliJ IDEA (used during development)
- (Optional) PostgreSQL (for production-like database)

---

## Running the Application

### Using IntelliJ IDEA

1. Open the project in IntelliJ IDEA.
2. Let Maven resolve all dependencies.
3. Navigate to `com.att.tdp.popcorn_palace.PopcornPalaceApplication.java`.
4. Right-click and select **Run**.

### Using Maven CLI

```bash
mvn spring-boot:run
```

The application will start on port `8080`.

Sample endpoints:

- `GET /movies`
- `POST /showtimes`
- `POST /bookings`

---

## Running Tests

### With IntelliJ IDEA

- **All tests**:  
  Right-click on `src/test/java` → Select **Run All Tests**

- **Single test class**:  
  Right-click on `MovieServiceTest.java`, `BookingControllerTest.java`, etc. → Select **Run**

### With Maven

```bash
mvn test
```

### Test Coverage

- **Controllers**
    - `MovieControllerTest.java`
    - `ShowtimeControllerTest.java`
    - `BookingControllerTest.java`

- **Services**
    - `MovieServiceTest.java`
    - `ShowtimeServiceTest.java`
    - `BookingServiceTest.java`

- **End-to-End**
    - `E2Etest.java`: Covers complete application flow from movie creation to seat booking

---

## Configuration

Configuration is stored in:

```
src/main/resources/application.yaml
```

You typically don't need to change this file unless switching to a production database or modifying server settings.

### Sample YAML for PostgreSQL

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/popcorn
    username: your_user
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update
```

---

## Project Structure

```
popcorn-palace/
├── src/
│   ├── main/
│   │   ├── java/com/att/tdp/popcorn_palace/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── entities/
│   │   │   ├── exception/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   └── resources/
│   │       └── application.yaml
│   └── test/
│       ├── java/com/att/tdp/popcorn_palace/controller/
│       ├── java/com/att/tdp/popcorn_palace/service/
│       └── java/com/att/tdp/popcorn_palace/E2Etest.java
├── pom.xml
```

---

## Notes

- All tests and application logic were developed and verified using **IntelliJ IDEA**.
- All tests pass as of the latest commit, including edge cases for showtime overlaps and seat booking conflicts.
- If you encounter port or connection issues, ensure port `8080` is free or change it in `application.yaml`.

---