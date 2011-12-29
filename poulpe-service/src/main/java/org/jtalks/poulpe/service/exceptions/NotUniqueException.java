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
package org.jtalks.poulpe.service.exceptions;

/**
 * Exception for cases when some entity field should be unique but it isn't.
 * @author Pavel Vervenko
 * 
 * @deprecated Use jsr303 validation and its ValidationException
 */
@Deprecated
public class NotUniqueException extends Exception {

    /**
     * Generated uid
     */
    private static final long serialVersionUID = -1301487402231289042L;

    /**
     * Default constructor.
     *
     * {@link Exception}
     */
    public NotUniqueException() {
    }

    /**
     * Create exception with specific message.
     *
     * @param message exception message
     */
    public NotUniqueException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public NotUniqueException(Throwable cause) {
        super(cause);
    }
}
