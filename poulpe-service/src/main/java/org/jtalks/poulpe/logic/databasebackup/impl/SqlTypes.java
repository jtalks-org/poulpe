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

import java.util.Arrays;

public enum SqlTypes {
    TINYINT(new int[] { java.sql.Types.BIT, java.sql.Types.TINYINT }, true),
    SMALLINT(java.sql.Types.SMALLINT, true),
    INT(java.sql.Types.INTEGER, true),
    BIGINT(java.sql.Types.BIGINT, true),
    FLOAT(java.sql.Types.FLOAT, true),
    REAL(java.sql.Types.REAL, true),
    DOUBLE(java.sql.Types.DOUBLE, true),
    NUMERIC(java.sql.Types.NUMERIC, true),
    DECIMAL(java.sql.Types.DECIMAL, true),
    CHAR(java.sql.Types.CHAR, true),
    VARCHAR(java.sql.Types.VARCHAR, true),
    LONGTEXT(java.sql.Types.LONGVARCHAR, false),
    DATE(java.sql.Types.DATE, false),
    TIME(java.sql.Types.TIME, false),
    TIMESTAMP(java.sql.Types.TIMESTAMP, false),
    BINARY(java.sql.Types.BINARY, true),
    VARBINARY(java.sql.Types.VARBINARY, true),
    BLOB(new int[] { java.sql.Types.BLOB, java.sql.Types.LONGVARBINARY }, false),
    NULL(java.sql.Types.NULL, true),
    OTHER(java.sql.Types.OTHER, true),
    JAVA_OBJECT(java.sql.Types.JAVA_OBJECT, true),
    DISTINCT(java.sql.Types.DISTINCT, true),
    STRUCT(java.sql.Types.STRUCT, true),
    ARRAY(java.sql.Types.ARRAY, true),
    CLOB(java.sql.Types.CLOB, false),
    REF(java.sql.Types.REF, true),
    DATALINK(java.sql.Types.DATALINK, true),
    BOOLEAN(java.sql.Types.BOOLEAN, true),
    ROWID(java.sql.Types.ROWID, true),
    NCHAR(java.sql.Types.NCHAR, true),
    NVARCHAR(java.sql.Types.NVARCHAR, true),
    LONGNVARCHAR(java.sql.Types.LONGNVARCHAR, true),
    NCLOB(java.sql.Types.NCLOB, false),
    SQLXML(java.sql.Types.SQLXML, true);

    SqlTypes(final int jdbcSqlType, final boolean hasSize) {
        this.jdbcSqlType = new int[] { jdbcSqlType };
        this.hasSize = hasSize;
    }

    SqlTypes(final int[] jdbcSqlTypeArray, final boolean hasSize) {
        this.jdbcSqlType = jdbcSqlTypeArray;
        this.hasSize = hasSize;

        Arrays.sort(this.jdbcSqlType);
    }

    public int[] getJdbcSqlType() {
        return jdbcSqlType;
    }

    public boolean isHasSize() {
        return hasSize;
    }

    public static SqlTypes getSqlTypeByJdbcSqlType(final int jdbcSqlType) {
        for (SqlTypes typeToCheck : SqlTypes.values()) {
            if (Arrays.binarySearch(typeToCheck.getJdbcSqlType(), jdbcSqlType) >= 0) {
                return typeToCheck;
            }
        }
        throw new IllegalArgumentException("Given jdbcSqlType=" + jdbcSqlType + " is incorrect.");
    }

    private int[] jdbcSqlType;
    private boolean hasSize;
}
