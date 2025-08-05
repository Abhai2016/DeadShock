package com.abhai.deadshock.utils;

import java.util.ArrayList;
import java.util.function.Supplier;

public class ObjectPool<T> {
    private final int maxSize;
    private final Supplier<T> factory;
    private final ArrayList<T> pool;

    public ObjectPool(Supplier<T> factory, int initializeSize, int maxSize) {
        this.factory = factory;
        this.maxSize = maxSize;
        this.pool = new ArrayList<>(initializeSize);

        for (int i = 0; i < initializeSize; i++) {
            pool.add(factory.get());
        }
    }

    public void put(T object) {
        if (pool.size() < maxSize)
            pool.add(object);
    }

    public T get() {
        return pool.isEmpty() ? factory.get() : pool.removeFirst();
    }
}
