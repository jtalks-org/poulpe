package org.jtalks.poulpe.util.databasebackup.dbdump.mysqlsyntax;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

import org.jtalks.poulpe.util.databasebackup.TestUtil;
import org.jtalks.poulpe.util.databasebackup.domain.ColumnMetaData;
import org.jtalks.poulpe.util.databasebackup.domain.Row;
import org.jtalks.poulpe.util.databasebackup.persistence.DbTable;
import org.jtalks.poulpe.util.databasebackup.persistence.SqlTypes;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;

public class TableDataDumpCommandTest {
    @BeforeMethod
    public void beforeMethod() throws SQLException {
        dbTable = Mockito.mock(DbTable.class);
        List<Row> rows = ImmutableList.of(
                new Row().addCell(ColumnMetaData.getInstance("id", SqlTypes.INT), 1),
                new Row().addCell(ColumnMetaData.getInstance("name", SqlTypes.VARCHAR), "value"),
                new Row().addCell(ColumnMetaData.getInstance("nullColumn", SqlTypes.VARCHAR), null));
        Mockito.when(dbTable.getData()).thenReturn(rows);
        Mockito.when(dbTable.getTableName()).thenReturn("tableName");

        sut = new TableDataDumpCommand(dbTable);
    }

    @Test
    public void executeTableDataDumpCommand() throws SQLException, IOException {
        String expectedDataDump = "INSERT INTO `tableName` (id) VALUES (1);"
                + "INSERT INTO `tableName` (name) VALUES ('value');"
                + "INSERT INTO `tableName` (nullColumn) VALUES (NULL);";
        OutputStream output = new ByteArrayOutputStream();

        sut.execute(output);

        Assert.assertEquals(TestUtil.makeLowerAndRemoveSpaces(expectedDataDump),
                TestUtil.makeLowerAndRemoveSpaces(TestUtil.removeEmptyStringsAndSqlComments(output.toString())));
    }

    private DbTable dbTable;
    private TableDataDumpCommand sut;
}
