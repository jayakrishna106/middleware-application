# Middleware Application

Multi-module middleware integration platform with connectors, distribution, transformation, batch orchestration, custom logic, and operations utilities.

## Build System

This project uses a **Maven multi-module build** with Java 17.

### Included Maven modules
- `platform-common`
- `connector-service`
- `distributor-service`
- `transformation-service`
- `batch-service`
- `custom-logic-service`
- `ops-service`

### Non-build folders
- `admin-ui` (UI placeholder)
- `infra` (deployment/infrastructure placeholder)
- `docs` (requirements and implementation plan)

## Quick start

```bash
mvn clean test
```

```bash
mvn clean package
```

## Next implementation steps

1. Add connector adapters (Kafka/JMS/HTTP) in `connector-service`.
2. Add splitter/join/conditional routing in `distributor-service`.
3. Add XML↔JSON + FTL/Mustache transformation engine in `transformation-service`.
4. Add batch orchestration, auditing, and reprocess flows.
