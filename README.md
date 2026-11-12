# Library App Catalogue Service

Distributed Library System — Catalog Service

---

## Overview

The Catalog Service provides read-only access to catalog metadata within the distributed library platform.

It is responsible for exposing structured series and chapter data to client-facing applications through RESTful APIs.

The service is designed as a stateless, containerized Spring Boot microservice with pagination, filtering, and UUID-based resource retrieval.

---

## Service Boundaries

### Provides

- Series metadata retrieval
- Chapter metadata retrieval
- Paginated catalog access
- Filter-based resource queries

### Does Not Handle

- Borrowing operations
- User management
- Authentication and authorization
- Inventory state management

---

## Badges

<!-- Code Quality & Tests -->

[![Tests](https://github.com/damouu/library-app-catalogue/actions/workflows/run-tests.yml/badge.svg?branch=test)](https://github.com/damouu/library-app-catalogue/actions/workflows/run-tests.yml)
[![Merge PR](https://github.com/damouu/library-app-catalogue/actions/workflows/merge-pr.yml/badge.svg)](https://github.com/damouu/library-app-catalogue/actions/workflows/merge-pr.yml)
[![Prepare](https://github.com/damouu/library-app-catalogue/actions/workflows/prepare.yml/badge.svg)](https://github.com/damouu/library-app-catalogue/actions/workflows/prepare.yml)
[![YouTrack Staging](https://github.com/damouu/library-app-catalogue/actions/workflows/staging.yml/badge.svg)](https://github.com/damouu/library-app-catalogue/actions/workflows/staging.yml)
[![YouTrack Closed](https://github.com/damouu/library-app-catalogue/actions/workflows/youtrack-done.yml/badge.svg)](https://github.com/damouu/library-app-catalogue/actions/workflows/youtrack-done.yml)

<!-- Coverage -->

[![Codecov](https://codecov.io/gh/damouu/library-app-catalogue/branch/test/graph/badge.svg)](https://codecov.io/gh/damouu/library-app-catalogue)

<!-- Docker -->

[![Docker Build](https://github.com/damouu/library-app-catalogue/actions/workflows/build-and-publish.yml/badge.svg)](https://github.com/damouu/library-app-catalogue/actions/workflows/build-and-publish.yml)
[![Docker Image](https://img.shields.io/docker/v/damou/library-app-catalogue?label=docker&logo=docker)](https://hub.docker.com/r/damou/library-app-catalogue)
[![Docker Pulls](https://img.shields.io/docker/pulls/damou/library-app-catalogue?logo=docker)](https://hub.docker.com/r/damou/library-app-catalogue)

<!-- Git / Version -->

[![Git Tag](https://img.shields.io/github/v/tag/damouu/library-app-catalogue?logo=github)](https://github.com/damouu/library-app-catalogue/tags)

<!-- Observability / Monitoring -->

![OpenTelemetry](https://img.shields.io/badge/OpenTelemetry-instrumented-brightgreen)
![Kafka](https://img.shields.io/badge/Kafka-integrated-orange)
![Prometheus](https://img.shields.io/badge/Prometheus-monitored-blue)

---

## Responsibilities

- Persist catalog metadata
- Serve series resources
- Serve chapter resources
- Support filtered retrieval
- Support paginated access
- Maintain catalog consistency

---

## Technology Stack

| Category          | Technology                          |
|-------------------|-------------------------------------|
| Runtime           | Java 21                             |
| Framework         | Spring Boot 2.7                     |
| Persistence       | Spring Data JPA                     |
| Database          | PostgreSQL / H2                     |
| API Documentation | Springdoc OpenAPI                   |
| Validation        | Bean Validation                     |
| Testing           | JUnit 5 / Mockito / JaCoCo / Instancio |
| Containerization  | Docker                              |
| CI/CD             | GitHub Actions                      |

---

## API Endpoints

### Series

#### Get All Series

```http
GET /series
```

Returns paginated series resources.

---

#### Get Series Chapters

```http
GET /series/{seriesUUID}/chapters
```

Returns paginated chapters for the specified series.

---

### Chapters

#### Get All Chapters

```http
GET /chapters
```

Returns paginated chapter resources.

---

#### Get Chapter by UUID

```http
GET /chapters/{chapterUUID}
```

Returns a single chapter resource.

---

## Query Features

Supported capabilities:

- Pagination
- Dynamic filtering
- UUID-based lookup

---

## API Documentation

`/swagger-ui/`

---

## Configuration

The service uses environment-driven configuration.

```env
SPRING_DATASOURCE_URL=
SPRING_DATASOURCE_USERNAME=
SPRING_DATASOURCE_PASSWORD=
```

---

## Monitoring

`/actuator/health`

---

## Build Quality

The CI pipeline enforces:

- Automated test execution
- Coverage thresholds
- Pull request validation
- Docker image publication

---

## License

MIT
