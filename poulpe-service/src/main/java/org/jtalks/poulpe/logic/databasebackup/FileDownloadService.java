package org.jtalks.poulpe.logic.databasebackup;

import java.io.InputStream;

/**
 * The class is used to download a file in the browser. For performing this the class uses next objects
 * which should be set via setters:
 * <ul>
 * <li>{@link ContentProvider} - the interface gets content for the downloading file and probably performs 
 * an additional transformation under it like compressing it and other such activities.
 * <li>{@link FileDownloader} - the class is responsible for sending the file to the browser.</li>
 * </ul>
 * 
 * @author Evgeny Surovtsev
 *
 */
public class FileDownloadService {
	/**
	 * This is the main method of the class. The method performs the actual file preparing using the provided 
	 * {@link ContentProvider} and sends the resulting file to the browser using the provided {@link FileDownloader}.
	 *  
	 * @throws FileDownloadException is thrown in case of any errors during file preparing or sending it to the browser.
	 */
	public void PerformFileDownload() throws FileDownloadException {
		try {
			InputStream content = contentProvider.getContent();
			
			fileDownloader.setMimeContentType(contentProvider.getMimeContentType());
			fileDownloader.setContentFileName(contentFileNameWithoutExt + contentProvider.getContentFileNameExt());
			fileDownloader.download(content);
		} catch (Exception e) {
			throw new FileDownloadException(e);
		}
	}
	
	// injected
	private ContentProvider contentProvider;
	/**
	 * Injects a Content provider object which will be used for creating content which later will be send to a browser.
	 * 
	 * @param contentProvider An instance of a Content Provider 
	 */
	public void setContentProvider(final ContentProvider contentProvider) {
		this.contentProvider = contentProvider;
	}
	// injected 
	private FileDownloader fileDownloader;
	/**
	 * Injects a File Downloader object which "knows" how to send previously prepared content to a user's browser.
	 * 
	 * @param fileDownloader The instance of the FileDownloader
	 */
	public void setFileDownloader(final FileDownloader fileDownloader) {
		this.fileDownloader = fileDownloader;
	}
	// injected
	private String contentFileNameWithoutExt;
	/**
	 * Injects a local filename for prepared content in the shape without filename extension (Ex. "jtalks").
	 * 
	 * @param contentFileNameWithoutExt String which represents the filename without extension.
	 */
	public void setContentFileNameWithoutExt(final String contentFileNameWithoutExt) {
		this.contentFileNameWithoutExt = contentFileNameWithoutExt;
	}
}
