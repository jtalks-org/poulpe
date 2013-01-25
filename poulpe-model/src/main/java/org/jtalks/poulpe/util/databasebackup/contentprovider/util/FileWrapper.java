/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jtalks.poulpe.util.databasebackup.contentprovider.util;

import java.io.File;
import java.io.IOException;

/**
 * Class is designed to use for calling static methods on java.io.File. So they (methods) can be mocked and class under
 * test which uses them can be tested.
 */
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
