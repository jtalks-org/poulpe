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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.jtalks.poulpe.util.databasebackup.persistence.SqlTypes;
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
    protected void setUp() {
        idColumn = new ColumnMetaData("id", SqlTypes.INT);
        nameColumn = new ColumnMetaData("name", SqlTypes.VARCHAR);
    }

    /**
     * Two different instances of Row should not be equal.
     */
    @Test
    public void twoNotEqualTableRowAreNotEqual() {
        Row testObject1 = createRowA();
        Row testObject2 = createRowB();
        assertFalse(testObject1.equals(testObject2));
    }

    /**
     * Two the same instances of Row should be equal.
     */
    @Test
    public void twoEqualTableRowAreEqual() {
        Row testObject1 = createRowA();
        Row testObject2 = createRowA();
        assertTrue(testObject1.equals(testObject2));
    }

    /**
     * Two the same instances of Row should be equal.
     */
    @Test
    public void twoEqualTableRowWithDifferentColumnsOrderAreEqual() {
        Row testObject1 = createRowA();
        Row testObject2 = createRevertedRowA();
        assertTrue(testObject1.equals(testObject2));
    }

    /**
     * If there is a Cell in the Row you cannot add to the Row another one with the same name.
     */
    @Test
    public void addingTwoColumnsWithTheSameNameIsForbidden() {
        Row testObject = createRowA();
        Cell theSameCell = testObject.getCellList().get(0);
        try {
            testObject.addCell(theSameCell);
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
        Row testObject1, testObject2, testObject3, differentTestObject;

        testObject1 = createRowA();
        assertTrue(testObject1.equals(testObject1), "Reflexive");

        testObject1 = createRowA();
        testObject2 = createRowA();
        assertTrue(testObject1.equals(testObject2), "Equal Symmetric");
        assertTrue(testObject2.equals(testObject1), "Equal Symmetric");

        testObject1 = createRowA();
        differentTestObject = createRowB();
        assertFalse(testObject1.equals(differentTestObject), "Not Equal Symmetric");
        assertFalse(differentTestObject.equals(testObject1), "Not Equal Symmetric");

        testObject1 = createRowA();
        testObject2 = createRowA();
        testObject3 = createRowA();
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
        Row testObject = createRowA();
        Cell data = testObject.getCellList().get(0);
        assertEquals(data.getColumnName(), idColumn.getName());
        assertEquals(data.getSqlType(), idColumn.getType());
        assertEquals(data.getColumnData(), "A");
    }

    /**
     * Creates and returns a "RowA" instance of Row. Method creates a new instance every time it is called.
     * 
     * @return a newly created "RowA"
     */
    private Row createRowA() {
        return new Row()
                .addCell(new Cell(idColumn, "A"))
                .addCell(new Cell(nameColumn, "nameA"));
    }

    /**
     * Creates and returns a "Reverted RowA" instance of Row which is the same as "RowA" but has different Cells order.
     * Method creates a new instance every time it is called.
     * 
     * @return a newly created "Reverted RowA"
     */
    private Row createRevertedRowA() {
        return new Row()
                .addCell(new Cell(nameColumn, "nameA"))
                .addCell(new Cell(idColumn, "A"));
    }

    /**
     * Creates and returns a "RowB" instance of Row. Method creates a new instance every time it is called.
     * 
     * @return a newly created "RowB"
     */
    private Row createRowB() {
        return new Row()
                .addCell(new Cell(idColumn, "A"))
                .addCell(new Cell(nameColumn, "nameB"));
    }
}
