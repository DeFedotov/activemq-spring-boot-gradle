# ActiveMQ & Spring Boot E2E Testing Pipeline

A production-ready asynchronous message delivery pipeline built with Spring Boot, Apache ActiveMQ, and PostgreSQL. The entire workflow is covered by end-to-end (E2E) integration tests using Behavior-Driven Development (BDD) with the Cucumber framework.

## 🚀 What Does This Project Do?

This repository demonstrates a classic architectural pattern for distributed systems:
1. **Producer:** Sends a text message to the `day3-queue` hosted on the ActiveMQ message broker.
2. **Consumer:** Asynchronously listens to the queue, fetches the message, processes it, and saves it into a PostgreSQL database using Spring Data JPA.
3. **E2E Testing:** Cucumber scenarios (written in Gherkin) automatically trigger the pipeline and verify the data flow by isolating and testing strictly the *latest* record inserted into the live database, preventing false positives from historical data.

---

## 🛠 Tech Stack

| Technology | Purpose | Version |
|---|---|---|
| **Java** | Runtime Environment | 21 |
| **Gradle** | Build Automation Tool | 8.8 |
| **Spring Boot** | Core Application Framework | 3.2.5 |
| **Spring JMS** | Java Message Service Integration | 6.1.6 |
| **Apache ActiveMQ** | Message Broker (JMS Provider) | 6.1.2 |
| **Spring Data JPA** | Database Abstraction Layer (Hibernate) | 3.2.5 |
| **PostgreSQL** | Relational Database | 42.7.3 |
| **Cucumber JVM** | BDD Testing Framework | 7.18.0 |
| **JUnit 5 Platform** | Test Execution Engine | 1.10.2 |

---

## 📋 BDD Test Scenario

The E2E tests are written in human-readable Gherkin syntax (located in `src/test/resources/features/pipeline.feature`):

```gherkin
Feature: Verify end-to-end message delivery pipeline

  @SmokeTest
  Scenario: Sent message is successfully saved as the latest record
    When I send a message "Testing latest record!" to the queue
    Then the latest record in the database should have text "Testing latest record!"

The background test engine scans the real-time database entries, uses Java Streams to locate the maximum record ID, and validates the content of the latest transaction safely without wiping out historical records.

💻 Prerequisites
Before running the project locally, ensure you have:

Java 21 JDK installed

Docker & Docker Compose installed (to host ActiveMQ and PostgreSQL instances)

🚀 How to Run Locally
1. Spin up the Infrastructure
Start the ActiveMQ broker and PostgreSQL database containers using Docker Compose:

Bash
docker-compose up -d
2. Run Integration Tests
To build the application and execute the Cucumber test suite under the correct Java environment, run the following command in your terminal:

Bash
JAVA_HOME=$(/usr/libexec/java_home -v 21) ./gradlew clean test
Once execution completes successfully, Gradle will generate detailed test logs, and you will see the green BUILD SUCCESSFUL status in your terminal.
