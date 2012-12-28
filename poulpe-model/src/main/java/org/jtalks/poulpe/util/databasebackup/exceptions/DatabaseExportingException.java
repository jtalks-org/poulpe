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
package org.jtalks.poulpe.util.databasebackup.exceptions;

/**
 * The Exception is thrown when a error during exporting data from the database happens.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class DatabaseExportingException extends FileDownloadException {
    /**
     * Constructor creates an instance and uses error message from given Exception.
     * 
     * @param e
     *            An exception which is used to construct FileDownloadException.
     */
    public DatabaseExportingException(final Exception e) {
        super(e);
    }

    private static final long serialVersionUID = 1L;
}
