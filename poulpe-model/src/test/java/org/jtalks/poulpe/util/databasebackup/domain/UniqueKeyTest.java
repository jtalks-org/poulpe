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

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * UniqueKey is mainly used in Collections so first of all we need to test it's comparison abilities.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class UniqueKeyTest {
    /**
     * Prepares SUTs for testing.
     */
    @BeforeMethod
    public void beforeMethod() {
        sutA1 = new UniqueKey("indexName1", "column1");
        sutA2 = new UniqueKey("indexName1", "column1");

        sutB1 = new UniqueKey("indexName2", "column2");
    }

    /**
     * Two different instances of UniqueKey should not be equal.
     */
    @Test(groups = { "databasebackup" })
    public void twoNotEqualUniqueKeyAreNotEquals() {
        Assert.assertFalse(sutA1.equals(sutB1));
        Assert.assertFalse(sutA1.hashCode() == sutB1.hashCode());
    }

    /**
     * Two the same instances of UniqueKey should be equal.
     */
    @Test(groups = { "databasebackup" })
    public void twoEqualUniqueKeyAreEquals() {
        Assert.assertEquals(sutA1, sutA2);
        Assert.assertEquals(sutA1.hashCode(), sutA2.hashCode());
    }

    /**
     * There should be no possibility to construct object without providing UniqueKey Column.
     */
    @Test(groups = { "databasebackup" }, expectedExceptions = NullPointerException.class)
    public void uniqueKeyShouldBeInitializedInConstructor() {
        @SuppressWarnings("unused")
        UniqueKey tablePrimaryKey = new UniqueKey(null, "column");
    }

    private UniqueKey sutA1, sutA2, sutB1;
}
