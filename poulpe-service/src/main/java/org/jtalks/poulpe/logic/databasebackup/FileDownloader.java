package org.jtalks.poulpe.logic.databasebackup;

import java.io.InputStream;

/**
 * An abstract class which performs file downloading process by forcing a browser to start getting the file.
 * 
 * @author Evgeny Surovtsev
 *
 */
public abstract class FileDownloader {
	/**
	 * The method performs pushing input stream as a file to a browser's download process using predefined MIME Content
	 * Type (see {@link #setMimeContentType(String)}) and Content Filename (see {@link #setContentFileName(String)}). 
	 * 
	 * @param content InputStream object which will be pushed to downloading.
	 */
	abstract protected void download(InputStream content) throws FileDownloadException;

	// injected
	private String mimeContentType;
	/**
	 * The method sets a MIME type for the content (Ex. "text/plain"). 
	 * 
	 * @param mimeContentType String representation of the MIME content type.
	 */
	public void setMimeContentType(String mimeContentType) {
		this.mimeContentType= mimeContentType; 
	}
	protected String getMimeContentType() {
		return mimeContentType; 
	}
	// injected
	private String contentFileName;
	/**
	 * 
	 * @param contentFileName The filename which will be used for storing content on the local disk for a user 
	 * (ex. "jtalks.sql").
	 */
	public void setContentFileName(String contentFileName) {
		this.contentFileName = contentFileName;
	}
	protected String getContentFileName() {
		return contentFileName;
	}
}
