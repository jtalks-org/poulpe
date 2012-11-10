package org.jtalks.poulpe.model.databasebackup.dto;

import static org.testng.Assert.*;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.jtalks.poulpe.model.databasebackup.SqlTypes;
import org.jtalks.poulpe.model.databasebackup.dto.Row;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * TableRow container is used in the Collections so each object of TableRow should support comparing operations under
 * itself.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class RowTest {
    private ColumnMetaData idColumn;
    private ColumnMetaData nameColumn;

    @BeforeClass
    private void setUp() {
        idColumn = new ColumnMetaData("id", SqlTypes.INT);
        nameColumn = new ColumnMetaData("name", SqlTypes.VARCHAR);
    }

    /**
     * Two different instances of TableRow should not be equal.
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
     * Two the same instances of TableRow should be equal.
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
     * Two the same instances of TableRow should be equal.
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
     * Two the same instances of TableRow should be equal.
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
     * Two the same instances of TableRow should be equal.
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

    @Test
    public void setAndGetTableRowData() {
        Row testObject = new Row().addCell(new Cell(idColumn, "A"));
        Cell data = testObject.getCellList().get(0);
        assertEquals(data.getColumnName(), "id");
        assertEquals(data.getSqlType(), SqlTypes.INT);
        assertEquals(data.getColumnData(), "A");
    }
}
