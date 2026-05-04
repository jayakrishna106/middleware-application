# Detailed Requirements

## 1. Functional Scope

### 1.1 Connector Service
- Support inbound/outbound JMS, Kafka, and HTTP API connectors.
- Normalize metadata into a common envelope.
- At-least-once delivery with idempotency options.

### 1.2 Distributor Service
- Splitter pattern for array/list/XML node fan-out.
- Join/Aggregator pattern with completion by count, timeout, or marker.
- Conditional driver action for content/header/rule-based routing.
- Default/fallback routes and dead-letter channel support.

### 1.3 Transformation Service
- XML to JSON conversion.
- JSON to XML conversion.
- FTL (FreeMarker) template transforms.
- Mustache template transforms.
- Pre/post validation with schema references.

### 1.4 Batch Service
- Scheduled and ad-hoc jobs.
- Chunk processing and restart from checkpoint.
- Success/failure summaries and rejected record output.

### 1.5 Custom Logic Service
- Pluggable pre-route, pre-transform, post-transform, pre-delivery hooks.
- Controlled runtime contract (input envelope + context, output + error model).

### 1.6 Common Utility Scope (Ops)
- Purging by retention policy.
- Auditing for every message lifecycle event.
- Auto-reflow for retry/re-drive with backoff.
- Manual reprocess by message ID/correlation/date range.

## 2. Non-Functional Requirements
- Horizontal scalability and independent module deployment.
- Observability: logs, metrics, traces.
- Security: OAuth2/JWT, TLS, secret management, PII masking.
- Resilience: retries, DLQ, circuit breakers, timeouts.
- Performance targets (define p95 latency and throughput SLOs per flow type).

## 3. Canonical Envelope (Minimum Fields)
- `messageId`
- `correlationId`
- `causationId`
- `sourceSystem`
- `messageType`
- `schemaRef`
- `headers`
- `payload`
- `attempt`
- `status`
- `timestamps`
