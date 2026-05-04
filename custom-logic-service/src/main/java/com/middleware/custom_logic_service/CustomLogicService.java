package com.middleware.custom_logic_service;

import com.middleware.platform_common.model.MessageEnvelope;

import java.util.function.Function;

public class CustomLogicService {
    public MessageEnvelope apply(MessageEnvelope input, Function<String, String> fn) {
        return new MessageEnvelope(input.getCorrelationId(), fn.apply(input.getPayload()), input.getHeaders());
    }
}
