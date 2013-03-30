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
package org.jtalks.poulpe.util.databasebackup.domain;

import static org.testng.Assert.*;

import org.jtalks.poulpe.util.databasebackup.persistence.SqlTypes;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ColumnMetaDataTest {
    private ColumnMetaData sut1, sut2, differentSut;

    @BeforeMethod
    public void beforeMethod() {
        sut1 = ColumnMetaData.getInstance("columnTypeA", SqlTypes.INT).setSize(32).setAutoincrement(true)
                .setDefaultValue("5")
                .setNullable(false).setComment("true - sid is user, false - sid is a granted authority");
        sut2 = ColumnMetaData.getInstance("columnTypeA", SqlTypes.INT).setSize(32).setAutoincrement(true)
                .setDefaultValue("5")
                .setNullable(false).setComment("true - sid is user, false - sid is a granted authority");
        differentSut = ColumnMetaData.getInstance("columnTypeB", SqlTypes.VARCHAR).setSize(8).setAutoincrement(false)
                .setDefaultValue("").setNullable(true);
    }

    @Test
    public void twoNotEqualTableColumnAreNotEquals() {
        assertFalse(sut1.equals(differentSut));
        assertFalse(sut1.hashCode() == differentSut.hashCode());
    }

    @Test
    public void twoEqualTableColumnAreEquals() {
        assertEquals(sut1, sut2);
        assertEquals(sut1.hashCode(), sut2.hashCode());
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void constructThrowsExceptionWhenColumnTypeIsNull() {
        ColumnMetaData tableColumn = ColumnMetaData.getInstance("columnTypeA", null);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void constructThrowsExceptionWhenColumnNaemIsNull() {
        ColumnMetaData tableColumn = ColumnMetaData.getInstance(null, SqlTypes.INT);
    }
}
