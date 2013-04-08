package org.jtalks.poulpe.util.databasebackup.common.collection;

import java.util.ArrayList;
import java.util.List;

/**
 * Static utility methods pertaining to List instances. Also see the class's counterparts {@link Sets} and {@link Maps}.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public final class Lists {
    /**
     * Creation an instance of the class is forbidden.
     */
    private Lists() {

    }

    /**
     * Creates a mutable, empty ArrayList instance.
     * 
     * @return an empty ArrayList instance.
     */
    public static <T> List<T> newArrayList() {
        return new ArrayList<T>();
    }

    /**
     * Creates a mutable ArrayList instance containing the given elements.
     * 
     * @param elements
     *            the elements that the list should contain, in order
     * @return a new ArrayList containing those elements
     */
    public static <T> List<T> newArrayList(T... elements) {
        List<T> result = new ArrayList<T>();
        for (T element : elements) {
            result.add(element);
        }
        return result;
    }
}
