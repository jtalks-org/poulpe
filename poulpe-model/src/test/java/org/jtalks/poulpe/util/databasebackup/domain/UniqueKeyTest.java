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

import static org.testng.Assert.*;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UniqueKeyTest {
    private UniqueKey sutA1, sutA2, sutB1;

    @BeforeMethod
    public void beforeMethod() {
        sutA1 = new UniqueKey("indexName1", "column1");
        sutA2 = new UniqueKey("indexName1", "column1");

        sutB1 = new UniqueKey("indexName2", "column2");
    }

    @Test
    public void twoNotEqualUniqueKeyAreNotEquals() {
        assertFalse(sutA1.equals(sutB1));
        assertFalse(sutA1.hashCode() == sutB1.hashCode());
    }

    @Test
    public void twoEqualUniqueKeyAreEquals() {
        assertEquals(sutA1, sutA2);
        assertEquals(sutA1.hashCode(), sutA2.hashCode());
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void uniqueKeyShouldBeInitializedInConstructor() {
        UniqueKey tablePrimaryKey = new UniqueKey(null, "column");
    }
}
