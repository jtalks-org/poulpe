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
 * Row is mainly used in Collections so first of all we need to test it's comparison abilities.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class RowTest {
    private ColumnMetaData idColumn;
    private ColumnMetaData nameColumn;

    /**
     * Sets up 2 wildly used in the tests variables: a sample of id column and a sample of name column.
     */
    @BeforeClass(groups = { "databasebackup" })
    protected void setUp() {
        idColumn = ColumnMetaData.getInstance("id", SqlTypes.INT);
        nameColumn = ColumnMetaData.getInstance("name", SqlTypes.VARCHAR);
    }

    /**
     * Prepares SUTs for testing.
     */
    @BeforeMethod
    public void beforeMethod() {
        sutA1 = new Row().addCell(new Cell(idColumn, "A")).addCell(new Cell(nameColumn, "nameA"));
        sutA2 = new Row().addCell(new Cell(idColumn, "A")).addCell(new Cell(nameColumn, "nameA"));
        sutA3 = new Row().addCell(new Cell(idColumn, "A")).addCell(new Cell(nameColumn, "nameA"));

        revertedSutA = new Row().addCell(new Cell(nameColumn, "nameA")).addCell(new Cell(idColumn, "A"));

        sutB1 = new Row().addCell(new Cell(idColumn, "A")).addCell(new Cell(nameColumn, "nameB"));
    }

    /**
     * Two different instances of Row should not be equal.
     */
    @Test(groups = { "databasebackup" })
    public void twoNotEqualTableRowAreNotEqual() {
        Assert.assertFalse(sutA1.equals(sutB1));
        Assert.assertFalse(sutA1.hashCode() == sutB1.hashCode());
    }

    /**
     * Two the same instances of Row should be equal.
     */
    @Test(groups = { "databasebackup" })
    public void twoEqualTableRowAreEqual() {
        Assert.assertEquals(sutA1, sutA2);
        Assert.assertEquals(sutA1.hashCode(), sutA2.hashCode());
    }

    /**
     * Two the same instances of Row should be equal.
     */
    @Test(groups = { "databasebackup" })
    public void twoEqualTableRowWithDifferentColumnsOrderAreEqual() {
        Assert.assertEquals(sutA1, revertedSutA);
    }

    /**
     * If there is a Cell in the Row you cannot add to the Row another one with the same name.
     */
    @Test(groups = { "databasebackup" }, expectedExceptions = IllegalArgumentException.class)
    public void addingTwoColumnsWithTheSameNameIsForbidden() {
        Cell theSameCell = sutA1.getCellList().get(0);
        sutA1.addCell(theSameCell);
    }

    /**
     * Tests Java Equals Contract.
     */
    @Test(groups = { "databasebackup" })
    public void equalsContractTest() {
        Assert.assertEquals(sutA1, sutA1, "Reflexive");

        Assert.assertEquals(sutA1, sutA2, "Equal Symmetric");
        Assert.assertEquals(sutA2, sutA1, "Equal Symmetric");

        Assert.assertFalse(sutA1.equals(sutB1), "Not Equal Symmetric");
        Assert.assertFalse(sutB1.equals(sutA1), "Not Equal Symmetric");

        Assert.assertEquals(sutA1, sutA2, "Transitive");
        Assert.assertEquals(sutA1, sutA3, "Transitive");
        Assert.assertEquals(sutA2, sutA3, "Transitive");

        Assert.assertFalse(sutA1.equals(null), "Null value should return false");
    }

    /**
     * Checking if setters and getters for the Row are working.
     */
    @Test(groups = { "databasebackup" })
    public void setAndGetRowData() {
        Cell data = sutA1.getCellList().get(0);
        Assert.assertEquals(data.getColumnName(), idColumn.getName());
        Assert.assertEquals(data.getSqlType(), idColumn.getType());
        Assert.assertEquals(data.getColumnData(), "A");
    }

    private Row sutA1, sutA2, sutA3, sutB1, revertedSutA;
}
