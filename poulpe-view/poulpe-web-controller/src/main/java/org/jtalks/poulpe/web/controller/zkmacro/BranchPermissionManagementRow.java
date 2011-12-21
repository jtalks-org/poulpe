package org.jtalks.poulpe.web.controller.zkmacro;

import org.jtalks.poulpe.model.entity.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * @author stanislav bashkirtsev
 */
public class BranchPermissionManagementRow {
    private List<Group> groups;
    private String rowLabel;

    protected BranchPermissionManagementRow(String rowLabel, List<Group> groups) {
        this.groups = groups;
        this.rowLabel = rowLabel;
    }

    public List<Group> getGroups() {
        return groups;
    }

    
    public String getRowLabel() {
        return rowLabel;
    }

    public BranchPermissionManagementRow addGroup(Group group){
        groups.add(group);
        return this;
    }

    public static BranchPermissionManagementRow newAllowRow() {
        return new BranchPermissionManagementRow("${labels.branch.permissions.allow_label}", new ArrayList<Group>());
    }

    public static BranchPermissionManagementRow newRestrictRow() {
        return new BranchPermissionManagementRow("${labels.branch.permissions.restrict_label}",
                new ArrayList<Group>());
    }

    public static BranchPermissionManagementRow newAllowRow(List<Group> groups) {
        return new BranchPermissionManagementRow("${labels.branch.permissions.allow_label}", groups);
    }

    public static BranchPermissionManagementRow newRestrictRow(List<Group> groups) {
        return new BranchPermissionManagementRow("${labels.branch.permissions.restrict_label}", groups);
    }

}
