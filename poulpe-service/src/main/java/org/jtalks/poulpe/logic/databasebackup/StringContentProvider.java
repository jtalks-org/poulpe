package org.jtalks.poulpe.logic.databasebackup;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * The class provides given string as a content.
 * 
 * @author Evgeny Surovtsev
 *
 */
public class StringContentProvider implements ContentProvider {
	/**
	 * @param content String which represents the content
	 */
	StringContentProvider(final String content) {
		this.content = content;
	}
	
	@Override
	public InputStream getContent() throws FileDownloadException {
		InputStream contentInputStream = null; 
		try {
			contentInputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new FileDownloadException(e);
		}
		return contentInputStream;
	}

	@Override
	public String getMimeContentType() {
		return MIME_CONTENT_TYPE;
	}

	@Override
	public String getContentFileNameExt() {
		return FILE_NAME_EXT;
	}

	private String content;
	
	private static String MIME_CONTENT_TYPE = "text/plain";  
	private static String FILE_NAME_EXT = ".sql";
}
