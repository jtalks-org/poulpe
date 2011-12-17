package org.jtalks.poulpe.web.controller.zkmacro;

import org.jtalks.poulpe.model.entity.Group;

import java.util.List;

/**
 * @author stanislav bashkirtsev
 */
public class BranchPermissionManagementRow {
    private List<Group> groups;
    private String rowLabel;

    public BranchPermissionManagementRow() {
        this.groups = groups;
    }

    public BranchPermissionManagementRow(String rowLabel, List<Group> groups) {
        this.groups = groups;
        this.rowLabel = rowLabel;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public String getRowLabel() {
        return rowLabel;
    }

    public void setRowLabel(String rowLabel) {
        this.rowLabel = rowLabel;
    }
}
