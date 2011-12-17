package org.jtalks.poulpe.web.controller.branch;

import org.jtalks.poulpe.model.entity.Branch;
import org.zkoss.bind.annotation.NotifyChange;

/**
 * A View Model for page that allows user to specify what actions can be done with the specific branch and what
 * user groups can do them.
 *
 * @author stanislav bashkirtsev
 */
public class BranchPermissionManagementVm {
    private Branch branch;
    private String[] possibleAccessLevels = new String[]{"Allow", "Neutral", "Restrict"};
    private String test = "test";

    public String getTest() {
        return test;
    }
    @NotifyChange
    public void setTest(String test) {
        this.test = test;
    }
}

