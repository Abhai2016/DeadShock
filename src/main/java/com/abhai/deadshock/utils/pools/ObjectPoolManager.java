package com.abhai.deadshock.utils.pools;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ObjectPoolManager<T> {
    private final Map<Class<? extends T>, ObjectPool<? extends T>> pools;

    public ObjectPoolManager() {
        pools = new HashMap<>();
    }

    public <S extends T> void register(Class<? extends S> type, Supplier<? extends S> factory, int initSize, int maxSize) {
        pools.put(type, new ObjectPool<>(factory, initSize, maxSize));
    }

    @SuppressWarnings("unchecked")
    public <S extends T> S get(Class<S> type) {
        ObjectPool<S> pool = (ObjectPool<S>) pools.get(type);
        if (pool == null)
            throw new IllegalStateException("Pool Not Found");
        return pool.get();
    }

    @SuppressWarnings("unchecked")
    public <S extends T> void put(S object) {
        ObjectPool<S> pool = (ObjectPool<S>) pools.get(object.getClass());
        if (pool == null)
            throw new IllegalStateException("Pool Not Found");
        pool.put(object);
    }
}
