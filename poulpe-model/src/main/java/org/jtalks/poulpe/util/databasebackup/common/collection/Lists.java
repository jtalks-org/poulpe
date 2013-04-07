package org.jtalks.poulpe.util.databasebackup.common.collection;

import java.util.ArrayList;
import java.util.List;

public final class Lists {
    private Lists() {

    }

    public static final <T> List<T> newArrayList() {
        return new ArrayList<T>();
    }

    public static final <T> List<T> newArrayList(T... items) {
        List<T> result = new ArrayList<T>();
        for (T item : items) {
            result.add(item);
        }
        return result;
    }
}
