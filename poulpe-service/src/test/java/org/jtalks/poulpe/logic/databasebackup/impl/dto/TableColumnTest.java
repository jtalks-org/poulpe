package org.jtalks.poulpe.logic.databasebackup.impl.dto;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * TableColumn container is used in the Collections so each object of TableColumn should support comparing operations
 * under itself.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class TableColumnTest {

    /**
     * Two different instances of TableColumn should not be equal.
     */
    @Test
    public void twoNotEqualTableColumnAreNotEquals() {
        assertFalse(typeA01.equals(typeB01));
    }

    /**
     * Two the same instances of TableColumn should be equal.
     */
    @Test
    public void twoEqualTableColumnAreEquals() {
        assertTrue(typeA01.equals(typeA02));
    }

    /**
     * There should be no possibility to construct object without providing Name and Type values.
     */
    @Test
    public void tableColumnShouldBeInitializedInConstructor() {
        try {
            @SuppressWarnings("unused")
            TableColumn tableColumn = new TableColumn(null, null);
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
        assertTrue(typeA01.equals(typeA01), "Reflexive");

        assertEquals(typeA01.equals(typeA02), typeA02.equals(typeA01), "Equal Symmetric");
        assertEquals(typeA01.equals(typeB01), typeB01.equals(typeA01), "Not Equal Symmetric");

        assertEquals(typeA01.equals(typeA03), typeA03.equals(typeA01), "Transitive");

        assertFalse(typeA01.equals(null), "Null value should return false");
    }

    /**
     * Method creates different TableColumn objects for testing.
     */
    @BeforeMethod
    private void setUpTableColumns() {
        typeA01 = new TableColumn("columnTypeA", SqlTypes.INT)
                .setSize(32).setAutoincrement(true).setDefaultValue("5").setNullable(false);
        typeA02 = new TableColumn("columnTypeA", SqlTypes.INT)
                .setSize(32).setAutoincrement(true).setDefaultValue("5").setNullable(false);
        typeA03 = new TableColumn("columnTypeA", SqlTypes.INT)
                .setSize(32).setAutoincrement(true).setDefaultValue("5").setNullable(false);

        typeB01 = new TableColumn("columnTypeB", SqlTypes.VARCHAR)
                .setSize(8).setAutoincrement(false).setDefaultValue("").setNullable(true);
    }

    private TableColumn typeA01, typeA02, typeA03;
    private TableColumn typeB01;
}
