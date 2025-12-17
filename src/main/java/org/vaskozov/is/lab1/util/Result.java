package org.vaskozov.is.lab1.util;

public class Result<V, E> {
    private final V value;
    private final E error;

    private Result(V value, E error) {
        this.value = value;
        this.error = error;
    }

    public static <V, E> Result<V, E> success(V value) {
        return new Result<>(value, null);
    }

    public static <V, E> Result<V, E> error(E error) {
        return new Result<>(null, error);
    }

    public boolean isSuccess() {
        return value != null || error == null;
    }

    public boolean isError() {
        return !isSuccess();
    }

    public V getValue() {
        if (isError()) {
            throw new IllegalStateException("No value present");
        }

        return value;
    }

    public E getError() {
        if (isSuccess()) {
            throw new IllegalStateException("No error present");
        }

        return error;
    }
}
