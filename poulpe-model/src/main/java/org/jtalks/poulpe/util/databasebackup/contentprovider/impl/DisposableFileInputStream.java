package org.jtalks.poulpe.util.databasebackup.contentprovider.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * <p>
 * A version of classic FileInputStream which works with temporary files. The file associated with
 * DisposableFileInputStream will be removed automatically when the InputStream is closed.
 * </p>
 * <p>
 * The class is used for downloading a file to a user's machine with org.zkoss.zul.Filedownload which doesn't provide a
 * way to wipe the file after download is completed but automatically closes given it InputStream resource.
 * </p>
 * 
 * @author Evgeny Surovtsev
 * 
 */
class DisposableFileInputStream extends FileInputStream {
    /**
     * Only one constructor with File is available. Creates a FileInputStream by opening a connection to an actual file,
     * the file named by the File object file in the file system.
     * 
     * @param file
     *            the file to be opened for reading.
     * @throws FileNotFoundException
     *             if the file does not exist, is a directory rather than a regular file, or for some other reason
     *             cannot be opened for reading.
     */
    public DisposableFileInputStream(File file) throws FileNotFoundException {
        super(file);
        this.file = file;
    }

    @Override
    public void close() throws IOException {
        super.close();
        if (!file.delete()) {
            throw new IOException("File " + file.getAbsolutePath() + " cannot be deleted.");
        }
    }

    private File file;
}
