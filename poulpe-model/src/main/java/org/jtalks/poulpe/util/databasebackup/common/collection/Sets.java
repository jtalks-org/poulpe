package org.jtalks.poulpe.util.databasebackup.common.collection;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Static utility methods pertaining to Set instances. Also see the class's counterparts {@link Lists} and {@link Maps}.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public final class Sets {
    /**
     * Creation an instance of the class is forbidden.
     */
    private Sets() {
    }

    /**
     * Creates a mutable, empty HashSet instance.
     * 
     * @return a new, empty HashSet
     */
    public static <T> Set<T> newHashSet() {
        return new HashSet<T>();
    }

    /**
     * Creates a mutable HashSet instance containing the given elements in provided collection.
     * 
     * @param c
     *            the elements that the set should contain.
     * @return a new HashSet containing those elements (minus duplicates).
     */
    public static <T> Set<T> newHashSet(Collection<T> c) {
        return new HashSet<T>(c);
    }
}
