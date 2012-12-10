package org.jtalks.poulpe.util.databasebackup.logic.impl;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import junit.framework.Assert;

import org.jtalks.poulpe.util.databasebackup.domain.ColumnMetaData;
import org.jtalks.poulpe.util.databasebackup.domain.ForeignKey;
import org.jtalks.poulpe.util.databasebackup.domain.UniqueKey;
import org.jtalks.poulpe.util.databasebackup.persistence.DbTable;
import org.jtalks.poulpe.util.databasebackup.persistence.SqlTypes;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Tests that SqlTableStructureDump provides correct table structure.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class SqlTableStructureDumpTest {
    /**
     * Checks that SqlTableStructureDump forms expected result for an example data. There could be differences in final
     * formating of SQL statement which don't have an influence on the resulting behaviour of SQL Server so for being
     * able to check the correctness of the result we remove all spaces from expected SQL and actual SQL before
     * comparing them.
     * 
     * @throws SQLException
     *             must never happen.
     */
    @Test
    public void tableStructureTest() throws SQLException {
        List<ColumnMetaData> tableStructure =
                Lists.newArrayList(new ColumnMetaData("id", SqlTypes.INT).setComment("comment for the column"));
        Set<UniqueKey> uniqueKeySet = Sets.newHashSet(new UniqueKey("column", "column"));
        Set<ForeignKey> foreignKeySet =
                Sets.newHashSet(new ForeignKey("fkTable", "fkColumn", "pkTable", "pkColumn"));
        Map<String, String> commonParams = Maps.newHashMap();
        commonParams.put("Collation", "UTF-8");

        DbTable dbTable = Mockito.mock(DbTable.class);
        Mockito.when(dbTable.getTableName()).thenReturn("tableName");
        Mockito.when(dbTable.getStructure()).thenReturn(tableStructure);
        Mockito.when(dbTable.getPrimaryKeySet()).thenReturn(uniqueKeySet);
        Mockito.when(dbTable.getUniqueKeySet()).thenReturn(uniqueKeySet);
        Mockito.when(dbTable.getForeignKeySet()).thenReturn(foreignKeySet);
        Mockito.when(dbTable.getCommonParameterMap()).thenReturn(commonParams);

        String expectedSql = "CREATE TABLE `tableName` ("
                + "`id` INT NOT NULL  COMMENT 'comment for the column', "
                + "PRIMARY KEY (`column`), "
                + "CONSTRAINT `column` UNIQUE (`column`), KEY `fkTable`(`fkColumn`), "
                + "CONSTRAINT `fkTable` FOREIGN KEY (`fkColumn`) REFERENCES `pkTable`(`pkColumn`)) "
                + "Collation=UTF-8;";

        SqlTableStructureDump testObject = new SqlTableStructureDump(dbTable);
        Assert.assertEquals(expectedSql.replaceAll(" ", ""),
                removeGapsAndEmptyStringsAndSqlComments(testObject.dumpStructure().toString()).replaceAll(" ", ""));
    }

    /**
     * Checks that if column has a defaultValue and type of the column is text based then the default value will be
     * quoted.
     * 
     * @throws SQLException
     *             must never happen.
     */
    @Test
    public void defaultValuesShouldBeQuotedForStrings() throws SQLException {
        List<ColumnMetaData> tableStructure = Lists.newArrayList(
                new ColumnMetaData("id", SqlTypes.INT).setDefaultValue("5"),
                new ColumnMetaData("name", SqlTypes.VARCHAR).setSize(32).setDefaultValue("defaultValue"),
                new ColumnMetaData("time", SqlTypes.TIMESTAMP).setDefaultValue("CURRENT_TIMESTAMP"));
        Set<UniqueKey> uniqueKeySet = Sets.newHashSet(new UniqueKey("column", "column"));
        Set<ForeignKey> foreignKeySet =
                Sets.newHashSet(new ForeignKey("fkTable", "fkColumn", "pkTable", "pkColumn"));
        Map<String, String> commonParams = Maps.newHashMap();
        commonParams.put("Collation", "UTF-8");

        DbTable dbTable = Mockito.mock(DbTable.class);
        Mockito.when(dbTable.getTableName()).thenReturn("tableName");
        Mockito.when(dbTable.getStructure()).thenReturn(tableStructure);
        Mockito.when(dbTable.getPrimaryKeySet()).thenReturn(uniqueKeySet);
        Mockito.when(dbTable.getUniqueKeySet()).thenReturn(uniqueKeySet);
        Mockito.when(dbTable.getForeignKeySet()).thenReturn(foreignKeySet);
        Mockito.when(dbTable.getCommonParameterMap()).thenReturn(commonParams);

        String expectedSql = "CREATE TABLE `tableName` ("
                + "`id` INT NOT NULL DEFAULT 5, "
                + "`name` VARCHAR (32) NOT NULL DEFAULT 'defaultValue', "
                + "`time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "
                + "PRIMARY KEY (`column`), "
                + "CONSTRAINT `column` UNIQUE (`column`), KEY `fkTable`(`fkColumn`), "
                + "CONSTRAINT `fkTable` FOREIGN KEY (`fkColumn`) REFERENCES `pkTable`(`pkColumn`)) "
                + "Collation=UTF-8;";

        SqlTableStructureDump testObject = new SqlTableStructureDump(dbTable);
        Assert.assertEquals(expectedSql.replaceAll(" ", ""),
                removeGapsAndEmptyStringsAndSqlComments(testObject.dumpStructure().toString()).replaceAll(" ", ""));
    }

    /**
     * SqlTableDataDump generates many comments in the resulting SQL which can be unpredictable and we don't want to
     * test them. So before checking the result we're removing all comments and empty lines from the result SQL.
     * 
     * @param actualOutput
     *            a raw SQL statement before cleaning it up
     * @return pure SQL statement without comments or empty lines
     */
    private String removeGapsAndEmptyStringsAndSqlComments(final String actualOutput) {
        Iterator<String> iterator = Iterables.filter(
                Splitter.on(SqlTableDumpUtil.LINEFEED).omitEmptyStrings().trimResults().split(actualOutput),
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
}
