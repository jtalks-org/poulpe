package org.jtalks.poulpe.util.databasebackup.exceptions;

/**
 * The exception is throws when data base does not contain any table, so there is nothing to export. in the case of
 * working system the situation is unreal and usually tells about critical errors during working with database.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class DataBaseDoesntContainTablesException extends FileDownloadException {
    private static final long serialVersionUID = 1L;
}
