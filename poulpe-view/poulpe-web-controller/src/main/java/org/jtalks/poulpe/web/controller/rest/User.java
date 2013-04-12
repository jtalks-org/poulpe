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
package org.jtalks.poulpe.web.controller.rest;

import javax.xml.bind.annotation.*;

/**
 * Java class for information of user complex type.
 *
 * @author Mikhail Zaitsev
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "user", namespace = "http://www.jtalks.org/namespaces/1.0" , propOrder = {
        "username",
        "passwordHash",
        "email",
        "firstName",
        "lastName"
})
@XmlRootElement(name = "user", namespace = "http://www.jtalks.org/namespaces/1.0")
public class User {

    @XmlElement(namespace = "http://www.jtalks.org/namespaces/1.0", required = true)
    private String username;

    @XmlElement(namespace = "http://www.jtalks.org/namespaces/1.0", required = true)
    private String passwordHash;

    @XmlElement(namespace = "http://www.jtalks.org/namespaces/1.0", required = true)
    private String email;

    @XmlElement(namespace = "http://www.jtalks.org/namespaces/1.0")
    private String firstName;

    @XmlElement(namespace = "http://www.jtalks.org/namespaces/1.0")
    private String lastName;

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
