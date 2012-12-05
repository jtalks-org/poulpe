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

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.testng.annotations.Test;

/**
 * ForeignKey is mainly used in Collections so first of all we need to test it's comparison abilities.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class ForeignKeyTest {
    /**
     * Two different instances of ForeignKey should not be equal.
     */
    @Test
    public void twoNotEqualTableForeignKeyAreNotEquals() {
        ForeignKey testObject1 = createForeignKeyA();
        ForeignKey testObject2 = createForeignKeyB();
        assertFalse(testObject1.equals(testObject2));
    }

    /**
     * Two the same instances of ForeignKey should be equal.
     */
    @Test
    public void twoEqualTableForeignKeyAreEquals() {
        ForeignKey testObject1 = createForeignKeyA();
        ForeignKey testObject2 = createForeignKeyA();
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
        ForeignKey testObject1, testObject2, testObject3, differentTestObject;

        testObject1 = createForeignKeyA();
        assertTrue(testObject1.equals(testObject1), "Reflexive");

        testObject1 = createForeignKeyA();
        testObject2 = createForeignKeyA();
        assertTrue(testObject1.equals(testObject2), "Equal Symmetric");
        assertTrue(testObject2.equals(testObject1), "Equal Symmetric");

        testObject1 = createForeignKeyA();
        differentTestObject = createForeignKeyB();
        assertFalse(testObject1.equals(differentTestObject), "Not Equal Symmetric");
        assertFalse(differentTestObject.equals(testObject1), "Not Equal Symmetric");

        testObject1 = createForeignKeyA();
        testObject2 = createForeignKeyA();
        testObject3 = createForeignKeyA();
        assertTrue(testObject1.equals(testObject2), "Transitive");
        assertTrue(testObject1.equals(testObject3), "Transitive");
        assertTrue(testObject2.equals(testObject3), "Transitive");

        assertFalse(testObject1.equals(null), "Null value should return false");
    }

    /**
     * Creates and returns an "ForeignKeyA" instance of ForeignKey. Method creates a new instance every time it is
     * called.
     * 
     * @return a newly created "ForeignKeyA"
     */
    private ForeignKey createForeignKeyA() {
        return new ForeignKey("column1", "param1", "param2", "param3");
    }

    /**
     * Creates and returns an "ForeignKeyB" instance of ForeignKey. Method creates a new instance every time it is
     * called.
     * 
     * @return a newly created "ForeignKeyB"
     */
    private ForeignKey createForeignKeyB() {
        return new ForeignKey("column2", "param1", "param2", "param3");
    }
}
