package org.jtalks.poulpe.util.databasebackup.dbdump;
//package org.jtalks.poulpe.util.databasebackup.dbdump;
//
//import java.util.LinkedList;
//import java.util.Queue;
//
//import junit.framework.Assert;
//
//import org.jtalks.poulpe.util.databasebackup.contentprovider.impl.SqlTableDumpUtil;
//import org.jtalks.poulpe.util.databasebackup.dbdump.DbDumpCommand;
//import org.jtalks.poulpe.util.databasebackup.dbdump.x_TableDumpCommandExecutor;
//import org.testng.annotations.Test;
//
//import com.google.common.collect.Lists;
//
///**
// * Tests that TableDumpCommandExecutor class performs given Commands in the correct order and returns expected result of
// * given commands executing.
// * 
// * @author Evgeny Surovtsev
// * 
// */
//public class x_TableDumpCommandExecutorTest {
//    /**
//     * When given a null instead of command Queue the class must throw an IllegalArgumentException.
//     */
//    @Test(expectedExceptions = IllegalArgumentException.class)
//    public void nullCommandListGeneratesException() {
//        @SuppressWarnings("unused")
//        x_TableDumpCommandExecutor testObject = new x_TableDumpCommandExecutor(null);
//    }
//
//    /**
//     * When given an empty list of commands the class must return an Empty result.
//     */
//    @Test
//    public void emptyCommandListReturnsEmptyResult() {
//        x_TableDumpCommandExecutor testObject = new x_TableDumpCommandExecutor(new LinkedList<DbDumpCommand>());
//        Assert.assertEquals("", testObject.execute().toString());
//    }
//
//    /**
//     * When given a command the class must return expected result.
//     */
//    @Test
//    public void returnsExpectedResultForOneCommand() {
//        Queue<DbDumpCommand> commandList = Lists.newLinkedList();
//        DbDumpCommand simpleCommand = createTableDumpCommand("simpleCommand");
//        commandList.add(simpleCommand);
//
//        String expectedOutput = simpleCommand.getHeader().toString()
//                + SqlTableDumpUtil.LINEFEED
//                + simpleCommand.getData().toString()
//                + SqlTableDumpUtil.LINEFEED + SqlTableDumpUtil.LINEFEED;
//
//        x_TableDumpCommandExecutor testObject = new x_TableDumpCommandExecutor(commandList);
//        Assert.assertEquals(expectedOutput, testObject.execute().toString());
//    }
//
//    /**
//     * When given commands in certain order the result must be arranged in the same order of command execution.
//     */
//    @Test
//    public void commandExecutorKeepsCorrectOrder() {
//        Queue<DbDumpCommand> commandList = Lists.newLinkedList();
//        DbDumpCommand firstCommand = createTableDumpCommand("firstCommand");
//        DbDumpCommand secondCommand = createTableDumpCommand("secondCommand");
//        DbDumpCommand thirdCommand = createTableDumpCommand("thirdCommand");
//        commandList.add(firstCommand);
//        commandList.add(secondCommand);
//        commandList.add(thirdCommand);
//
//        String expectedOutput =
//                // First command results
//                firstCommand.getHeader().toString()
//                        + SqlTableDumpUtil.LINEFEED
//                        + firstCommand.getData().toString()
//                        + SqlTableDumpUtil.LINEFEED + SqlTableDumpUtil.LINEFEED
//                        // Second command results
//                        + secondCommand.getHeader().toString()
//                        + SqlTableDumpUtil.LINEFEED
//                        + secondCommand.getData().toString()
//                        + SqlTableDumpUtil.LINEFEED + SqlTableDumpUtil.LINEFEED
//                        // Third command results
//                        + thirdCommand.getHeader().toString()
//                        + SqlTableDumpUtil.LINEFEED
//                        + thirdCommand.getData().toString()
//                        + SqlTableDumpUtil.LINEFEED + SqlTableDumpUtil.LINEFEED;
//
//        x_TableDumpCommandExecutor testObject = new x_TableDumpCommandExecutor(commandList);
//        Assert.assertEquals(expectedOutput, testObject.execute().toString());
//    }
//
//    /**
//     * Method creates a new exemplar of TableDumpCommand with certain id every time the method is called. Created
//     * TableDumpCommand instance will use the given id for formating its output.
//     * 
//     * @param label
//     *            id of the TableDumpCommand instance.
//     * @return a newly created instance of TableDumpCommand.
//     */
//    private DbDumpCommand createTableDumpCommand(final String label) {
//        return new DbDumpCommand() {
//
//            @Override
//            public StringBuilder getHeader() {
//                return new StringBuilder(label + ":getHeader result");
//            }
//
//            @Override
//            public StringBuilder getData() {
//                return new StringBuilder(label + ":getData result");
//            }
//        };
//    }
// }
