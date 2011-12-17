package org.jtalks.poulpe.web.controller.branch;

import com.google.common.collect.Lists;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Group;
import org.jtalks.poulpe.service.security.BranchPermission;
import org.jtalks.poulpe.service.security.JtalksPermission;
import org.jtalks.poulpe.web.controller.zkmacro.BranchPermissionManagementBlock;
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
    private List<BranchPermissionManagementBlock> blocks = new LinkedList<BranchPermissionManagementBlock>();
    private String prop = "prop";
    private Branch branch;
    private String test = "test";

    public BranchPermissionManagementVm() {
        List<BranchPermissionManagementRow> rows = new LinkedList<BranchPermissionManagementRow>();
        rows.add(new BranchPermissionManagementRow("Allowed", Lists.newArrayList(new Group("Moderators", ""))));
        rows.add(new BranchPermissionManagementRow("Restricted", Lists.newArrayList(new Group
                ("Moderators", ""), new Group("Registered Users", ""), new Group("Activated Users", ""))));
        blocks.add(new BranchPermissionManagementBlock(BranchPermission.CREATE_TOPICS, rows.get(0), rows.get(1)));
    }

    public List<BranchPermissionManagementBlock> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<BranchPermissionManagementBlock> blocks) {
        this.blocks = blocks;
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

