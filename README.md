# Middleware Application

Production-ready module skeleton for an enterprise middleware platform, including connectors, distributor patterns, transformations, batch orchestration, custom logic hooks, and operations utilities.

## Modules
- `platform-common`: canonical message envelope + shared utilities.
- `connector-service`: ingress/egress connector queue abstraction (Kafka/JMS/API adapter baseline).
- `distributor-service`: splitter, join/aggregator, conditional route selection.
- `transformation-service`: XML↔JSON + Mustache/FTL-style template transformations.
- `batch-service`: chunking engine baseline for batch processing.
- `custom-logic-service`: pluggable payload logic execution.
- `ops-service`: audit storage, purge, manual reprocess APIs.

## Build & verification

### Compile and run full scenario validation (no network needed)
```bash
rm -rf out && mkdir -p out && javac -d out $(find platform-common/src/main/java connector-service/src/main/java distributor-service/src/main/java transformation-service/src/main/java batch-service/src/main/java custom-logic-service/src/main/java ops-service/src/main/java tests -name '*.java') && java -ea -cp out ScenarioTest
```

### Maven packaging (requires access to Maven Central)
```bash
mvn -DskipTests package
```
