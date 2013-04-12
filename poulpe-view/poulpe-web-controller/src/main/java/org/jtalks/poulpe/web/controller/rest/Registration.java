package org.jtalks.poulpe.web.controller.rest;

import javax.xml.bind.annotation.*;
import java.util.List;

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

    @XmlElement(namespace = "http://www.jtalks.org/namespaces/1.0", required = false)
    private List<String> validErrorMessage;

    public Registration() {
    }

    /**
     * Returns any message about error
     *
     * @return any message about error
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets any message of about error
     *
     * @param errorMessage any message about error
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Returns list of messages about validation errors
     *
     * @return list of messages about validation errors
     */
    public List<String> getValidErrorMessage() {
        return validErrorMessage;
    }

    /**
     * Sets list of messages about validation errors
     *
     * @param validErrorMessage list of messages about validation errors
     */
    public void setValidErrorMessage(List<String> validErrorMessage) {
        this.validErrorMessage = validErrorMessage;
    }
}
