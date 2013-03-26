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
        "email"
})
@XmlRootElement(name = "user", namespace = "http://www.jtalks.org/namespaces/1.0")
public class User {

    @XmlElement(namespace = "http://www.jtalks.org/namespaces/1.0", required = true)
    private String username;

    @XmlElement(namespace = "http://www.jtalks.org/namespaces/1.0", required = true)
    private String passwordHash;

    @XmlElement(namespace = "http://www.jtalks.org/namespaces/1.0", required = true)
    private String email;

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
}
