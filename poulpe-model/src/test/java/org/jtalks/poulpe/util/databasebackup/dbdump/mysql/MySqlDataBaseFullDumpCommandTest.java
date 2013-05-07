package org.jtalks.poulpe.util.databasebackup.dbdump.mysql;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;
import java.util.Queue;

import org.jtalks.poulpe.util.databasebackup.dbdump.DbDumpCommand;
import org.jtalks.poulpe.util.databasebackup.dbdump.mysql.AddForeignKeysCommand;
import org.jtalks.poulpe.util.databasebackup.dbdump.mysql.CommonHeaderCommand;
import org.jtalks.poulpe.util.databasebackup.dbdump.mysql.CreateTableCommand;
import org.jtalks.poulpe.util.databasebackup.dbdump.mysql.MySqlDataBaseFullDumpCommand;
import org.jtalks.poulpe.util.databasebackup.dbdump.mysql.TableDataDumpCommand;
import org.jtalks.poulpe.util.databasebackup.persistence.DbTable;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class MySqlDataBaseFullDumpCommandTest {
    @BeforeMethod
    public void beforeMethod() {
        DbTable dbTable1 = mock(DbTable.class);
        DbTable dbTable2 = mock(DbTable.class);
        List<DbTable> dbTables = ImmutableList.of(dbTable1, dbTable2);
        sut = new MySqlDataBaseFullDumpCommand(dbTables);
    }

    @Test
    public void dumpCommandQueueContainsAllNeededCommandsInCorrectOrder() throws IllegalAccessException,
            NoSuchFieldException {
        @SuppressWarnings("unchecked")
        Queue<DbDumpCommand> dumpCommandQueue = (Queue<DbDumpCommand>) getAccessibleDumpCommandQueueField().get(sut);

        assertTrue(dumpCommandQueue.poll() instanceof CommonHeaderCommand);
        assertTrue(dumpCommandQueue.poll() instanceof CreateTableCommand);
        assertTrue(dumpCommandQueue.poll() instanceof CreateTableCommand);
        assertTrue(dumpCommandQueue.poll() instanceof TableDataDumpCommand);
        assertTrue(dumpCommandQueue.poll() instanceof TableDataDumpCommand);
        assertTrue(dumpCommandQueue.poll() instanceof AddForeignKeysCommand);
        assertTrue(dumpCommandQueue.poll() instanceof AddForeignKeysCommand);
        assertTrue(dumpCommandQueue.isEmpty());
    }

    @Test
    public void executeRunsAllCommandsInCorrectOrder() throws IllegalArgumentException, IllegalAccessException,
            NoSuchFieldException, SQLException, IOException {
        getAccessibleDumpCommandQueueField().set(sut, getFakeCommandQueue());
        OutputStream output = new ByteArrayOutputStream();

        sut.execute(output);
        assertEquals("123", output.toString());
    }

    /**
     * Returns private accessible dumpCommandQueue field with Java Reflection.
     */
    private Field getAccessibleDumpCommandQueueField() throws NoSuchFieldException {
        Field dumpCommandQueueField = sut.getClass().getDeclaredField("dumpCommandQueue");
        dumpCommandQueueField.setAccessible(true);
        return dumpCommandQueueField;
    }

    /**
     * Returns a Fake CommandQueue which consists of 3 Commands where each Command returns a number from one to three,
     * so total result of executing the whole CommandQueue must be "123".
     */
    private Queue<DbDumpCommand> getFakeCommandQueue() {
        Queue<DbDumpCommand> dumpCommandQueue = Lists.newLinkedList();
        dumpCommandQueue.add(new DbDumpCommand() {

            @Override
            public void execute(OutputStream output) throws SQLException, IOException {
                Writer writer = new PrintWriter(output);
                writer.write("1");
                writer.flush();
            }
        });
        dumpCommandQueue.add(new DbDumpCommand() {

            @Override
            public void execute(OutputStream output) throws SQLException, IOException {
                Writer writer = new PrintWriter(output);
                writer.write("2");
                writer.flush();
            }
        });
        dumpCommandQueue.add(new DbDumpCommand() {

            @Override
            public void execute(OutputStream output) throws SQLException, IOException {
                Writer writer = new PrintWriter(output);
                writer.write("3");
                writer.flush();
            }
        });
        return dumpCommandQueue;
    }

    private MySqlDataBaseFullDumpCommand sut;
}
