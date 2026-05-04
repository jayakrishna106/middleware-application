# Implementation Plan

## Phase 0 - Foundation (Week 1-2)
1. Create multi-module build and shared coding standards.
2. Implement canonical envelope and error contract in `platform-common`.
3. Add baseline observability and centralized config model.
4. Set up CI quality gates (lint, unit test, security scan).

## Phase 1 - Connectors + Distributor MVP (Week 3-6)
1. Build Kafka, JMS, HTTP adapters in `connector-service`.
2. Implement splitter and conditional routing in `distributor-service`.
3. Add DLQ and retry policy integration.
4. Emit audit events for lifecycle steps.

## Phase 2 - Transformation MVP (Week 7-9)
1. Build XML↔JSON conversion APIs in `transformation-service`.
2. Integrate FTL and Mustache template engines.
3. Add schema validation and transformation golden tests.

## Phase 3 - Aggregation + Batch (Week 10-13)
1. Implement join/aggregator with persistent correlation state.
2. Build batch scheduler, checkpoint store, restart/replay controls.
3. Produce batch run metrics and rejection artifacts.

## Phase 4 - Custom Logic + Ops Utilities (Week 14-16)
1. Implement plugin contracts and hook runtime in `custom-logic-service`.
2. Build purge, audit query, auto-reflow, manual reprocess APIs in `ops-service`.
3. Add operator workflows to `admin-ui`.

## Phase 5 - Hardening & Readiness (Week 17-18)
1. Run load/soak/failure tests.
2. Validate security, backup/restore, disaster recovery.
3. Finalize runbooks, SLO dashboards, and production cutover checklist.

## Deliverables by end of MVP
- Connector ingress/egress (Kafka/JMS/API).
- Distributor patterns: split + conditional route + join.
- Transformation: XML↔JSON + FTL + Mustache.
- Batch orchestration with checkpoint restart.
- Utility features: audit, purge, auto-reflow, manual reprocess.
