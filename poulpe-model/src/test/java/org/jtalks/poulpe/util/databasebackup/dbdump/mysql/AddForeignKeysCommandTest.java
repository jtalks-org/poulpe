package org.jtalks.poulpe.util.databasebackup.dbdump.mysql;

import com.google.common.collect.ImmutableSet;
import org.jtalks.poulpe.util.databasebackup.TestUtil;
import org.jtalks.poulpe.util.databasebackup.domain.ForeignKey;
import org.jtalks.poulpe.util.databasebackup.persistence.DbTable;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Set;

public class AddForeignKeysCommandTest {
    @BeforeMethod
    public void beforeMethod() throws SQLException {
        dbTable = Mockito.mock(DbTable.class);
        Mockito.when(dbTable.getTableName()).thenReturn("tableName");

        Set<ForeignKey> foreignKeys = ImmutableSet.of(
                new ForeignKey("fkTableName", "fkColumnName", "pkTableName", "pkColumnName"));
        Mockito.when(dbTable.getForeignKeySet()).thenReturn(foreignKeys);
    }

    @Test
    public void executeAddForeignKeysCommand() throws SQLException, IOException {
        AddForeignKeysCommand sut = new AddForeignKeysCommand(dbTable);
        String expectedAlterTableStatement = "ALTER TABLE `tableName` "
                + "ADD FOREIGN KEY (`fkColumnName`) REFERENCES `pkTableName`(`pkColumnName`);";
        OutputStream output = new ByteArrayOutputStream();

        sut.execute(output);

        Assert.assertEquals(
                TestUtil.makeLowerAndRemoveSpaces(TestUtil.removeEmptyStringsAndSqlComments(output.toString())),
                TestUtil.makeLowerAndRemoveSpaces(expectedAlterTableStatement));
    }

    private DbTable dbTable;
}
