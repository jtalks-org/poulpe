package org.jtalks.poulpe.logic.databasebackup.impl;

import javax.sql.DataSource;

import org.testng.annotations.Test;

public class TableDataInformationProviderTest {
    @Test(enabled=false)
    public void getTableNamesListTest() {
        DataSource dataSource = null;
        TableDataInformationProvider testObject = new TableDataInformationProvider(dataSource);

    }
}
