package org.jtalks.poulpe.util.databasebackup.dbdump;

import java.sql.SQLException;

import org.testng.Assert;
import org.testng.annotations.Test;

public class HeaderAndDataAwareCommandTest {
    @Test(groups = { "databasebackup" })
    public void executeMethodCallsForHeaderAndData() throws SQLException {
        final String getHeaderReturn = "getHeader method";
        final String getDataReturn = "getData method";

        HeaderAndDataAwareCommand testObject = new HeaderAndDataAwareCommand() {

            @Override
            protected StringBuilder getHeader() {
                return new StringBuilder(getHeaderReturn);
            }

            @Override
            protected StringBuilder getData() {
                return new StringBuilder(getDataReturn);
            }

        };

        String expectedOutput = getHeaderReturn + HeaderAndDataAwareCommand.LINEFEED + getDataReturn
                + HeaderAndDataAwareCommand.LINEFEED + HeaderAndDataAwareCommand.LINEFEED;

        Assert.assertEquals(expectedOutput, testObject.execute().toString());
    }
}
