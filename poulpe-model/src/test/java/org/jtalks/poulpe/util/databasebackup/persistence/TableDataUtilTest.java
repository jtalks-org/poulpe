package org.jtalks.poulpe.util.databasebackup.persistence;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TableDataUtilTest {
    @Test
    public void getSqlValueQuotedStringTest() {
        Assert.assertEquals(TableDataUtil.getSqlValueQuotedString("tableValue"), "'tableValue'");
    }

    @Test
    public void escapeSqlInGetSqlValueQuotedStringTest() {
        Assert.assertEquals(TableDataUtil.getSqlValueQuotedString("table's value"), "'table''s value'");
    }

    @Test
    public void getSqlColumnQuotedStringTest() {
        Assert.assertEquals(TableDataUtil.getSqlColumnQuotedString("tableName"), "`tableName`");
    }

    @Test
    public void escapeBackslashSymbolInGetSqlValueQuotedStringTest() {
        Assert.assertEquals(TableDataUtil.getSqlValueQuotedString("\\"), "'\\\\'");
        Assert.assertEquals(TableDataUtil.getSqlValueQuotedString("table\\Value"), "'table\\\\Value'");
    }
}
