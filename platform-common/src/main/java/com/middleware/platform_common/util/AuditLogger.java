package com.middleware.platform_common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AuditLogger {
    private final List<String> events = new ArrayList<>();
    public void record(String event) { events.add(event); }
    public List<String> events() { return Collections.unmodifiableList(events); }
}
