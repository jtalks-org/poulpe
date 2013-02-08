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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Cell is mainly used in Collections so first of all we need to test it's comparison abilities.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class CellTest {
    private ColumnMetaData columnMetaData;

    /**
     * Sets up a ColumnMetaData which is used in most of the tests and a different ColumnMetaData.
     */
    @BeforeClass(groups = { "databasebackup" })
    protected void beforeClass() {
        columnMetaData = ColumnMetaData.getInstance("columnName", SqlTypes.VARCHAR);
    }

    /**
     * Prepares SUTs for testing.
     */
    @BeforeMethod
    public void beforeMethod() {
        sut1 = new Cell(columnMetaData, "columnValue");
        sut2 = new Cell(columnMetaData, "columnValue");
        differentSut = new Cell(columnMetaData, "differentColumnValue");
    }

    /**
     * Two different instances must be not equal and should have different hash codes.
     */
    @Test(groups = { "databasebackup" })
    public void twoNotEqualCellsAreNotEqualAndShouldHaveDifferentHashCodes() {
        Assert.assertFalse(sut1.equals(differentSut));
        Assert.assertTrue(sut1.hashCode() != differentSut.hashCode());
    }

    /**
     * Two the same instances must be equal and must have equal hash codes.
     */
    @Test(groups = { "databasebackup" })
    public void twoEqualCellsAreEqualAndHaveEqualHashCode() {
        Assert.assertEquals(sut1, sut2);
        Assert.assertEquals(sut1.hashCode(), sut2.hashCode());
    }

    /**
     * Creation new Cell with null ColumnMetaInfo is forbidden.
     */
    @Test(groups = { "databasebackup" }, expectedExceptions = NullPointerException.class)
    public void nullColumnMetaDataThrowsException() {
        @SuppressWarnings("unused")
        Cell sut = new Cell(null, "columnValue");
    }

    /**
     * Creation new Cell with null value is forbidden.
     */
    @Test(groups = { "databasebackup" }, expectedExceptions = NullPointerException.class)
    public void nullColumnValueThrowsException() {
        @SuppressWarnings("unused")
        Cell sut = new Cell(null, null);
    }

    private Cell sut1, sut2, differentSut;
}
