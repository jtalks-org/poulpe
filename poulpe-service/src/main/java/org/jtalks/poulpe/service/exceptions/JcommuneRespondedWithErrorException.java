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
 * An exception is thrown when a response has http status, indicating successful treatment did not request.
 * @author Mikhail Zaitsev
 */
public class JcommuneRespondedWithErrorException extends Exception {

    public  JcommuneRespondedWithErrorException(){
        super();
    }

    public JcommuneRespondedWithErrorException(String s) {
        super(s);
    }
    
    public JcommuneRespondedWithErrorException(Exception e) {
        super(e);
    }
}
