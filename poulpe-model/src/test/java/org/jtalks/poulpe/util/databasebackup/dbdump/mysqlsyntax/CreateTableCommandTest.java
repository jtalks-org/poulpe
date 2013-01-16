package org.jtalks.poulpe.util.databasebackup.dbdump.mysqlsyntax;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.jtalks.poulpe.util.databasebackup.domain.ColumnMetaData;
import org.jtalks.poulpe.util.databasebackup.domain.UniqueKey;
import org.jtalks.poulpe.util.databasebackup.persistence.DbTable;
import org.jtalks.poulpe.util.databasebackup.persistence.SqlTypes;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class CreateTableCommandTest {
    @BeforeMethod
    public void beforeMethod() throws SQLException {
        dbTable = Mockito.mock(DbTable.class);
        Mockito.when(dbTable.getTableName()).thenReturn("tableName");

        List<ColumnMetaData> tableStructure = Lists.newArrayList(
                new ColumnMetaData("id", SqlTypes.INT),
                new ColumnMetaData("name", SqlTypes.VARCHAR));
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
    public void CreateTableCommandExecuteTest() throws SQLException {
        CreateTableCommand sut = new CreateTableCommand(dbTable);
        String expectedCreateTableStatement = "CREATE TABLE `tableName` ("
                + "`id` INT NOT NULL,"
                + "`name` VARCHAR NOT NULL,"
                + "PRIMARY KEY (`id`),"
                + "CONSTRAINT KEY_NAME UNIQUE (`name`)"
                + ") COLLATE=utf-8 AUTO_INCREMENT=5;";

        Assert.assertEquals(makeLowerAndRemoveSpaces(removeEmptyStringsAndSqlComments(sut.execute().toString())),
                makeLowerAndRemoveSpaces(expectedCreateTableStatement));
    }

    private String makeLowerAndRemoveSpaces(final String str) {
        return str.toLowerCase().replaceAll("\\s", "");
    }

    private String removeEmptyStringsAndSqlComments(final String actualOutput) {
        Iterator<String> iterator = Iterables.filter(
                Splitter.on(LINEFEED).omitEmptyStrings().trimResults().split(actualOutput),
                new Predicate<String>() {
                    @Override
                    public boolean apply(@Nullable final String arg) {
                        return arg != null && !"--".equals(arg.substring(0, 2));
                    }
                }).iterator();

        StringBuilder result = new StringBuilder();
        while (iterator.hasNext()) {
            result.append(iterator.next());
        }
        return result.toString();
    }

    private DbTable dbTable = null;
    private static final String LINEFEED = "\n";
}
