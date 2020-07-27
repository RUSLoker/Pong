package com.rusloker.pong;

public class Pair<F, S> {
    public final F first;
    public final S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) {
            return false;
        }
        Pair<?, ?> p = (Pair<?, ?>) o;
        return p.first.equals(first) && p.second.equals(second);
    }

    @Override
    public int hashCode() {
        return (first == null ? 0 : first.hashCode()) ^ (second == null ? 0 : second.hashCode());
    }

    @Override
    public String toString() {
        return "Pair{" + first + " " + second + "}";
    }

    public static <A, B> Pair <A, B> create(A a,  B b) {
        return new Pair<>(a, b);
    }
}
