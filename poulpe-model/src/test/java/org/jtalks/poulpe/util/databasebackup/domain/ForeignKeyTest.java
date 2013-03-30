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

public class ForeignKeyTest {
    private ForeignKey sut1, sut2, differentSut;

    @BeforeMethod
    public void beforeMethod() {
        sut1 = new ForeignKey("column1", "param1", "param2", "param3");
        sut2 = new ForeignKey("column1", "param1", "param2", "param3");
        differentSut = new ForeignKey("column2", "param1", "param2", "param3");
    }

    @Test
    public void twoNotEqualTableForeignKeyAreNotEquals() {
        assertFalse(sut1.equals(differentSut));
        assertFalse(sut1.hashCode() == differentSut.hashCode());
    }

    @Test
    public void twoEqualTableForeignKeyAreEquals() {
        assertEquals(sut1, sut2);
        assertEquals(sut1.hashCode(), sut2.hashCode());
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void tableForeignKeyShouldBeInitializedInConstructor() {
        ForeignKey tableForeignKey = new ForeignKey(null, null, null, null);
    }
}
