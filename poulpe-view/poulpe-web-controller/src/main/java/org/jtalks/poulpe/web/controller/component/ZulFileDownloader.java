package org.jtalks.poulpe.web.controller.component;

import java.io.InputStream;

import org.jtalks.poulpe.logic.databasebackup.FileDownloader;
import org.zkoss.zul.Filedownload;

public class ZulFileDownloader extends FileDownloader {

	@Override
	protected void download(InputStream content) {
		Filedownload.save(content, getMimeContentType(), getContentFileName());
	}

}
