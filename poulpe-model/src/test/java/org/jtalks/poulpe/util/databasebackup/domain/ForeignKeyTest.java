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
 * ForeignKey is mainly used in Collections so first of all we need to test it's comparison abilities.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class ForeignKeyTest {
    /**
     * Prepare SUTs for test.
     */
    @BeforeMethod
    public void beforeMethod() {
        sut1 = new ForeignKey("column1", "param1", "param2", "param3");
        sut2 = new ForeignKey("column1", "param1", "param2", "param3");
        differentSut = new ForeignKey("column2", "param1", "param2", "param3");
    }

    /**
     * Two different instances of ForeignKey should not be equal.
     */
    @Test(groups = { "databasebackup" })
    public void twoNotEqualTableForeignKeyAreNotEquals() {
        Assert.assertFalse(sut1.equals(differentSut));
        Assert.assertFalse(sut1.hashCode() == differentSut.hashCode());
    }

    /**
     * Two the same instances of ForeignKey should be equal.
     */
    @Test(groups = { "databasebackup" })
    public void twoEqualTableForeignKeyAreEquals() {
        Assert.assertEquals(sut1, sut2);
        Assert.assertEquals(sut1.hashCode(), sut2.hashCode());
    }

    /**
     * There should be no possibility to construct object without providing Primary Key Column.
     */
    @Test(groups = { "databasebackup" }, expectedExceptions = NullPointerException.class)
    public void tableForeignKeyShouldBeInitializedInConstructor() {
        @SuppressWarnings("unused")
        ForeignKey tableForeignKey = new ForeignKey(null, null, null, null);
    }

    private ForeignKey sut1, sut2, differentSut;
}
