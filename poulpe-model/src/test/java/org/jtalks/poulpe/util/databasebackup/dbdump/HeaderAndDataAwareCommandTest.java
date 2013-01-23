package org.jtalks.poulpe.util.databasebackup.dbdump;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HeaderAndDataAwareCommandTest {
    @BeforeMethod
    public void beforeMethod() {
        sut = new HeaderAndDataAwareCommand() {

            @Override
            protected StringBuilder getHeader() {
                return new StringBuilder(getHeaderReturn);
            }

            @Override
            protected StringBuilder getData() {
                return new StringBuilder(getDataReturn);
            }

        };
    }

    @Test
    public void executeHeaderAndDataAwareCommand() throws SQLException, IOException {
        String expectedOutput = getHeaderReturn
                + HeaderAndDataAwareCommand.LINEFEED
                + getDataReturn
                + HeaderAndDataAwareCommand.LINEFEED
                + HeaderAndDataAwareCommand.LINEFEED;
        OutputStream output = new ByteArrayOutputStream();

        sut.execute(output);

        Assert.assertEquals(expectedOutput, output.toString());
    }

    private HeaderAndDataAwareCommand sut;
    private final String getHeaderReturn = "getHeader method";
    private final String getDataReturn = "getData method";
}
