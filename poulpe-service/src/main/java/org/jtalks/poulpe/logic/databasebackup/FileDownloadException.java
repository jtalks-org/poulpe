package org.jtalks.poulpe.logic.databasebackup;

/**
 * The Exception is thrown when any error occurs while preparing content or sending file to downloading.
 * 
 * @author Evgeny Surovtsev
 *
 */
public class FileDownloadException extends Exception {
	public FileDownloadException(Exception e) {
		super();
		message = e.getMessage();
	}
	/**
	 * Provide the error message about thrown exception. 
	 */
	@Override
	public String getMessage() {
		return message;
	}
	
	private String message;
	private static final long serialVersionUID = 1L;
}
