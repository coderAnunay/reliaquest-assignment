# 🧩 Employee API - Spring Boot

It serves a set of RESTful APIs for managing employees by interacting with an upstream `mock-employee-api`

---

## 📁 Project Structure

```
employee-challenge-java/
├── api/                  # api module (the application)
│   ├── controller/       # REST controllers
│   ├── dto/              # Data transfer objects
│   ├── service/          # Service layer
│   ├── client/           # WebClient-based API client to consume `mock-employee-api`
│   ├── common/           # Constants and utility classes
│   └── ...               # Application config, main class, etc.
├── build.gradle          # Root Gradle build file
├── server/               # Contains the upstream `mock-employee-api` code
└── settings.gradle       # Includes `api` module
```

---

## 🚀 Getting Started

### ✅ Prerequisites

- Java 17
- Gradle (wrapper included)
- Internet access to resolve dependencies
- `mock-employee-api` is successfully built and run (refer README.md under the `server` module for instructions)

---

## 🛠 Build, Test, and Run

All commands should be run from the **employee-challenge-java**. Look for `settings.gradle`

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

## 📦 Technologies/Tools Used

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
