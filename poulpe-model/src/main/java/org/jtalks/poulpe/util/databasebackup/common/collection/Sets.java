package org.jtalks.poulpe.util.databasebackup.common.collection;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class Sets {
    private Sets() {
    }

    public static <T> Set<T> newHashSet() {
        return new HashSet<T>();
    }

    public static <T> Set<T> newHashSet(Collection<T> c) {
        return new HashSet<T>(c);
    }
}
