# Library App Catalogue Service / カタログサービス

Distributed Library System — Catalog Service
分散型図書館システム — カタログサービス

---

Overview / 概要

The Catalog Service is responsible for managing and exposing library catalog metadata.

It exposes read-only REST APIs for retrieving series and chapter resources with pagination and filtering support.

---

Catalog Service は図書館カタログ情報の管理・提供を担当するサービスです。

シリーズ情報およびチャプター情報を、ページネーション・フィルタリング対応の REST API として提供します。


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
- Expose series retrieval endpoints
- Expose chapter retrieval endpoints
- Support paginated resource access
- Maintain catalog consistency

日本語

- カタログメタデータの永続化
- シリーズ取得 API 提供
- チャプター取得 API 提供
- ページネーション対応
- データ整合性維持

---

Technology Stack / 技術スタック

Category| Technology
Runtime| Java 21
Framework| Spring Boot 2.7
Persistence| Spring Data JPA
Database| PostgreSQL / H2
Testing| JUnit 5 / Mockito / Jacoco
Containerization| Docker
CI/CD| GitHub Actions

---

API Endpoints / API エンドポイント

Public API

GET /series

GET /series/{seriesUUID}/chapters

GET /chapters

GET /chapters/{chapterUUID}

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

---

Configuration / 設定

SPRING_DATASOURCE_URL=
SPRING_DATASOURCE_USERNAME=
SPRING_DATASOURCE_PASSWORD=

---

Monitoring / モニタリング

/actuator/health

---

CI/CD

- Test execution
- Coverage verification
- Docker publishing

---

License / ライセンス

MIT
