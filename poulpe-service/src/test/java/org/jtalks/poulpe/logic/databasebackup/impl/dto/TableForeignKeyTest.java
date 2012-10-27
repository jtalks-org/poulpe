package org.jtalks.poulpe.logic.databasebackup.impl.dto;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.testng.annotations.Test;

/**
 * TableForeignKey container is used in the Collections so each object of TableForeignKey should support comparing
 * operations under itself.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class TableForeignKeyTest {

    /**
     * Two different instances of TableForeignKey should not be equal.
     */
    @Test
    public void twoNotEqualTableForeignKeyAreNotEquals() {
        assertFalse(new TableForeignKey("column1", "param1", "param2", "param3").equals(
                new TableForeignKey("column2", "param1", "param2", "param3")));
    }

    /**
     * Two the same instances of TableForeignKey should be equal.
     */
    @Test
    public void twoEqualTableForeignKeyAreEquals() {
        assertTrue(new TableForeignKey("column1", "param1", "param2", "param3").equals(
                new TableForeignKey("column1", "param1", "param2", "param3")));
    }

    /**
     * There should be no possibility to construct object without providing Primary Key Column.
     */
    @Test
    public void tableForeignKeyShouldBeInitializedInConstructor() {
        try {
            @SuppressWarnings("unused")
            TableForeignKey tableForeignKey = new TableForeignKey(null, null, null, null);
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
        TableForeignKey tableForeignKey1 = new TableForeignKey("column", "param1", "param2", "param3");
        TableForeignKey tableForeignKey2 = new TableForeignKey("column", "param1", "param2", "param3");
        TableForeignKey tableForeignKey3 = new TableForeignKey("column", "param1", "param2", "param3");
        TableForeignKey tableForeignKey4 = new TableForeignKey("differentColumn", "param1", "param2", "param3");

        assertTrue(tableForeignKey1.equals(tableForeignKey1), "Reflexive");

        assertEquals(tableForeignKey1.equals(tableForeignKey2), tableForeignKey2.equals(tableForeignKey1),
                "Equal Symmetric");
        assertEquals(tableForeignKey1.equals(tableForeignKey4), tableForeignKey4.equals(tableForeignKey1),
                "Not Equal Symmetric");

        assertEquals(tableForeignKey1.equals(tableForeignKey3), tableForeignKey3.equals(tableForeignKey1),
                "Transitive");

        assertFalse(tableForeignKey1.equals(null), "Null value should return false");
    }
}
