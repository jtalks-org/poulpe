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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;


/**
 * <p>Java class for templateErrorMessages complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="templateErrorMessages">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codeErrorMessage" type="{http://www.jtalks.org/namespaces/1.0}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 * @author Mikhail Zaitsev
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "codeErrorMessages", namespace = "http://www.jtalks.org/namespaces/1.0")
public class CodeErrorMessages {

    @XmlElement(namespace = "http://www.jtalks.org/namespaces/1.0", required = false)
    private List<String> codeErrorMessage;

    /**
     * Constructs object of this class
     */
    public CodeErrorMessages() {
    }

    /**
     * Returns list of codes error messages
     *
     * @return list of codes error messages
     */
    public List<String> getCodeErrorMessage() {
        return codeErrorMessage;
    }

    /**
     * Sets list of codes error messages
     *
     * @param codeErrorMessage list of codes error messages
     */
    public void setCodeErrorMessage(List<String> codeErrorMessage) {
        this.codeErrorMessage = codeErrorMessage;
    }
}
