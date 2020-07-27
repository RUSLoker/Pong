package com.rusloker.pong.events;

public class CallableDataEvent<T> extends DataEvent<T> {
    public void call(T data) {
        for(EventListener<T> i : super.listeners) {
            i.call(data);
        }
    }
}
