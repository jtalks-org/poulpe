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
package org.jtalks.poulpe.logic.databasebackup;


/**
 * The Exception is thrown when any error occurs while preparing content or sending file to downloading.
 *
 * @author Evgeny Surovtsev
 *
 */
public class FileDownloadException extends Exception {
    /**
     * Constructor creates an instance and uses error message from given Exception.
     *
     * @param e An exception which is used to construct FileDownloadException.
     */
    public FileDownloadException(final Exception e) {
        super();
        message = e.getMessage();
    }

    /**
     * Provide the error message about thrown exception.
     * {@inheritDoc}
     */
    @Override
    public final String getMessage() {
        return message;
    }

    private final String message;
    private static final long serialVersionUID = 1L;
}
