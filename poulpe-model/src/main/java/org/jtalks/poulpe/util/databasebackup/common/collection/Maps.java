package org.jtalks.poulpe.util.databasebackup.common.collection;

import java.util.HashMap;
import java.util.Map;

/**
 * Static utility methods pertaining to Map instances. Also see the class's counterparts {@link Lists} and {@link Sets}.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public final class Maps {
    /**
     * Creation an instance of the class is forbidden.
     */
    private Maps() {
    }

    /**
     * Creates a mutable, empty HashMap instance.
     * 
     * @return a new, empty HashMap.
     */
    public static <K, V> Map<K, V> newHashMap() {
        return new HashMap<K, V>();
    }
}
