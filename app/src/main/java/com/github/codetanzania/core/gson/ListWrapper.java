package com.github.codetanzania.core.gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class ListWrapper<T> implements ParameterizedType {

    private final Class<?> mWrapped;

    public ListWrapper(Class<T> wrapped) {
        mWrapped = wrapped;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return new Type[] { mWrapped };
    }

    @Override
    public Type getRawType() {
        return List.class;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }
}
