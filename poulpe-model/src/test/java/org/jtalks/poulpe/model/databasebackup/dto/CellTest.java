package org.jtalks.poulpe.model.databasebackup.dto;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.jtalks.poulpe.model.databasebackup.SqlTypes;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * TableColumn container is used in the Collections so each object of TableRow should support comparing operations under
 * itself.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class CellTest {
    private ColumnMetaData columnMetaData;
    private ColumnMetaData differentColumnMetaData;

    @BeforeClass
    private void setUp() {
        columnMetaData = new ColumnMetaData("columnName", SqlTypes.VARCHAR);
        differentColumnMetaData = new ColumnMetaData("columnName", SqlTypes.INT);
    }

    /**
     * Two different instances of TableColumnData should not be equal.
     */
    @Test
    public void twoNotEqualCellsAreNotEqual() {
        Cell testObject1 = new Cell(columnMetaData, "columnValue");
        Cell testObject2 = new Cell(differentColumnMetaData, "differentColumnValue");
        assertFalse(testObject1.equals(testObject2), "totally diferent objects");

        testObject1 = new Cell(columnMetaData, "columnValue");
        testObject2 = new Cell(differentColumnMetaData, "columnValue");
        assertFalse(testObject1.equals(testObject2), "only one field differs");

        testObject1 = new Cell(columnMetaData, "columnValue");
        testObject2 = new Cell(differentColumnMetaData, "columnValue");
        assertFalse(testObject1.equals(testObject2), "only one field differs");

        testObject1 = new Cell(columnMetaData, "columnValue");
        testObject2 = new Cell(columnMetaData, "differentColumnValue");
        assertFalse(testObject1.equals(testObject2), "only one field differs");
    }

    @Test
    public void twoEqualCellWithNullValueAreEqual() {
        Cell testObject1 = new Cell(columnMetaData, null);
        Cell testObject2 = new Cell(columnMetaData, null);
        assertEquals(testObject1, testObject2);
    }

    /**
     * Two the same instances of TableColumnData should be equal.
     */
    @Test
    public void twoEqualCellsAreEqual() {
        Cell testObject1 = new Cell(columnMetaData, "columnValue");
        Cell testObject2 = new Cell(columnMetaData, "columnValue");
        assertEquals(testObject1, testObject2);
    }

    /**
     * Check if creating instance with null parameters is forbiden.
     */
    @Test
    @SuppressWarnings("unused")
    public void instanceShouldBeInitializedWithNonNulls() {
        try {
            Cell testObject1 = new Cell(null, "columnValue");
            fail("columnName cannot be null.");
        } catch (NullPointerException e) {
            // do nothing - the exception is expected
        }
        try {
            Cell testObject1 = new Cell(null, null);
            fail("columnValue cannot be null.");
        } catch (NullPointerException e) {
            // do nothing - the exception is expected
        }
    }

    /**
     * Tests Java Equals Contract.
     */
    @Test
    public void equalsContractTest() {
        Cell testObject1 = new Cell(columnMetaData, "columnValue");
        assertTrue(testObject1.equals(testObject1), "Reflexive");

        Cell testObject2 = new Cell(columnMetaData, "columnValue");
        assertTrue(testObject1.equals(testObject2), "Equal Symmetric");
        assertTrue(testObject2.equals(testObject1), "Equal Symmetric");

        Cell diferentTestObject = new Cell(differentColumnMetaData, "columnValue");
        assertFalse(testObject1.equals(diferentTestObject), "Not Equal Symmetric");
        assertFalse(diferentTestObject.equals(testObject1), "Not Equal Symmetric");

        Cell testObject3 = new Cell(columnMetaData, "columnValue");
        assertTrue(testObject1.equals(testObject2), "Transitive");
        assertTrue(testObject1.equals(testObject3), "Transitive");
        assertTrue(testObject2.equals(testObject3), "Transitive");

        assertFalse(testObject2.equals(null), "Null value should return false");
    }
}
