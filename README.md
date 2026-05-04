# Library App Catalogue Service / カタログサービス

Distributed Library System — Catalog Service
分散型図書館システム — カタログサービス

---

Overview / 概要

The Catalog Service provides read-only access to catalog metadata within the distributed library platform.

It is responsible for exposing structured series and chapter data to client-facing applications through RESTful APIs.

The service is designed as a stateless, containerized Spring Boot microservice with pagination, filtering, and
UUID-based resource retrieval.

---

Catalog Service は分散型図書館システムにおいて、カタログ情報を参照専用 API として提供するサービスです。

シリーズおよびチャプター情報を REST API 経由で提供し、ページネーション・フィルタリング・UUID 指定取得に対応しています。

ステートレスな Spring Boot マイクロサービスとして設計されています。

---

Service Boundaries / サービス境界

Provides

- Series metadata retrieval
- Chapter metadata retrieval
- Paginated catalog access
- Filter-based resource queries

Does Not Handle

- Borrowing operations
- User management
- Authentication / authorization
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


Responsibilities / 責務

English

- Persist catalog metadata
- Serve series resources
- Serve chapter resources
- Support filtered retrieval
- Support paginated access
- Maintain catalog consistency

日本語

- カタログメタデータ永続化
- シリーズ情報提供
- チャプター情報提供
- フィルタリング対応
- ページネーション対応
- データ整合性維持

---

Technology Stack / 技術スタック

Category| Technology
Runtime| Java 21
Framework| Spring Boot 2.7
Persistence| Spring Data JPA
Database| PostgreSQL / H2
API Documentation| Springdoc OpenAPI
Validation| Bean Validation
Testing| JUnit 5 / Mockito / Jacoco / Instancio
Containerization| Docker
CI/CD| GitHub Actions

---

API Endpoints / API エンドポイント

Series

GET /series

Returns paginated series resources.

---

GET /series/{seriesUUID}/chapters

Returns paginated chapters for the specified series.

---

Chapters

GET /chapters

Returns paginated chapter resources.

---

GET /chapters/{chapterUUID}

Returns a single chapter resource.

---

Query Features / クエリ機能

Supported capabilities:

- Pagination
- Dynamic filtering
- UUID-based lookup

---

API Documentation / API ドキュメント

/swagger-ui/

---

Local Development / ローカル開発

Requirements

- Java 21
- Maven
- Docker
- PostgreSQL

---

Run

docker compose up --build

---

Testing / テスト

./mvnw verify

Includes:

- Unit tests
- Integration tests
- Coverage verification

---

Configuration / 設定

SPRING_DATASOURCE_URL=
SPRING_DATASOURCE_USERNAME=
SPRING_DATASOURCE_PASSWORD=

Environment-driven configuration.

---

Monitoring / モニタリング

/actuator/health

---

Build Quality / 品質保証

The build pipeline enforces:

- Automated test execution
- Coverage thresholds
- Pull request validation
- Docker image publication

---

License / ライセンス

MIT
