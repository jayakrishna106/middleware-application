package com.middleware.batch_service;

import java.util.ArrayList;
import java.util.List;

public class BatchService {
    public <T> List<List<T>> chunk(List<T> items, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("chunk size must be > 0");
        }
        List<List<T>> chunks = new ArrayList<>();
        for (int i = 0; i < items.size(); i += size) {
            chunks.add(new ArrayList<>(items.subList(i, Math.min(items.size(), i + size))));
        }
        return chunks;
    }
}
