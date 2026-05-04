package com.middleware.connector_service;

import com.middleware.platform_common.model.MessageEnvelope;

import java.util.ArrayDeque;
import java.util.Queue;

public class ConnectorService {
    private final Queue<MessageEnvelope> ingress = new ArrayDeque<>();
    private final Queue<MessageEnvelope> egress = new ArrayDeque<>();

    public void receive(MessageEnvelope envelope) { ingress.add(envelope); }
    public MessageEnvelope pollIngress() { return ingress.poll(); }
    public void send(MessageEnvelope envelope) { egress.add(envelope); }
    public MessageEnvelope pollEgress() { return egress.poll(); }
}
