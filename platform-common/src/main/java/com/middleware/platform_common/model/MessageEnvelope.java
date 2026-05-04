package com.middleware.platform_common.model;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MessageEnvelope {
    private final String messageId;
    private final String correlationId;
    private final String payload;
    private final Map<String, String> headers;
    private final Instant createdAt;

    public MessageEnvelope(String correlationId, String payload, Map<String, String> headers) {
        this.messageId = UUID.randomUUID().toString();
        this.correlationId = correlationId;
        this.payload = payload;
        this.headers = headers == null ? new HashMap<>() : new HashMap<>(headers);
        this.createdAt = Instant.now();
    }

    public String getMessageId() { return messageId; }
    public String getCorrelationId() { return correlationId; }
    public String getPayload() { return payload; }
    public Map<String, String> getHeaders() { return headers; }
    public Instant getCreatedAt() { return createdAt; }
}
