package com.rusloker.pong.events;

public interface EventListener<T> {
    void call(T data);
}
