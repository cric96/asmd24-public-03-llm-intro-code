package it.unibo.utils;

public record Pair<X, Y>(X x, Y y) {

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public static <X, Y> Pair<X, Y> of(X x, Y y) {
        return new Pair<>(x, y);
    }
}
