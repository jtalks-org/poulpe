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
 * The Exception is thrown when a error happens during gzipping previously prepared content.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class GzipPackingException extends FileDownloadException {
    /**
     * Initializes an instance of Exception with given Exception as a cause.
     * 
     * @param e
     *            a cause exception.
     */
    public GzipPackingException(final Exception e) {
        super(e);
    }

    private static final long serialVersionUID = -8578677100750727015L;

}
