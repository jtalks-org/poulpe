package org.jtalks.poulpe.model.databasebackup.dto;

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
public class UniqueKeyTest {

    /**
     * Two different instances of TablePrimaryKey should not be equal.
     */
    @Test
    public void twoNotEqualTablePrimaryKeyAreNotEquals() {
        UniqueKey testObject1 = new UniqueKey("column1");
        UniqueKey testObject2 = new UniqueKey("column2");
        assertFalse(testObject1.equals(testObject2));
    }

    /**
     * Two the same instances of TablePrimaryKey should be equal.
     */
    @Test
    public void twoEqualTablePrimaryKeyAreEquals() {
        UniqueKey testObject1 = new UniqueKey("column1");
        UniqueKey testObject2 = new UniqueKey("column1");
        assertTrue(testObject1.equals(testObject2));
    }

    /**
     * There should be no possibility to construct object without providing Primary Key Column.
     */
    @Test
    @SuppressWarnings("unused")
    public void tablePrimaryKeyShouldBeInitializedInConstructor() {
        try {
            UniqueKey tablePrimaryKey = new UniqueKey(null);
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
        UniqueKey testObject1 = new UniqueKey("column1");
        assertTrue(testObject1.equals(testObject1), "Reflexive");

        UniqueKey testObject2 = new UniqueKey("column1");
        assertTrue(testObject1.equals(testObject2), "Equal Symmetric");
        assertTrue(testObject2.equals(testObject1), "Equal Symmetric");

        UniqueKey differentTestObject = new UniqueKey("differentColumn");
        assertFalse(testObject1.equals(differentTestObject), "Not Equal Symmetric");
        assertFalse(differentTestObject.equals(testObject1), "Not Equal Symmetric");

        UniqueKey testObject3 = new UniqueKey("column1");
        assertTrue(testObject1.equals(testObject2), "Transitive");
        assertTrue(testObject1.equals(testObject3), "Transitive");
        assertTrue(testObject2.equals(testObject3), "Transitive");

        assertFalse(testObject1.equals(null), "Null value should return false");
    }
}
