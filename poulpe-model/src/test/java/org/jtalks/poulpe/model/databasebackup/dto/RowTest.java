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
package org.jtalks.poulpe.model.databasebackup.dto;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.jtalks.poulpe.model.databasebackup.SqlTypes;
import org.testng.annotations.BeforeClass;
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
    @BeforeClass
    private void setUp() {
        idColumn = new ColumnMetaData("id", SqlTypes.INT);
        nameColumn = new ColumnMetaData("name", SqlTypes.VARCHAR);
    }

    /**
     * Two different instances of Row should not be equal.
     */
    @Test
    public void twoNotEqualTableRowAreNotEqual() {
        Row testObject1 = new Row()
                .addCell(new Cell(idColumn, "A"))
                .addCell(new Cell(nameColumn, "nameA"));
        Row testObject2 = new Row()
                .addCell(new Cell(idColumn, "A"))
                .addCell(new Cell(nameColumn, "nameB"));
        assertFalse(testObject1.equals(testObject2));
    }

    /**
     * Two the same instances of Row should be equal.
     */
    @Test
    public void twoEqualTableRowAreEqual() {
        Row testObject1 = new Row()
                .addCell(new Cell(idColumn, "A"))
                .addCell(new Cell(nameColumn, "nameA"));
        Row testObject2 = new Row()
                .addCell(new Cell(idColumn, "A"))
                .addCell(new Cell(nameColumn, "nameA"));
        assertTrue(testObject1.equals(testObject2));
    }

    /**
     * Two the same instances of Row should be equal.
     */
    @Test
    public void twoEqualTableRowWithDifferentColumnsOrderAreEqual() {
        Row testObject1 = new Row()
                .addCell(new Cell(idColumn, "A"))
                .addCell(new Cell(nameColumn, "nameA"));
        Row testObject2 = new Row()
                .addCell(new Cell(nameColumn, "nameA"))
                .addCell(new Cell(idColumn, "A"));
        assertTrue(testObject1.equals(testObject2));
    }

    /**
     * Two the same instances of Row should be equal.
     */
    @Test
    public void addingTwoColumnsWithTheSamenameIsForbidden() {
        Row testObject = new Row().addCell(new Cell(idColumn, "A"));
        try {
            testObject.addCell(new Cell(new ColumnMetaData("id", SqlTypes.VARCHAR), "nameA"));
            fail("Adding 2 columns with the same name is forbidden.");
        } catch (IllegalArgumentException e) {
            // doing nothing - exception is expected.
        }
    }

    /**
     * Two the same instances of Row should be equal.
     */
    @Test
    public void addingTwoColumnsWithTheSamenameIsForbidden2() {
        Row testObject = new Row().addCell(new Cell(idColumn, "A"));
        try {
            testObject.addCell(new Cell(new ColumnMetaData("id", SqlTypes.VARCHAR), "nameA"));
            fail("Adding 2 columns with the same name is forbidden.");
        } catch (IllegalArgumentException e) {
            // doing nothing - exception is expected.
        }
    }

    /**
     * Tests Java Equals Contract.
     */
    @Test
    public void equalsContractTest() {
        Row testObject1 = new Row()
                .addCell(new Cell(idColumn, "A"))
                .addCell(new Cell(nameColumn, "nameA"));
        assertTrue(testObject1.equals(testObject1), "Reflexive");

        Row testObject2 = new Row()
                .addCell(new Cell(idColumn, "A"))
                .addCell(new Cell(nameColumn, "nameA"));
        assertTrue(testObject1.equals(testObject2), "Equal Symmetric");
        assertTrue(testObject2.equals(testObject1), "Equal Symmetric");

        Row differentTestObject = new Row()
                .addCell(new Cell(idColumn, "A"))
                .addCell(new Cell(new ColumnMetaData("defferentName", SqlTypes.INT), "differentValue"));
        assertFalse(testObject1.equals(differentTestObject), "Not Equal Symmetric");
        assertFalse(differentTestObject.equals(testObject1), "Not Equal Symmetric");

        Row testObject3 = new Row()
                .addCell(new Cell(idColumn, "A"))
                .addCell(new Cell(nameColumn, "nameA"));
        assertTrue(testObject1.equals(testObject2), "Transitive");
        assertTrue(testObject1.equals(testObject3), "Transitive");
        assertTrue(testObject2.equals(testObject3), "Transitive");

        assertFalse(testObject1.equals(null), "Null value should return false");
    }

    /**
     * Checking if setters and getters for the Row are working.
     */
    @Test
    public void setAndGetRowData() {
        Row testObject = new Row().addCell(new Cell(idColumn, "A"));
        Cell data = testObject.getCellList().get(0);
        assertEquals(data.getColumnName(), "id");
        assertEquals(data.getSqlType(), SqlTypes.INT);
        assertEquals(data.getColumnData(), "A");
    }
}
