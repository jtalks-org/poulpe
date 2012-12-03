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
package org.jtalks.poulpe.util.databasebackup.logic.impl;

import java.util.List;

import org.apache.commons.lang3.Validate;

import com.google.common.base.Joiner;

/**
 * The utility class provides helper methods used while SQL-dumping.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public final class SqlTableDumpUtil {
    /**
     * The utility class has no instances.
     */
    private SqlTableDumpUtil() {
    }

    /**
     * Convert a list of Strings into one string with given separator. For empty list an empty StringBuilder is
     * returned.
     * 
     * @param stringList
     *            a list of strings to join.
     * @param separator
     *            a separator to insert between strings under joining.
     * @return Joined list of strings.
     * @throws NullPointerException
     *             if stringList or separator is null.
     */
    public static StringBuilder joinStrings(final List<String> stringList, final String separator) {
        Validate.notNull(stringList, "stringList must not be null");
        Validate.notNull(separator, "separator must not be null");
        StringBuilder result = new StringBuilder();
        if (stringList.size() > 0) {
            Joiner joiner = Joiner.on(separator).skipNulls();
            result.append(joiner.join(stringList));
        }
        return result;
    }

    /**
     * Line feed constant.
     */
    public static final String LINEFEED = "\n";
}
