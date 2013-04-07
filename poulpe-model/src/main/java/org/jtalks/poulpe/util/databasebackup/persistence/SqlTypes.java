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
package org.jtalks.poulpe.util.databasebackup.persistence;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jtalks.poulpe.util.databasebackup.common.collection.Lists;

/**
 * The enumeration represents table column's type in database. The enumeration is used to map the JDBC's column type
 * which is a part of {@link java.sql.Types} to a string representation of column type's name.
 * 
 * @author Evgeny Surovtsev
 * 
 */

public enum SqlTypes {
    /**
     * TINYINT type is converted from java.sql.Types.BIT or java.sql.Types.TINYINT. The type has predefined length.
     */
    TINYINT(new int[] { java.sql.Types.BIT, java.sql.Types.TINYINT }, true, false),

    /**
     * SMALLINT type is converted from java.sql.Types.SMALLINT. The type has predefined length.
     */
    SMALLINT(java.sql.Types.SMALLINT, true, false),

    /**
     * INT type is converted from java.sql.Types.INTEGER. The type has predefined length.
     */
    INT(java.sql.Types.INTEGER, true, false),

    /**
     * BIGINT type is converted from java.sql.Types.BIGINT. The type has predefined length.
     */
    BIGINT(java.sql.Types.BIGINT, true, false),

    /**
     * FLOAT type is converted from java.sql.Types.FLOAT. The type has predefined length.
     */
    FLOAT(java.sql.Types.FLOAT, true, false),

    /**
     * REAL type is converted from java.sql.Types.REAL. The type has predefined length.
     */
    REAL(java.sql.Types.REAL, true, false),

    /**
     * DOUBLE type is converted from java.sql.Types.DOUBLE. The type has predefined length.
     */
    DOUBLE(java.sql.Types.DOUBLE, true, false),

    /**
     * NUMERIC type is converted from java.sql.Types.NUMERIC. The type has predefined length.
     */
    NUMERIC(java.sql.Types.NUMERIC, true, false),

    /**
     * DECIMAL type is converted from java.sql.Types.DECIMAL. The type has predefined length.
     */
    DECIMAL(java.sql.Types.DECIMAL, true, false),

    /**
     * CHAR type is converted from java.sql.Types.CHAR. The type has predefined length.
     */
    CHAR(java.sql.Types.CHAR, true, true),

    /**
     * VARCHAR type is converted from java.sql.Types.VARCHAR. The type has predefined length.
     */
    VARCHAR(java.sql.Types.VARCHAR, true, true),

    /**
     * LONGTEXT type is converted from java.sql.Types.LONGVARCHAR. The type has NO predefined length.
     */
    LONGTEXT(java.sql.Types.LONGVARCHAR, false, true),

    /**
     * DATE type is converted from java.sql.Types.DATE. The type has NO predefined length.
     */
    DATE(java.sql.Types.DATE, false, true) {
        @Override
        public List<String> getKeyWordList() {
            return Collections.unmodifiableList(Lists.newArrayList("CURRENT_TIMESTAMP"));
        }
    },

    /**
     * TIME type is converted from java.sql.Types.TIME. The type has NO predefined length.
     */
    TIME(java.sql.Types.TIME, false, true) {
        @Override
        public List<String> getKeyWordList() {
            return Collections.unmodifiableList(Lists.newArrayList("CURRENT_TIMESTAMP"));
        }
    },

    /**
     * TIMESTAMP type is converted from java.sql.Types.TIMESTAMP. The type has NO predefined length.
     */
    TIMESTAMP(java.sql.Types.TIMESTAMP, false, true) {
        @Override
        public List<String> getKeyWordList() {
            return Collections.unmodifiableList(Lists.newArrayList("CURRENT_TIMESTAMP"));
        }
    },

    /**
     * BINARY type is converted from java.sql.Types.BINARY. The type has predefined length.
     */
    BINARY(java.sql.Types.BINARY, true, true),

    /**
     * VARBINARY type is converted from java.sql.Types.VARBINARY. The type has predefined length.
     */
    VARBINARY(java.sql.Types.VARBINARY, true, true),

    /**
     * BLOB type is converted from java.sql.Types.BLOB or java.sql.Types.LONGVARBINARY. The type has NO predefined
     * length.
     */
    BLOB(new int[] { java.sql.Types.BLOB, java.sql.Types.LONGVARBINARY }, false, true),

    /**
     * NULL type is converted from java.sql.Types.NULL. The type has predefined length.
     */
    NULL(java.sql.Types.NULL, true, false),

    /**
     * OTHER type is converted from java.sql.Types.OTHER. The type has predefined length.
     */
    OTHER(java.sql.Types.OTHER, true, false),

    /**
     * JAVA_OBJECT type is converted from java.sql.Types.JAVA_OBJECT. The type has predefined length.
     */
    JAVA_OBJECT(java.sql.Types.JAVA_OBJECT, true, false),

    /**
     * DISTINCT type is converted from java.sql.Types.DISTINCT. The type has predefined length.
     */
    DISTINCT(java.sql.Types.DISTINCT, true, false),

    /**
     * STRUCT type is converted from java.sql.Types.STRUCT. The type has predefined length.
     */
    STRUCT(java.sql.Types.STRUCT, true, false),

    /**
     * ARRAY type is converted from java.sql.Types.ARRAY. The type has predefined length.
     */
    ARRAY(java.sql.Types.ARRAY, true, false),

    /**
     * CLOB type is converted from java.sql.Types.CLOB. The type has NO predefined length.
     */
    CLOB(java.sql.Types.CLOB, false, true),

    /**
     * REF type is converted from java.sql.Types.REF. The type has predefined length.
     */
    REF(java.sql.Types.REF, true, false),

    /**
     * DATALINK type is converted from java.sql.Types.DATALINK. The type has predefined length.
     */
    DATALINK(java.sql.Types.DATALINK, true, false),

    /**
     * BOOLEAN type is converted from java.sql.Types.BOOLEAN. The type has predefined length.
     */
    BOOLEAN(java.sql.Types.BOOLEAN, true, false),

    /**
     * ROWID type is converted from java.sql.Types.ROWID. The type has predefined length.
     */
    ROWID(java.sql.Types.ROWID, true, false),

    /**
     * NCHAR type is converted from java.sql.Types.NCHAR. The type has predefined length.
     */
    NCHAR(java.sql.Types.NCHAR, true, true),

    /**
     * NVARCHAR type is converted from java.sql.Types.NVARCHAR. The type has predefined length.
     */
    NVARCHAR(java.sql.Types.NVARCHAR, true, true),

    /**
     * LONGNVARCHAR type is converted from java.sql.Types.LONGNVARCHAR. The type has predefined length.
     */
    LONGNVARCHAR(java.sql.Types.LONGNVARCHAR, true, true),

    /**
     * NCLOB type is converted from java.sql.Types.NCLOB. The type has NO predefined length.
     */
    NCLOB(java.sql.Types.NCLOB, false, true),

    /**
     * SQLXML type is converted from java.sql.Types.SQLXML. The type has predefined length.
     */
    SQLXML(java.sql.Types.SQLXML, true, false);

    /**
     * Construct the Enumerate value, setting only one value from {@link java.sql.Types} as a mapped column type.
     * 
     * @param jdbcSqlType
     *            The mapped column type which can be returned by JDBC (must match one of the values from
     *            {@link java.sql.Types}).
     * @param hasSize
     *            Does the column type represented by the Enum's value need to have a value's length defined in the
     *            CREATE TABLE SQL. (Example. INT(10) - type INT has size set to 10; TEXT - type TEXT has no defined
     *            size).
     * @param textBased
     *            Should be set to true if a value for this type must be quoted in the SQL INSERT statement.
     */
    private SqlTypes(final int jdbcSqlType, final boolean hasSize, final boolean textBased) {
        this(new int[] { jdbcSqlType }, hasSize, textBased);
    }

    /**
     * Construct the Enumerate value, setting array of values from {@link java.sql.Types} as a mapped column type.
     * 
     * @param jdbcSqlTypeArray
     *            The mapped array of column types which can be returned by JDBC (must match one of the values from
     *            {@link java.sql.Types}).
     * @param hasSize
     *            Does the column type represented by the Enum's value need to have a value's length defined in the
     *            CREATE TABLE SQL. (Example. INT(10) - type INT has size set to 10; TEXT - type TEXT has no defined
     *            size).
     * @param textBased
     *            Should be set to true if a value for this type must be quoted in the SQL INSERT statement.
     */
    private SqlTypes(final int[] jdbcSqlTypeArray, final boolean hasSize, final boolean textBased) {
        assert jdbcSqlTypeArray != null : "jdbcSqlTypeArray must not be null.";
        this.jdbcSqlType = jdbcSqlTypeArray.clone();
        this.hasSize = hasSize;
        this.textBased = textBased;

        Arrays.sort(this.jdbcSqlType);
    }

    /**
     * The method returns all {@link java.sql.Types} values which can be mapped to a given Enumerator's Value.
     * 
     * @return An array, each element of which represents a {@link java.sql.Types} value.
     */
    private int[] getJdbcSqlType() {
        return jdbcSqlType;
    }

    /**
     * Returns if a Column'type represented by given Enumerator's value must have size defined in the SQL CERATE TABLE
     * statement.
     * 
     * @return True if Column'type must have a column size definition (like VARCHAR(30)) or False otherwise (like
     *         LONGTEXT).
     */
    public boolean isHasSize() {
        return hasSize;
    }

    /**
     * Static method maps and returns a SqlTypes's value based on given JDBC's {@link java.sql.Types}.
     * 
     * @param jdbcSqlType
     *            A value represents JDBC SQL Type (should be matched to {@link java.sql.Types}).
     * @return A SqlTypes value associated with the given JDBC SQL Type.
     * @throws IllegalArgumentException
     *             If jdbcSqlType is incorrect.
     */
    public static SqlTypes getSqlTypeByJdbcSqlType(final int jdbcSqlType) {
        for (SqlTypes typeToCheck : sqlTypesValues) {
            if (Arrays.binarySearch(typeToCheck.getJdbcSqlType(), jdbcSqlType) >= 0) {
                return typeToCheck;
            }
        }
        throw new IllegalArgumentException("Given jdbcSqlType=" + jdbcSqlType + " is incorrect.");
    }

    private static SqlTypes[] sqlTypesValues = SqlTypes.values();

    /**
     * Checks if the column's value needs to be quoted before putting it into SQL INSERT statement.
     * 
     * @return True if the column's value must be quoted in the SQL INSERT statement or False otherwise.
     */
    public boolean isTextBased() {
        return textBased;
    }

    /**
     * Returns the list of key words which must not be quoted. For example CURRENT_TIMESTAMP is a keyword for TIMESTAMP
     * type, so it must not be quoted in SQL statements, i.e. "fieldName TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP"
     * but not "fieldName TIMESTAMP NOT NULL DEFAULT 'CURRENT_TIMESTAMP'".
     * 
     * @return list of keyword for the type.
     */
    public List<String> getKeyWordList() {
        return Collections.emptyList();
    }

    private int[] jdbcSqlType;
    private boolean hasSize;
    private boolean textBased;
}
