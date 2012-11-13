/**
 * Copyright (C) 2012  JTalks.org Team
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
package org.jtalks.poulpe.logic.databasebackup.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import org.jtalks.poulpe.model.databasebackup.SqlTypes;
import org.jtalks.poulpe.model.databasebackup.dto.Cell;
import org.jtalks.poulpe.model.databasebackup.dto.ColumnMetaData;
import org.jtalks.poulpe.model.databasebackup.dto.Row;
import org.jtalks.poulpe.model.databasebackup.jdbc.DbTable;
import org.testng.annotations.Test;

import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class SqlTableDataDumpTest {

    @Test
    public final void dumpDataTest() throws SQLException {
        String expectedStatement = "INSERT INTO `tableName` "
                + "(stringColumn,intColumn,nullStringColumn,nullIntColumn,stringWithQuoteColumn) "
                + "VALUES ('stringValue',3,NULL,NULL,'stringWith''Quote''');";
        List<Row> actualRows = Lists.newArrayList();
        actualRows.add(new Row()
                .addCell(new Cell(new ColumnMetaData("stringColumn", SqlTypes.VARCHAR), "stringValue"))
                .addCell(new Cell(new ColumnMetaData("intColumn", SqlTypes.INT), new Integer(3)))
                .addCell(new Cell(new ColumnMetaData("nullStringColumn", SqlTypes.VARCHAR), null))
                .addCell(new Cell(new ColumnMetaData("nullIntColumn", SqlTypes.INT), null))
                .addCell(new Cell(new ColumnMetaData("stringWithQuoteColumn", SqlTypes.VARCHAR), "stringWith'Quote'")));

        DbTable dbTable = mock(DbTable.class);
        when(dbTable.getTableName()).thenReturn("tableName");
        when(dbTable.getData()).thenReturn(actualRows);

        SqlTableDataDump testObject = new SqlTableDataDump(dbTable);
        Iterator<String> actualIterator = removeEmptyStringsAndSqlComments(testObject.dumpData().toString());

        assertTrue(actualIterator.hasNext());
        assertEquals(actualIterator.next(), expectedStatement);
        assertFalse(actualIterator.hasNext());
    }

    private Iterator<String> removeEmptyStringsAndSqlComments(final String actualOutput) {
        return Iterables.filter(
                Splitter.on(SqlTableDumpUtil.LINEFEED).omitEmptyStrings().trimResults().split(actualOutput),
                new Predicate<String>() {
                    @Override
                    public boolean apply(@Nullable final String arg) {
                        return arg != null && !"--".equals(arg.substring(0, 2));
                    }
                }).iterator();
    }
}
