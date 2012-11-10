package org.jtalks.poulpe.model.databasebackup.dto;

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
public class ForeignKeyTest {
    /**
     * Two different instances of TableForeignKey should not be equal.
     */
    @Test
    public void twoNotEqualTableForeignKeyAreNotEquals() {
        ForeignKey testObject1 = new ForeignKey("column1", "param1", "param2", "param3");
        ForeignKey testObject2 = new ForeignKey("column2", "param1", "param2", "param3");
        assertFalse(testObject1.equals(testObject2));
    }

    /**
     * Two the same instances of TableForeignKey should be equal.
     */
    @Test
    public void twoEqualTableForeignKeyAreEquals() {
        ForeignKey testObject1 = new ForeignKey("column1", "param1", "param2", "param3");
        ForeignKey testObject2 = new ForeignKey("column1", "param1", "param2", "param3");
        assertTrue(testObject1.equals(testObject2));
    }

    /**
     * There should be no possibility to construct object without providing Primary Key Column.
     */
    @Test
    @SuppressWarnings("unused")
    public void tableForeignKeyShouldBeInitializedInConstructor() {
        try {
            ForeignKey tableForeignKey = new ForeignKey(null, null, null, null);
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
        ForeignKey testObject1 = new ForeignKey("column", "param1", "param2", "param3");
        assertTrue(testObject1.equals(testObject1), "Reflexive");

        ForeignKey testObject2 = new ForeignKey("column", "param1", "param2", "param3");
        assertTrue(testObject1.equals(testObject2), "Equal Symmetric");
        assertTrue(testObject2.equals(testObject1), "Equal Symmetric");

        ForeignKey differentTestObject = new ForeignKey("differentColumn", "param1", "param2", "param3");
        assertFalse(testObject1.equals(differentTestObject), "Not Equal Symmetric");
        assertFalse(differentTestObject.equals(testObject1), "Not Equal Symmetric");

        ForeignKey testObject3 = new ForeignKey("column", "param1", "param2", "param3");
        assertTrue(testObject1.equals(testObject2), "Transitive");
        assertTrue(testObject1.equals(testObject3), "Transitive");
        assertTrue(testObject2.equals(testObject3), "Transitive");

        assertFalse(testObject1.equals(null), "Null value should return false");
    }
}
