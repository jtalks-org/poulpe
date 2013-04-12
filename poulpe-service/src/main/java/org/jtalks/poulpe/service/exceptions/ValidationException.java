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

import java.util.List;

/**
 *  When there are errors of validation, this exception should be thrown.
 */
public class ValidationException extends Exception{

    private List<String> messages;

    /**
     * Construct
     *
     * @param message  the message of the exception
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Construct
     *
     * @param message the message of the exception
     * @param cause the attached exception
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Construct
     *
     * @param messages the list of messages
     */
    public ValidationException(List<String> messages){
        this.messages = messages;
    }

    /**
     * Construct
     *
     * @param messages the list of messages
     * @param cause the attached exception
     */
    public ValidationException(List<String> messages, Throwable cause) {
        super(cause);
        this.messages = messages;
    }

    /**
     * @return the list of messages of the exception
     */
    public List<String> getMessages() {
        return messages;
    }

    /**
     * @param messages the list of messages of the exception
     */
    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
