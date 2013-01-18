package org.jtalks.poulpe.util.databasebackup.dbdump.mysqlsyntax;

import java.sql.SQLException;
import java.util.Set;

import org.jtalks.poulpe.util.databasebackup.TestUtil;
import org.jtalks.poulpe.util.databasebackup.domain.ForeignKey;
import org.jtalks.poulpe.util.databasebackup.persistence.DbTable;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;

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
    public void executeAddForeignKeysCommand() throws SQLException {
        AddForeignKeysCommand sut = new AddForeignKeysCommand(dbTable);
        String expectedAlterTableStatement = "ALTER TABLE `tableName` "
                + "ADD FOREIGN KEY (`fkColumnName`) REFERENCES `pkTableName`(`pkColumnName`);";

        Assert.assertEquals(
                TestUtil.makeLowerAndRemoveSpaces(TestUtil.removeEmptyStringsAndSqlComments(sut.execute().toString())),
                TestUtil.makeLowerAndRemoveSpaces(expectedAlterTableStatement));
    }

    private DbTable dbTable;
}
