package org.jtalks.poulpe.web.controller.branch;

import com.google.common.collect.Lists;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Group;
import org.jtalks.poulpe.web.controller.zkmacro.BranchPermissionManagementRow;
import org.zkoss.bind.annotation.NotifyChange;

import java.util.LinkedList;
import java.util.List;

/**
 * A View Model for page that allows user to specify what actions can be done with the specific branch and what
 * user groups can do them.
 *
 * @author stanislav bashkirtsev
 */
public class BranchPermissionManagementVm {
    private List<BranchPermissionManagementRow> rows = new LinkedList<BranchPermissionManagementRow>();
    private String prop = "prop";
    private Branch branch;
    private String test = "test";

    public BranchPermissionManagementVm() {
        rows.add(new BranchPermissionManagementRow("Allowed", Lists.newArrayList(new Group("Moderators", ""))));
        rows.add(new BranchPermissionManagementRow("Restricted", Lists.newArrayList(new Group
                ("Moderators", ""), new Group("Registered Users", ""), new Group("Activated Users", ""))));
    }

    public List<BranchPermissionManagementRow> getRows() {
        return rows;
    }

    public void setRows(List<BranchPermissionManagementRow> rows) {
        this.rows = rows;
    }

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    public String getTest() {
        return test;
    }
    @NotifyChange
    public void setTest(String test) {
        this.test = test;
    }
}

