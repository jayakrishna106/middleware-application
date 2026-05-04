package com.middleware.distributor_service;

import com.middleware.platform_common.model.MessageEnvelope;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DistributorService {
    public List<MessageEnvelope> splitByComma(MessageEnvelope parent) {
        if (parent.getPayload() == null || parent.getPayload().isBlank()) {
            return List.of();
        }
        List<MessageEnvelope> out = new ArrayList<>();
        for (String p : parent.getPayload().split(",")) {
            String trimmed = p.trim();
            if (!trimmed.isEmpty()) {
                out.add(new MessageEnvelope(parent.getCorrelationId(), trimmed, parent.getHeaders()));
            }
        }
        return out;
    }

    public MessageEnvelope join(List<MessageEnvelope> parts) {
        if (parts == null || parts.isEmpty()) {
            throw new IllegalArgumentException("parts cannot be empty");
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(parts.get(i).getPayload());
        }
        return new MessageEnvelope(parts.get(0).getCorrelationId(), sb.toString(), Map.of("joined", "true"));
    }

    public String route(MessageEnvelope message) {
        String type = message.getHeaders().getOrDefault("type", "default").toLowerCase();
        return switch (type) {
            case "kafka" -> "KAFKA_CHANNEL";
            case "jms" -> "JMS_CHANNEL";
            case "api" -> "API_CHANNEL";
            default -> "DLQ_CHANNEL";
        };
    }
}
