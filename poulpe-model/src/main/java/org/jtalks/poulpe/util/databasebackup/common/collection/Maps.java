package org.jtalks.poulpe.util.databasebackup.common.collection;

import java.util.HashMap;
import java.util.Map;

public final class Maps {
    private Maps() {
    }

    public static <K, V> Map<K, V> newHashMap() {
        return new HashMap<K, V>();
    }
}
