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
package org.jtalks.poulpe.util.databasebackup.contentprovider.impl;


/**
 * Tests that SqlTableDataDump returns correct SQL statements with given database information.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class SqlTableDataDumpTest {
    /**
     * Passes a certain Row set to the SqlTableDataDump object and checks that it generates correct SQL statement on top
     * of it.
     * 
     * @throws SQLException
     *             Must never happen
     */
    // @Test
    // public final void dumpDataTest() throws SQLException {
    // String expectedStatement = "INSERT INTO `tableName` "
    // + "(stringColumn,intColumn,nullStringColumn,nullIntColumn,stringWithQuoteColumn) "
    // + "VALUES ('stringValue',3,NULL,NULL,'stringWith''Quote''');";
    // List<Row> actualRows = Lists.newArrayList();
    // actualRows.add(new Row()
    // .addCell(new Cell(new ColumnMetaData("stringColumn", SqlTypes.VARCHAR), "stringValue"))
    // .addCell(new Cell(new ColumnMetaData("intColumn", SqlTypes.INT), new Integer(3)))
    // .addCell(new Cell(new ColumnMetaData("nullStringColumn", SqlTypes.VARCHAR), null))
    // .addCell(new Cell(new ColumnMetaData("nullIntColumn", SqlTypes.INT), null))
    // .addCell(new Cell(new ColumnMetaData("stringWithQuoteColumn", SqlTypes.VARCHAR), "stringWith'Quote'")));
    //
    // DbTable dbTable = mock(DbTable.class);
    // when(dbTable.getTableName()).thenReturn("tableName");
    // when(dbTable.getData()).thenReturn(actualRows);
    //
    // SqlTableDataDump testObject = new SqlTableDataDump(dbTable);
    // Iterator<String> actualIterator = removeEmptyStringsAndSqlComments(testObject.dumpData().toString());
    //
    // assertTrue(actualIterator.hasNext());
    // assertEquals(actualIterator.next(), expectedStatement);
    // assertFalse(actualIterator.hasNext());
    // }

    /**
     * SqlTableDataDump generates many comments in the resulting SQL which can be unpredictable and we don't want to
     * test them. So before checking the result we're removing all comments and empty lines from the result SQL.
     * 
     * @param actualOutput
     *            a raw SQL statement before cleaning it up
     * @return pure SQL statement without comments or empty lines
     */
    // private Iterator<String> removeEmptyStringsAndSqlComments(final String actualOutput) {
    // return Iterables.filter(
    // Splitter.on(DbDumpUtil.LINEFEED).omitEmptyStrings().trimResults().split(actualOutput),
    // new Predicate<String>() {
    // @Override
    // public boolean apply(@Nullable final String arg) {
    // return arg != null && !"--".equals(arg.substring(0, 2));
    // }
    // }).iterator();
    // }
}
