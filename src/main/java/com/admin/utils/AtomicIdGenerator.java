package com.admin.utils;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicIdGenerator {
    private static final AtomicLong idCounter = new AtomicLong();

    public static Long generateId() {
        return idCounter.incrementAndGet();
    }
}
