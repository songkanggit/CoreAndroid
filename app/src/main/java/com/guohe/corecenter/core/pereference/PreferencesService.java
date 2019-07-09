package com.guohe.corecenter.core.pereference;

import java.util.HashMap;

public interface PreferencesService {
    void put(final String key, final Object value);

    <O> O get(final String key, final O defaultValue);

    void writeObject(final String key, final Object value);

    Object readObject(final String key);

    void remove(final String key);

    void clear();

    boolean contains(final String key);

    HashMap<String, ?> getAll();
}
