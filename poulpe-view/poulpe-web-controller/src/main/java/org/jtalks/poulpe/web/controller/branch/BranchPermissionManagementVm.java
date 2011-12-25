package org.jtalks.poulpe.web.controller.branch;

import com.google.common.collect.Table;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Group;
import org.jtalks.poulpe.service.BranchService;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.service.security.JtalksPermission;
import org.jtalks.poulpe.web.controller.zkmacro.BranchPermissionManagementBlock;
import org.jtalks.poulpe.web.controller.zkmacro.BranchPermissionManagementRow;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Window;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * A View Model for page that allows user to specify what actions can be done with the specific branch and what user
 * groups can do them.
 *
 * @author stanislav bashkirtsev
 */
public class BranchPermissionManagementVm {
    private final GroupService groupService;
    private final BranchService branchService;
    private final List<BranchPermissionManagementBlock> blocks = new ArrayList<BranchPermissionManagementBlock>();
    private ManageUserGroupsDialogVm groupsDialogVm;
    private Branch branch;

    public BranchPermissionManagementVm(@Nonnull BranchService branchService, @Nonnull GroupService groupService) {
        this.groupService = groupService;
        this.branchService = branchService;
        initDataForView();
    }

    @Command
    public void sortAddedList() {
        groupsDialogVm.revertSortingOfAddedList();

    }

    @Command
    public void sortAvailableList() {
        groupsDialogVm.revertSortingOfAvailableList();

    }

    @Command
    public void showGroupsDialog(@BindingParam("params") String params) {
        Map<String, String> parsedParams = parseParams(params);
        Integer blockId = Integer.parseInt(parsedParams.get("blockId"));
        BranchPermissionManagementBlock branchPermissionManagementBlock = blocks.get(blockId);
        String mode = parsedParams.get("mode");
        List<Group> toFillAddedGroupsGrid = getGroupsDependingOnMode(mode, branchPermissionManagementBlock);
        Window branchDialog = (Window) getComponent("branchPermissionManagementWindow");
        groupsDialogVm = createDialogData(toFillAddedGroupsGrid, "allow".equalsIgnoreCase(mode),
                branchPermissionManagementBlock.getPermission());
        Executions.createComponents("/sections/ManageGroupsDialog.zul", branchDialog, null);
    }

    @Command
    public void dialogClosed() {
        groupsDialogVm = null;
    }

    @Command
    public void save() {
        groupsDialogVm.getNewAdded();
        if (groupsDialogVm.isAllowAccess()) {
            branchService.grantPermissions(branch, groupsDialogVm.getPermission(), groupsDialogVm.getNewAdded());
        } else {
            branchService.restrictPermissions(branch, groupsDialogVm.getPermission(), groupsDialogVm.getNewAdded());
        }
        branchService.deletePermissions(branch, groupsDialogVm.getPermission(), groupsDialogVm.getRemovedFromAdded());
    }

    @Command
    public void moveSelectedToAdded() {
        groupsDialogVm.moveSelectedToAddedGroups();
    }

    @Command
    public void moveSelectedFromAdded() {
        groupsDialogVm.moveSelectedFromAddedGroups();
    }

    @Command
    public void moveAllToAdded() {
        groupsDialogVm.moveAllToAddedGroups();
    }

    @Command
    public void moveAllFromAdded() {
        groupsDialogVm.moveAllFromAddedGroups();
    }

    private void initDataForView() {
        Table<JtalksPermission, Group, Boolean> groupAccessList = branchService.getGroupAccessListFor(branch);
        for (JtalksPermission permission : groupAccessList.rowKeySet()) {
            BranchPermissionManagementRow allowRow = BranchPermissionManagementRow.newAllowRow(new ArrayList<Group>());
            BranchPermissionManagementRow restrictRow = BranchPermissionManagementRow.newRestrictRow(new ArrayList<Group>());
            for (Map.Entry<Group, Boolean> entry : groupAccessList.row(permission).entrySet()) {
                if (entry.getValue()) {
                    allowRow.addGroup(entry.getKey());
                } else {
                    restrictRow.addGroup(entry.getKey());
                }
            }
            blocks.add(new BranchPermissionManagementBlock(permission, allowRow, restrictRow));
        }
    }

    private List<Group> getGroupsDependingOnMode(String mode,
                                                 BranchPermissionManagementBlock branchPermissionManagementBlock) {
        if ("allow".equalsIgnoreCase(mode)) {
            return branchPermissionManagementBlock.getAllowRow().getGroups();
        } else {
            return branchPermissionManagementBlock.getRestrictRow().getGroups();
        }
    }

    private Map<String, String> parseParams(String params) {
        Map<String, String> parsedParams = new HashMap<String, String>();
        String[] paramRows = params.split(Pattern.quote(","));
        for (String nextParam : paramRows) {
            String[] splitParamRow = nextParam.trim().split(Pattern.quote("="));
            parsedParams.put(splitParamRow[0], splitParamRow[1]);
        }
        return parsedParams;
    }

    private ManageUserGroupsDialogVm createDialogData(List<Group> addedGroups, boolean allowAccess,
                                                      JtalksPermission permission) {
        return new ManageUserGroupsDialogVm(permission, allowAccess)
                .setAvailableGroups(groupService.getAll()).setAddedGroups(addedGroups);
    }

    private Component getComponent(String id) {
        return Executions.getCurrent().getDesktop().getFirstPage().getFellow(id);
    }

    public ListModel getBlocksListModel() {
        return new BindingListModelList(blocks, true);
    }

    public ManageUserGroupsDialogVm getGroupsDialogVm() {
        return groupsDialogVm;
    }

    public List<BranchPermissionManagementBlock> getBlocks() {
        return blocks;
    }
}

