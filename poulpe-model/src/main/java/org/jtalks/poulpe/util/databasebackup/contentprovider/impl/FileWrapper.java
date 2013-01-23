package org.jtalks.poulpe.util.databasebackup.contentprovider.impl;

import java.io.File;
import java.io.IOException;

public class FileWrapper {
    /**
     * Creates an empty file in the default temporary-file directory, using the given prefix and suffix to generate its
     * name. Invoking this method is equivalent to invoking {@code File.createTempFile(prefix, suffix, null)}.
     * 
     * @param prefix
     *            The prefix string to be used in generating the file's name; must be at least three characters long.
     * @param suffix
     *            The suffix string to be used in generating the file's name; may be null, in which case the suffix
     *            ".tmp" will be used.
     * @return An abstract pathname denoting a newly-created empty file
     * @throws IOException
     *             If a file could not be created
     */
    public File createTempFile(String prefix, String suffix) throws IOException {
        return File.createTempFile(prefix, suffix);
    }
}
