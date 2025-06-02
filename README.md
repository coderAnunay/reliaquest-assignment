# reliaquest-assignment
# 🧩 Employee API - Spring Boot

It serves a set of RESTful APIs for managing employees by interacting with an upstream mock server.

---

## 📁 Project Structure

```
.
├── api/                  # Main Spring Boot module
│   ├── controller/       # REST controllers
│   ├── dto/              # Data transfer objects
│   ├── service/          # Business logic layer
│   ├── client/           # WebClient-based API client to mock server
│   ├── common/           # Constants and utility classes
│   └── ...               # Application config, main class, etc.
├── build.gradle          # Root Gradle build file
└── settings.gradle       # Includes `api` module
```

---

## 🚀 Getting Started

### ✅ Prerequisites

- Java 17+
- Gradle (wrapper included)
- Internet access to resolve dependencies

---

## 🛠 Build, Test, and Run

All commands should be run from the **project root**.

### 📦 Build the Project

```bash
./gradlew clean build
```

This will:
- Compile the code
- Run unit + integration tests
- Package the application

---

### ✅ Run Unit Tests Only

```bash
./gradlew :api:test
```

---

### 🔁 Run Integration Tests or Smoke Test Only

```bash
./gradlew :api:test --tests '*IntegrationTest'
./gradlew :api:test --tests 'ApiApplicationTest'
```

---

### 🚀 Run the Application

```bash
./gradlew :api:bootRun
```

App will start on the configured port (as defined in `application.yml`).

---

### 🧹 Clean the Build Artifacts

```bash
./gradlew clean
```

---

## 📦 Technologies Used

- Java 17
- Spring Boot
- Spring WebFlux (`WebClient`)
- Jakarta Bean Validation
- JUnit 5 + Mockito
- WebTestClient (for integration testing)
- Gradle

---

## 🧪 Testing Strategy

- **Unit Tests**: Controller and service logic, mocking dependencies
- **Integration Tests**: Full CRUD scenarios using `WebTestClient`, with upstream API mocked

---

## 👨‍💻 Author

Built by **Anunay Sinha** as part assessment.
