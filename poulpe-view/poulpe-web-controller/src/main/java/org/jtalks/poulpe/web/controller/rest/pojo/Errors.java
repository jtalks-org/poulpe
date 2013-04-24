package org.jtalks.poulpe.web.controller.rest.pojo;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "errors", namespace = "http://www.jtalks.org/namespaces/1.0")
@XmlRootElement(name = "errors", namespace = "http://www.jtalks.org/namespaces/1.0")
public class Errors {

    @XmlElement(name = "error", namespace = "http://www.jtalks.org/namespaces/1.0", required = true)
    private List<Error> errorList;

    public Errors() {
    }

    public List<Error> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<Error> errorList) {
        this.errorList = errorList;
    }
}
