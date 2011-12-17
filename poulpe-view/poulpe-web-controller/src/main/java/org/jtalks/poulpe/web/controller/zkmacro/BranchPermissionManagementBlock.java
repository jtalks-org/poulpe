package org.jtalks.poulpe.web.controller.zkmacro;

import org.jtalks.poulpe.service.security.JtalksPermission;

/**
 * @author stanislav bashkirtsev.
 */
public class BranchPermissionManagementBlock {
    private JtalksPermission permission;
    private BranchPermissionManagementRow allowRow;
    private BranchPermissionManagementRow restrictRow;

    public BranchPermissionManagementBlock() {
    }

    public BranchPermissionManagementBlock(JtalksPermission permission) {
        this.permission = permission;
    }

    public BranchPermissionManagementBlock(JtalksPermission permission, BranchPermissionManagementRow allowRow,
                                           BranchPermissionManagementRow restrictRow) {
        this.permission = permission;
        this.allowRow = allowRow;
        this.restrictRow = restrictRow;
    }

    public JtalksPermission getPermission() {
        return permission;
    }

    public void setPermission(JtalksPermission permission) {
        this.permission = permission;
    }

    public BranchPermissionManagementRow getAllowRow() {
        return allowRow;
    }

    public void setAllowRow(BranchPermissionManagementRow allowRow) {
        this.allowRow = allowRow;
    }

    public BranchPermissionManagementRow getRestrictRow() {
        return restrictRow;
    }

    public void setRestrictRow(BranchPermissionManagementRow restrictRow) {
        this.restrictRow = restrictRow;
    }
}
