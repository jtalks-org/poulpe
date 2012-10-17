/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jtalks.poulpe.logic.databasebackup.impl;

import java.util.Collection;

//TODO: The class must be migrated to a COMMON project!
/**
 * String Utility class performs different operations under String objects.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public final class StringUtil {
    /**
     * private constructor to disable creating instances of the Utility class.
     */
    private StringUtil() {
    }

    /**
     * An utility's method which joins a given collection of Strings into one String using a given delimiter as a glue.
     * 
     * @param collection
     *            A collection of Strings to be joined.
     * @param delimiter
     *            A delimiter which will be used for joining the given Collection.
     * @return The String as a result of joining the given Collection with given delimiter.
     */
    public static String join(final Collection<String> collection, final String delimiter) {
        if (collection.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String value : collection) {
            sb.append(value + delimiter);
        }
        sb.delete(sb.length() - delimiter.length(), sb.length());
        return sb.toString();
    }
}
