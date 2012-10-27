package org.jtalks.poulpe.logic.databasebackup.impl.dto;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

/**
 * TableRow container is used in the Collections so each object of TableRow should support comparing operations under
 * itself.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class TableRowTest {
    private final TableRow tableRowA01 = new TableRow().addColumn("id", "A").addColumn("name", "nameA");
    private final TableRow tableRowA02 = new TableRow().addColumn("id", "A").addColumn("name", "nameA");
    private final TableRow tableRowA03 = new TableRow().addColumn("id", "A").addColumn("name", "nameA");
    private final TableRow tableRowB01 = new TableRow().addColumn("id", "B").addColumn("name", "nameB");

    /**
     * Two different instances of TableRow should not be equal.
     */
    @Test
    public void twoNotEqualTableRowAreNotEqual() {
        assertFalse(tableRowA01.equals(tableRowB01));
    }

    /**
     * Two the same instances of TableRow should be equal.
     */
    @Test
    public void twoEqualTableRowAreEqual() {
        assertTrue(tableRowA01.equals(tableRowA02));
    }

    /**
     * Tests Java Equals Contract.
     */
    @Test
    public void equalsContractTest() {
        assertTrue(tableRowA01.equals(tableRowA01), "Reflexive");

        assertTrue(tableRowA01.equals(tableRowA02), "Equal Symmetric");
        assertTrue(tableRowA02.equals(tableRowA01), "Equal Symmetric");

        assertFalse(tableRowA01.equals(tableRowB01), "Not Equal Symmetric");
        assertFalse(tableRowB01.equals(tableRowA01), "Not Equal Symmetric");

        assertTrue(tableRowA01.equals(tableRowA03), "Transitive");
        assertTrue(tableRowA03.equals(tableRowA01), "Transitive");

        assertFalse(tableRowA01.equals(null), "Null value should return false");
    }
}
