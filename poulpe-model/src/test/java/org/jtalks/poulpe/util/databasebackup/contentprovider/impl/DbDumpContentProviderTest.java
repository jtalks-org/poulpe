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


public class DbDumpContentProviderTest {
    // private DbTableNameLister dbTableNameLister;
    // private xX_SqlTableDump sqlTableDump;
    // private DataSource dataSource;

    // @BeforeMethod
    // private void setUp() {
    // dbTableNameLister = Mockito.mock(DbTableNameLister.class);
    // sqlTableDump = Mockito.mock(xX_SqlTableDump.class);
    // dataSource = Mockito.mock(DataSource.class);
    // }

    // @Test
    // public void getContentTest() throws FileDownloadException, IOException, SQLException {
    // String expected = "Test string for checking DbDumpContentProvider class";
    //
    // Mockito.when(dbTableNameLister.getIndependentList()).thenReturn(Arrays.asList("tableName"));
    // Mockito.when(dbTableNameLister.getDataSource()).thenReturn(dataSource);
    // Mockito.when(sqlTableDump.getFullDump()).thenReturn(expected);
    //
    // DbDumpContentProvider testObject = new DbDumpContentProvider(dbTableNameLister, sqlTableDump);
    // List<String> outputList =
    // removeEmptyStringsAndSqlComments(new BufferedReader(new InputStreamReader(testObject.getContent())));
    //
    // Mockito.verify(dbTableNameLister).getIndependentList();
    // Mockito.verify(sqlTableDump).setDbTable(Mockito.any(DbTable.class));
    // Mockito.verify(sqlTableDump).getFullDump();
    //
    // Assert.assertEquals(outputList.size(), 1);
    // Assert.assertEquals(outputList.get(0), expected);
    // }

    // @Test
    // public void databaseDoesntContainTablesExceptionThrowingTest() throws SQLException {
    // Mockito.when(dbTableNameLister.getIndependentList()).thenReturn(new ArrayList<String>());
    // try {
    // DbDumpContentProvider testObject = new DbDumpContentProvider(dbTableNameLister, sqlTableDump);
    // testObject.getContent();
    // Assert.fail("DataBaseDoesntContainTablesException was not thrown");
    // } catch (DataBaseDoesntContainTablesException e) {
    // // doing nothing - exception is expected
    // } catch (FileDownloadException e) {
    // Assert.fail("a " + e.getClass().getName() + " was thrown instead of DataBaseDoesntContainTablesException");
    // }
    // }

    // @Test
    // public void databaseExportingExceptionThrowingTest() throws SQLException {
    // Mockito.when(dbTableNameLister.getIndependentList()).thenReturn(Arrays.asList("tableName"));
    // Mockito.when(dbTableNameLister.getDataSource()).thenReturn(dataSource);
    // Mockito.when(sqlTableDump.getFullDump()).thenThrow(new SQLException());
    // try {
    // DbDumpContentProvider testObject = new DbDumpContentProvider(dbTableNameLister, sqlTableDump);
    // testObject.getContent();
    // Assert.fail("DataBaseDoesntContainTablesException was not thrown");
    // } catch (DatabaseExportingException e) {
    // // doing nothing - exception is expected
    // } catch (FileDownloadException e) {
    // Assert.fail("a " + e.getClass().getName() + " was thrown instead of DatabaseExportingException");
    // }
    // }

    /**
     * 
     * @param reader
     *            raw input.
     * @return cleared output without empty strings and SQL comments.
     * @throws IOException
     *             Must never happen.
     */
    // private List<String> removeEmptyStringsAndSqlComments(final BufferedReader reader) throws IOException {
    // List<String> outputList = Lists.newArrayList();
    // String line;
    // while ((line = reader.readLine()) != null) {
    // if (line.trim().length() > 0 && !"--".equals(line.substring(0, 2))) {
    // outputList.add(line);
    // }
    // }
    // reader.close();
    // return outputList;
    // }
}
