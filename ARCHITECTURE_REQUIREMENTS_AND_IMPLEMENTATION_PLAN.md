# Middleware Multi-Module Platform

## 1) Executive Summary

Build a modular middleware platform that supports multiple inbound/outbound connectors (JMS, Kafka, REST API, and extensible adapters), advanced routing patterns (splitter, join/aggregator, conditional routing), transformation services (XML↔JSON, FreeMarker/FTL, Mustache), batch orchestration, custom logic execution, and a shared utilities module (purging, auditing, auto-reflow, manual reprocess).

The platform should be production-ready, scalable, observable, testable, and extensible via pluggable modules.

---

## 2) Business Goals

1. **Reduce integration lead time** by providing reusable connector and processing components.
2. **Improve reliability** with retries, dead-letter handling, idempotency, and reprocessing.
3. **Enable governance and traceability** through standardized auditing and correlation.
4. **Support heterogeneous payloads** and template-driven transformation needs.
5. **Simplify operations** with auto-reflow, manual reprocess tools, and purging strategy.

---

## 3) Functional Requirements

## 3.1 Connector Service (multi-protocol)

### Required connectors
- JMS (queues/topics)
- Kafka (consumer/producer)
- HTTP API (REST inbound/outbound)
- Future extensibility for SFTP, AMQP, gRPC, MQ, etc.

### Capabilities
- Inbound consumption and outbound publishing.
- Sync and async request handling.
- Header/metadata propagation (correlation ID, tenant, source).
- Pluggable authentication (Basic, OAuth2/JWT, mTLS for APIs where needed).
- Configurable retry policy and backoff.
- Dead-letter queue/topic support.
- Idempotency key support for duplicate suppression.
- Schema/content-type detection and validation hooks.

## 3.2 Distributor Service (routing/orchestration)

### Patterns
- **Splitter**: split bulk payload into item-level messages.
- **Join/Aggregator**: combine related messages based on correlation key and completion criteria.
- **Conditional driver action**: route by rule/condition (e.g., type, source, value thresholds, SLA state).

### Capabilities
- Rules via config (YAML/JSON/DB-backed rule registry) plus optional custom predicates.
- Dynamic routing destinations (topic/queue/API endpoint references).
- Correlation strategy for split/join lifecycle.
- Timeout handling for joins (partial join policy).
- Error channel for failed route paths.

## 3.3 Transformation Service

### Required transformations
- XML → JSON
- JSON → XML
- Template-based renderers:
  - FTL (FreeMarker)
  - Mustache

### Capabilities
- Canonical model option (normalize once, map many).
- XSD/JSON schema validation options.
- Namespace support for XML.
- Template versioning and registry.
- Context enrichment (headers + payload + lookups).
- Mapping failure diagnostics.

## 3.4 Batch Service

### Capabilities
- Scheduled and ad-hoc job execution.
- Chunk-based processing with checkpointing.
- Large-file ingestion and partitioned processing.
- Restart/recovery from checkpoints.
- Job metadata tracking (run ID, status, start/end time, counts, failure causes).
- Integration with connector/distributor/transformation/custom logic stages.

## 3.5 Custom Logic Service

### Capabilities
- Extension points for domain-specific processors.
- Script/plugin model (controlled sandbox) or compiled module SPI.
- Pre/post hooks around routing/transformation.
- Side-effect integrations (DB updates, notifications, external calls) with policies.
- Versioned deployment and rollback of custom logic.

## 3.6 Common Utility Service

### Utilities
- **Purging**:
  - Retention-based cleanup for logs, audit entries, replay store, temp artifacts.
  - Configurable retention by message class/environment.
- **Auditing**:
  - End-to-end trace events per message stage.
  - Immutable audit ledger with correlation ID and actor/source.
- **Auto-reflow**:
  - Automatic replay of transient failures based on policy.
  - Reflow throttling and circuit protection.
- **Manual reprocess**:
  - Operator-initiated replay of selected failed/archived messages.
  - Filter by date range, flow ID, error code, tenant, correlation ID.
  - Dry-run mode and approval workflow (optional).

---

## 4) Non-Functional Requirements

1. **Scalability**
   - Horizontal scale per module.
   - Partition-aware consumption for Kafka/JMS concurrency.
2. **Reliability**
   - At-least-once baseline delivery; exactly-once where feasible with idempotency/transactions.
3. **Performance**
   - p95/p99 latency SLO by route type.
   - Throughput targets per connector.
4. **Observability**
   - Structured logs, metrics, traces (OpenTelemetry).
5. **Security**
   - Secrets management, encryption in transit, authN/authZ, audit compliance.
6. **Operability**
   - Health checks, readiness/liveness, graceful shutdown.
7. **Maintainability**
   - Clear module boundaries, test pyramid, contract testing.

---

## 5) Proposed Multi-Module Project Structure

```text
middleware-platform/
  build-parent/                    # BOM + plugin mgmt + shared build conventions
  common-model/                    # Shared DTOs, canonical message envelope
  common-util-service/             # Purging, auditing, reflow, reprocess, idempotency helper

  connector-service/
    connector-core/                # Connector SPI, lifecycle, config abstraction
    connector-jms/                 # JMS implementation
    connector-kafka/               # Kafka implementation
    connector-api/                 # REST inbound/outbound implementation

  distributor-service/
    distributor-core/              # Split/join/conditional engine
    distributor-rules/             # Rule parser/evaluator

  transformation-service/
    transform-core/                # Common transformation contracts
    transform-xml-json/            # XML↔JSON converters
    transform-ftl/                 # FreeMarker templates
    transform-mustache/            # Mustache templates

  batch-service/                   # Scheduling, partitioning, checkpointing
  custom-logic-service/            # SPI runtime for custom handlers

  control-plane-api/               # Admin APIs: flow deploy, replay, purge, audit query
  operator-ui/                     # Optional portal for operations

  integration-tests/               # Cross-module E2E tests via test containers
  deployment/
    helm/                          # Kubernetes deployment charts
    docker/
  docs/
    architecture/
    runbooks/
```

---

## 6) Core Architectural Concepts

1. **Canonical Message Envelope**
   - `messageId`, `correlationId`, `causationId`, `timestamp`, `source`, `payloadType`, `payload`, `headers`, `tenant`, `retryCount`.
2. **Pipeline Model**
   - Source connector → distributor → transformation/custom logic → target connector.
3. **Flow Definition**
   - Declarative flow DSL (YAML/JSON): connectors, conditions, transforms, retries, DLQ.
4. **Policy Framework**
   - Retry, timeout, idempotency, reflow, purge, masking policies.
5. **Persistence Layer**
   - Operational metadata DB + replay store + audit store.

---

## 7) Detailed Implementation Plan

## Phase 0 — Inception (1–2 weeks)
- Finalize scope and priorities (MVP vs later).
- Define NFR targets and sizing assumptions.
- Produce architecture decision records (ADRs).
- Select stack (e.g., Java/Kotlin + Spring Boot + Kafka + JMS provider + PostgreSQL + Redis optional).

## Phase 1 — Foundation (2–3 weeks)
- Create parent build and module skeleton.
- Implement canonical envelope and shared libraries.
- Setup CI/CD, code quality gates, and baseline observability.
- Setup config management and secrets approach.

## Phase 2 — Connectors MVP (3–5 weeks)
- Implement connector SPI and lifecycle manager.
- Build JMS, Kafka, and REST connectors with integration tests.
- Add retry/DLQ/idempotency baseline behavior.
- Add connector metrics and health endpoints.

## Phase 3 — Distributor Engine (3–4 weeks)
- Implement splitter, aggregator(join), and conditional router.
- Add rule evaluation engine and dynamic destination resolution.
- Add correlation store and join timeout handling.
- Add failure handling paths and compensating events.

## Phase 4 — Transformation Engine (3–4 weeks)
- Implement XML↔JSON converters and schema validation.
- Add FTL and Mustache processors with template registry.
- Add transformation context enrichment and error diagnostics.
- Add versioned transformation definitions.

## Phase 5 — Batch + Custom Logic (3–4 weeks)
- Implement batch job scheduler, chunking, checkpoint/restart.
- Build custom logic SPI with secure execution guardrails.
- Integrate batch and custom logic into flow runtime.

## Phase 6 — Common Utility Service (2–3 weeks)
- Purging scheduler and retention policies.
- End-to-end audit events and query APIs.
- Auto-reflow engine for transient failures.
- Manual reprocess APIs with safe operator controls.

## Phase 7 — Control Plane & Ops (2–3 weeks)
- Flow deployment/versioning APIs.
- Admin endpoints for retries/reprocess/purge/audit query.
- Optional operator UI for monitoring and actions.
- Runbooks, SLO dashboards, and on-call alarms.

## Phase 8 — Hardening & Production Readiness (2–4 weeks)
- Load, resilience, chaos, and failover testing.
- Security review (authZ matrix, secrets, data masking).
- Backward compatibility and migration validation.
- Production rollout strategy (canary/blue-green).

---

## 8) Data & Storage Design (high-level)

1. **Operational DB**
   - Flow config, module config, job metadata, correlation states.
2. **Replay Store**
   - Failed/in-flight archival payloads with retention.
3. **Audit Store**
   - Append-only event records for traceability.
4. **Cache (optional)**
   - Fast lookup for idempotency keys and rule caches.

---

## 9) API Requirements (Control Plane)

- `POST /flows` create/update flow definition.
- `GET /flows/{id}` retrieve deployed flow + version.
- `POST /reprocess` trigger manual replay.
- `POST /reflow/policies/{id}/run` trigger policy-driven reflow.
- `POST /purge/run` ad-hoc purge.
- `GET /audit/messages/{correlationId}` full lifecycle trace.
- `GET /batch/jobs/{jobId}/runs` job history.

---

## 10) Testing Strategy

1. **Unit tests** for connectors, routing, transformation, and utility logic.
2. **Contract tests** for APIs and connector interfaces.
3. **Integration tests** using Testcontainers (Kafka, JMS broker, DB).
4. **E2E tests** with representative flows (split→transform→join→publish).
5. **Performance tests** for throughput/latency under load.
6. **Resilience tests** for broker downtime, network blips, poison messages.

---

## 11) Security & Compliance Checklist

- AuthN/AuthZ for control plane and operator actions.
- PII masking in logs and audit exports.
- Encryption in transit (TLS/mTLS where required).
- Signed/approved flow and template deployments.
- Immutable audit trail for administrative actions.

---

## 12) MVP Scope Recommendation

### Include in MVP
- Kafka + API connectors (JMS if business-critical in wave 1).
- Splitter + conditional routing (join in same wave if mandatory).
- XML↔JSON plus one template engine (FTL or Mustache).
- Basic batch orchestration.
- Auditing + manual reprocess.

### Defer to Phase-2
- Advanced join policies and complex CEP-like routing.
- Multi-tenant isolation hardening.
- UI-heavy operator console enhancements.
- Auto-reflow policy intelligence and adaptive throttling.

---

## 13) Delivery Artifacts

1. Architecture diagram set (context/container/component/sequence).
2. ADR set for key decisions.
3. Flow DSL specification.
4. API OpenAPI specs.
5. Runbook + ops dashboard definitions.
6. Test evidence (functional/perf/resilience/security).

---

## 14) Risks and Mitigations

1. **Connector semantics mismatch** (JMS vs Kafka ordering/ack)
   - Mitigation: abstracted delivery contracts + connector-specific policies.
2. **Transformation complexity growth**
   - Mitigation: canonical model + versioned mapping and strict review.
3. **Operational overhead**
   - Mitigation: strong control plane automation and observability by default.
4. **Reprocess side effects**
   - Mitigation: idempotency keys, dry-run, scoped reprocess approval flow.

---

## 15) Suggested Next Steps (immediate)

1. Confirm MVP boundaries and priority connectors.
2. Choose technology stack and target infrastructure.
3. Define 3 representative pilot flows.
4. Start Phase 1 skeleton with CI, observability, and canonical envelope.
5. Deliver first end-to-end slice: API/Kafka connector → distributor → XML↔JSON transform → output + audit.
