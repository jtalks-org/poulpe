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
package org.jtalks.poulpe.web.controller.rest.pojo;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for error complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="error">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="errorMessage" type="{http://www.jtalks.org/namespaces/1.0}string" minOccurs="0"/>
 *         &lt;element name="codeErrorMessages" type="{http://www.w3.org/2001/XMLSchema}codeErrorMessages" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 * @author Mikhail Zaitsev
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "error", namespace = "http://www.jtalks.org/namespaces/1.0")
@XmlRootElement(name = "registration", namespace = "http://www.jtalks.org/namespaces/1.0")
public class Error {

    @XmlElement(namespace = "http://www.jtalks.org/namespaces/1.0", required = false)
    private String errorMessage;

    @XmlElement(namespace = "http://www.jtalks.org/namespaces/1.0", required = false)
    private CodeErrorMessages codeErrorMessages;

    /**
     * Constructs object of this class
     */
    public Error() {
    }

    /**
     * Returns the message about error
     *
     * @return the message about error
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the message about error
     *
     * @param errorMessage the message about error
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Returns {@code CodeErrorMessages} about validation errors
     *
     * @return {@code CodeErrorMessages} about validation errors
     */
    public CodeErrorMessages getCodeErrorMessages() {
        return codeErrorMessages;
    }

    /**
     * Sets {@code CodeErrorMessages} about validation errors
     *
     * @param codeErrorMessages {@code CodeErrorMessages} about validation errors
     */
    public void setCodeErrorMessages(CodeErrorMessages codeErrorMessages) {
        this.codeErrorMessages = codeErrorMessages;
    }
}
