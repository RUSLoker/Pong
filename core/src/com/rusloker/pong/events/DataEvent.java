package com.rusloker.pong.events;

import java.util.HashSet;
import java.util.Set;

public abstract class DataEvent<T> {
    final Set<EventListener<T>> listeners = new HashSet<>(2);

    public void subscribe(EventListener<T> listener) {
        listeners.add(listener);
    }
}
