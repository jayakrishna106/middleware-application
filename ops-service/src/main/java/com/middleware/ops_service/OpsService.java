package com.middleware.ops_service;

import com.middleware.platform_common.model.MessageEnvelope;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OpsService {
    private final List<MessageEnvelope> store = new ArrayList<>();
    public void audit(MessageEnvelope m) { store.add(m); }
    public List<MessageEnvelope> purgeOlderThan(Instant cutoff) {
        List<MessageEnvelope> purged = new ArrayList<>();
        store.removeIf(m -> {
            boolean old = m.getCreatedAt().isBefore(cutoff);
            if (old) purged.add(m);
            return old;
        });
        return purged;
    }
    public List<MessageEnvelope> manualReprocessByCorrelation(String c) {
        return store.stream().filter(m -> m.getCorrelationId().equals(c)).toList();
    }
}
