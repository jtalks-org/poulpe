package org.jtalks.poulpe.web.controller.rest.pojo;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "error", namespace = "http://www.jtalks.org/namespaces/1.0")
public class Error {

    @XmlAttribute
    private String code;

    @XmlValue
    private String error;

    public Error() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
