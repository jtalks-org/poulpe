package org.jtalks.poulpe.util.databasebackup.dbdump.mysqlsyntax;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jtalks.poulpe.util.databasebackup.TestUtil;
import org.jtalks.poulpe.util.databasebackup.dbdump.mysql.CreateTableCommand;
import org.jtalks.poulpe.util.databasebackup.domain.ColumnMetaData;
import org.jtalks.poulpe.util.databasebackup.domain.UniqueKey;
import org.jtalks.poulpe.util.databasebackup.persistence.DbTable;
import org.jtalks.poulpe.util.databasebackup.persistence.SqlTypes;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public class CreateTableCommandTest {
    @BeforeMethod
    public void beforeMethod() throws SQLException {
        dbTable = Mockito.mock(DbTable.class);
        Mockito.when(dbTable.getTableName()).thenReturn("tableName");

        List<ColumnMetaData> tableStructure = ImmutableList.of(
                ColumnMetaData.getInstance("id", SqlTypes.INT).setAutoincrement(true).setComment("comment"),
                ColumnMetaData.getInstance("name", SqlTypes.VARCHAR).setSize(32).setNullable(true)
                        .setDefaultValue("defaultValue"));
        Mockito.when(dbTable.getStructure()).thenReturn(tableStructure);

        Set<UniqueKey> primaryKeys = ImmutableSet.of(new UniqueKey("IDKEY", "id"));
        Mockito.when(dbTable.getPrimaryKeySet()).thenReturn(primaryKeys);

        Set<UniqueKey> uniqueKeys = ImmutableSet.of(new UniqueKey("KEY_NAME", "name"));
        Mockito.when(dbTable.getUniqueKeySet()).thenReturn(uniqueKeys);

        Map<String, String> commonParameters = ImmutableMap.of(
                "COLLATE", "utf-8",
                "AUTO_INCREMENT", "5");
        Mockito.when(dbTable.getCommonParameters()).thenReturn(commonParameters);
    }

    @Test
    public void executeCreateTableCommand() throws SQLException, IOException {
        CreateTableCommand sut = new CreateTableCommand(dbTable);
        String expectedCreateTableStatement = "CREATE TABLE `tableName` ("
                + "`id` INT NOT NULL AUTO_INCREMENT COMMENT 'comment',"
                + "`name` VARCHAR(32) NULL DEFAULT 'defaultValue',"
                + "PRIMARY KEY (`id`),"
                + "CONSTRAINT KEY_NAME UNIQUE (`name`)"
                + ") COLLATE=utf-8 AUTO_INCREMENT=5;";
        OutputStream output = new ByteArrayOutputStream();

        sut.execute(output);

        Assert.assertEquals(
                TestUtil.makeLowerAndRemoveSpaces(TestUtil.removeEmptyStringsAndSqlComments(output.toString())),
                TestUtil.makeLowerAndRemoveSpaces(expectedCreateTableStatement));
    }

    private DbTable dbTable;
    private static final String LINEFEED = "\n";
}
