package org.jtalks.poulpe.logic.databasebackup.impl.dto;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.testng.annotations.Test;

/**
 * TablePrimaryKey container is used in the Collections so each object of TablePrimaryKey should support comparing
 * operations under itself.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class TablePrimaryKeyTest {

    /**
     * Two different instances of TablePrimaryKey should not be equal.
     */
    @Test
    public void twoNotEqualTablePrimaryKeyAreNotEquals() {
        assertFalse(new TablePrimaryKey("column1").equals(new TablePrimaryKey("column2")));
    }

    /**
     * Two the same instances of TablePrimaryKey should be equal.
     */
    @Test
    public void twoEqualTablePrimaryKeyAreEquals() {
        assertTrue(new TablePrimaryKey("column1").equals(new TablePrimaryKey("column1")));
    }

    /**
     * There should be no possibility to construct object without providing Primary Key Column.
     */
    @Test
    public void tablePrimaryKeyShouldBeInitializedInConstructor() {
        try {
            @SuppressWarnings("unused")
            TablePrimaryKey tablePrimaryKey = new TablePrimaryKey(null);
            fail("Exception expected");
        } catch (NullPointerException e) {
            // do nothing - the exception is expected.
        }

    }

    /**
     * Tests Java Equals Contract.
     */
    @Test
    public void equalsContractTest() {
        TablePrimaryKey tablePrimaryKey1 = new TablePrimaryKey("column");
        TablePrimaryKey tablePrimaryKey2 = new TablePrimaryKey("column");
        TablePrimaryKey tablePrimaryKey3 = new TablePrimaryKey("column");
        TablePrimaryKey tablePrimaryKey4 = new TablePrimaryKey("differentColumn");

        assertTrue(tablePrimaryKey1.equals(tablePrimaryKey1), "Reflexive");

        assertEquals(tablePrimaryKey1.equals(tablePrimaryKey2), tablePrimaryKey2.equals(tablePrimaryKey1),
                "Equal Symmetric");
        assertEquals(tablePrimaryKey1.equals(tablePrimaryKey4), tablePrimaryKey4.equals(tablePrimaryKey1),
                "Not Equal Symmetric");

        assertEquals(tablePrimaryKey1.equals(tablePrimaryKey3), tablePrimaryKey3.equals(tablePrimaryKey1),
                "Transitive");

        assertFalse(tablePrimaryKey1.equals(null), "Null value should return false");
    }
}
