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

import org.jtalks.poulpe.util.databasebackup.persistence.SqlTypes;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * ColumnMetaData is mainly used in Collections so first of all we need to test it's comparison abilities.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class ColumnMetaDataTest {
    /**
     * Prepares SUTs for testing.
     */
    @BeforeMethod
    public void beforeMethod() {
        sut1 = new ColumnMetaData("columnTypeA", SqlTypes.INT).setSize(32).setAutoincrement(true).setDefaultValue("5")
                .setNullable(false).setComment("true - sid is user, false - sid is a granted authority");
        sut2 = new ColumnMetaData("columnTypeA", SqlTypes.INT).setSize(32).setAutoincrement(true).setDefaultValue("5")
                .setNullable(false).setComment("true - sid is user, false - sid is a granted authority");
        differentSut = new ColumnMetaData("columnTypeB", SqlTypes.VARCHAR).setSize(8).setAutoincrement(false)
                .setDefaultValue("").setNullable(true);
    }

    /**
     * Two different instances of ColumnMetaData should not be equal and should not have same hash code.
     */
    @Test(groups = { "databasebackup" })
    public void twoNotEqualTableColumnAreNotEquals() {
        Assert.assertFalse(sut1.equals(differentSut));
        Assert.assertFalse(sut1.hashCode() == differentSut.hashCode());
    }

    /**
     * Two the same instances of ColumnMetaData should be equal and have same hash code.
     */
    @Test(groups = { "databasebackup" })
    public void twoEqualTableColumnAreEquals() {
        Assert.assertEquals(sut1, sut2);
        Assert.assertEquals(sut1.hashCode(), sut2.hashCode());
    }

    /**
     * There should be no possibility to construct object without providing Column Type.
     */
    @Test(groups = { "databasebackup" }, expectedExceptions = NullPointerException.class)
    public void constructThrowsExceptionWhenColumnTypeIsNull() {
        @SuppressWarnings("unused")
        ColumnMetaData tableColumn = new ColumnMetaData("columnTypeA", null);
    }

    /**
     * There should be no possibility to construct object without providing Column Name.
     */
    @Test(groups = { "databasebackup" }, expectedExceptions = NullPointerException.class)
    public void constructThrowsExceptionWhenColumnNaemIsNull() {
        @SuppressWarnings("unused")
        ColumnMetaData tableColumn = new ColumnMetaData(null, SqlTypes.INT);
    }

    private ColumnMetaData sut1, sut2, differentSut;
}
