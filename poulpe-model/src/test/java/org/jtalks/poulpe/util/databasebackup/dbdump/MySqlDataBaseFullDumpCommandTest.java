package org.jtalks.poulpe.util.databasebackup.dbdump;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;
import java.util.Queue;

import junit.framework.Assert;

import org.jtalks.poulpe.util.databasebackup.dbdump.mysqlsyntax.AddForeignKeysCommand;
import org.jtalks.poulpe.util.databasebackup.dbdump.mysqlsyntax.CommonHeaderCommand;
import org.jtalks.poulpe.util.databasebackup.dbdump.mysqlsyntax.CreateTableCommand;
import org.jtalks.poulpe.util.databasebackup.dbdump.mysqlsyntax.TableDataDumpCommand;
import org.jtalks.poulpe.util.databasebackup.persistence.DbTable;
import org.mockito.Mockito;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class MySqlDataBaseFullDumpCommandTest {
    @BeforeMethod
    public void beforeMethod() {
        DbTable dbTable1 = Mockito.mock(DbTable.class);
        DbTable dbTable2 = Mockito.mock(DbTable.class);
        List<DbTable> dbTables = ImmutableList.of(dbTable1, dbTable2);
        sut = new MySqlDataBaseFullDumpCommand(dbTables);
    }

    @Test
    public void dumpCommandQueueContainsAllNeededCommandsInCorrectOrder() throws IllegalAccessException,
            NoSuchFieldException {
        @SuppressWarnings("unchecked")
        Queue<DbDumpCommand> dumpCommandQueue = (Queue<DbDumpCommand>) getAccessibleDumpCommandQueueField().get(sut);

        Assert.assertTrue(dumpCommandQueue.poll() instanceof CommonHeaderCommand);
        Assert.assertTrue(dumpCommandQueue.poll() instanceof CreateTableCommand);
        Assert.assertTrue(dumpCommandQueue.poll() instanceof CreateTableCommand);
        Assert.assertTrue(dumpCommandQueue.poll() instanceof TableDataDumpCommand);
        Assert.assertTrue(dumpCommandQueue.poll() instanceof TableDataDumpCommand);
        Assert.assertTrue(dumpCommandQueue.poll() instanceof AddForeignKeysCommand);
        Assert.assertTrue(dumpCommandQueue.poll() instanceof AddForeignKeysCommand);
        Assert.assertTrue(dumpCommandQueue.isEmpty());
    }

    @Test
    public void executeRunsAllCommandsInCorrectOrder() throws IllegalAccessException, NoSuchFieldException,
            SQLException {
        getAccessibleDumpCommandQueueField().set(sut, getFakeCommandQueue());
        Assert.assertEquals("123", sut.execute().toString());
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
            public StringBuilder execute() throws SQLException {
                return new StringBuilder("1");
            }
        });
        dumpCommandQueue.add(new DbDumpCommand() {

            @Override
            public StringBuilder execute() throws SQLException {
                return new StringBuilder("2");
            }
        });
        dumpCommandQueue.add(new DbDumpCommand() {

            @Override
            public StringBuilder execute() throws SQLException {
                return new StringBuilder("3");
            }
        });
        return dumpCommandQueue;
    }

    private MySqlDataBaseFullDumpCommand sut;
}
