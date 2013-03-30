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

import org.jtalks.poulpe.util.databasebackup.persistence.SqlTypes;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RowTest {
    private ColumnMetaData idColumn;
    private ColumnMetaData nameColumn;
    private Row sutA1, sutA2, sutA3, sutB1, revertedSutA;

    @BeforeClass
    protected void setUp() {
        idColumn = ColumnMetaData.getInstance("id", SqlTypes.INT);
        nameColumn = ColumnMetaData.getInstance("name", SqlTypes.VARCHAR);
    }

    @BeforeMethod
    public void beforeMethod() {
        sutA1 = new Row().addCell(idColumn, "A").addCell(nameColumn, "nameA");
        sutA2 = new Row().addCell(idColumn, "A").addCell(nameColumn, "nameA");
        sutA3 = new Row().addCell(idColumn, "A").addCell(nameColumn, "nameA");

        revertedSutA = new Row().addCell(nameColumn, "nameA").addCell(idColumn, "A");
        sutB1 = new Row().addCell(idColumn, "A").addCell(nameColumn, "nameB");
    }

    @Test
    public void twoNotEqualTableRowAreNotEqual() {
        assertFalse(sutA1.equals(sutB1));
        assertFalse(sutA1.hashCode() == sutB1.hashCode());
    }

    @Test
    public void twoEqualTableRowAreEqual() {
        assertEquals(sutA1, sutA2);
        assertEquals(sutA1.hashCode(), sutA2.hashCode());
    }

    @Test
    public void twoEqualTableRowWithDifferentColumnsOrderAreEqual() {
        assertEquals(sutA1, revertedSutA);
    }

    @Test
    public void equalsContractTest() {
        assertEquals(sutA1, sutA1, "Reflexive");

        assertEquals(sutA1, sutA2, "Equal Symmetric");
        assertEquals(sutA2, sutA1, "Equal Symmetric");

        assertFalse(sutA1.equals(sutB1), "Not Equal Symmetric");
        assertFalse(sutB1.equals(sutA1), "Not Equal Symmetric");

        assertEquals(sutA1, sutA2, "Transitive");
        assertEquals(sutA1, sutA3, "Transitive");
        assertEquals(sutA2, sutA3, "Transitive");

        assertFalse(sutA1.equals(null), "Null value should return false");
    }

    @Test
    public void setAndGetRowData() {
        // Cell data = sutA1.getCellList().get(0);
        // Assert.assertEquals(data.getColumnName(), idColumn.getName());
        // Assert.assertEquals(data.getSqlType(), idColumn.getType());
        // Assert.assertEquals(data.getColumnData(), "A");
    }
}
