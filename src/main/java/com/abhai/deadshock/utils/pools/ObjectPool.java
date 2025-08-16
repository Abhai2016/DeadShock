package com.abhai.deadshock.utils.pools;

import java.util.ArrayList;
import java.util.function.Supplier;

public class ObjectPool<T> {
    private final int maxSize;
    private final ArrayList<T> pool;
    private final Supplier<T> factory;

    public ObjectPool(Supplier<T> factory, int initSize, int maxSize) {
        this.factory = factory;
        this.maxSize = maxSize;
        this.pool = new ArrayList<>(initSize);

        for (int i = 0; i < initSize; i++)
            pool.add(factory.get());
    }

    public void put(T object) {
        if (pool.size() < maxSize)
            pool.add(object);
    }

    public T get() {
        return pool.isEmpty() ? factory.get() : pool.removeFirst();
    }
}
