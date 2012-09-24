package org.jtalks.poulpe.logic.databasebackup;

import java.io.InputStream;

/**
 * This is an interface for providing content which later will be shipped to a user as a file via standard browser's 
 * download functionality. The interface defines common methods such as get a content or return MIME type or 
 * file based extension for provided content.
 * 
 * @author Evgeny Surovtsev
 *
 */
public interface ContentProvider {
	/**
	 * The "main" method which prepares content for downloading. Besides of actual preparing also a post processing
	 * under the content like compressing it before sending to the browser and other such activities can be performed
	 * here.  
	 * 
	 * @return Content for sending to a user's browser as a file to download.
	 */
	InputStream getContent() throws FileDownloadException;
	/**
	* The method returns a MIME type for the content which was/will be provided by {@link #getContent()} method.
	* This method is used for setting MIME type in browser's response.
	*
	* @return String which represents the defined MIME content type.
	*/
	public String getMimeContentType();
	/**
	 * The method returns file extension for the content which was/will be provided by {@link #getContent()} method.
	 * This needs for forming final filename which will be passed to the browser so browser will be adviced under 
	 * which name downloaded file should be saved. 
	 * 
	 * @return Filename extension like ".html" with the dot prefixed. 
	 */
	String getContentFileNameExt();
}