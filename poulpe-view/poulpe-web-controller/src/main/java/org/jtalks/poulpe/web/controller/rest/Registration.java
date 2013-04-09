package org.jtalks.poulpe.web.controller.rest;

import javax.xml.bind.annotation.*;

/**
 * Java class for registration complex type.
 *
 * @author Mikhail Zaitsev
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "registration", namespace = "http://www.jtalks.org/namespaces/1.0")
@XmlRootElement(name = "registration", namespace = "http://www.jtalks.org/namespaces/1.0")
public class Registration {

    @XmlElement(namespace = "http://www.jtalks.org/namespaces/1.0", required = false)
    private String errorMessage;

    public Registration() {
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
