package org.jtalks.poulpe.web.controller.branch;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jtalks.poulpe.model.dto.branches.BranchAccessChanges;
import org.jtalks.poulpe.model.dto.branches.BranchAccessList;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Group;
import org.jtalks.poulpe.model.permissions.BranchPermission;
import org.jtalks.poulpe.model.permissions.JtalksPermission;
import org.jtalks.poulpe.service.BranchService;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.web.controller.zkmacro.BranchPermissionManagementBlock;
import org.jtalks.poulpe.web.controller.zkmacro.BranchPermissionRow;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Window;

import javax.annotation.Nonnull;
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
    private final Map<String, BranchPermissionManagementBlock> blocks = Maps.newLinkedHashMap();
    /**
     * Created each time {@link #showGroupsDialog(String)} is invoked.
     */
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
        String permissionName = parsedParams.get("permissionName");
        BranchPermissionManagementBlock branchPermissionManagementBlock = blocks.get(permissionName);
        String mode = parsedParams.get("mode");
        List<Group> toFillAddedGroupsGrid = getGroupsDependingOnMode(mode, branchPermissionManagementBlock);
        Window branchWindow = (Window) getComponent("branchPermissionManagementWindow");
        groupsDialogVm = createDialogData(toFillAddedGroupsGrid, "allow".equalsIgnoreCase(mode),
                branchPermissionManagementBlock.getPermission());
        Executions.createComponents("/sections/ManageGroupsDialog.zul", branchWindow, null);
    }

    @Command
    public void dialogClosed() {
        groupsDialogVm = null;
    }

    @Command
    public void saveDialogState() {
        BranchAccessChanges accessChanges = new BranchAccessChanges(groupsDialogVm.getPermission());
        accessChanges.setNewlyAddedPermissions(groupsDialogVm.getNewAdded());
        accessChanges.setRemovedPermissions(groupsDialogVm.getRemovedFromAdded());
        if (groupsDialogVm.isAllowAccess() && !accessChanges.isEmpty()) {
            branchService.changeGrants(branch, accessChanges);
        } else if (!groupsDialogVm.isAllowAccess() && !accessChanges.isEmpty()) {
            branchService.changeRestrictions(branch, accessChanges);
        }
        dialogClosed();
        Executions.getCurrent().sendRedirect("");//reloading the page, couldn't find a better way yet
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

    private void updateBlock(JtalksPermission permission, boolean allowRow, List<Group> groups) {
        BranchPermissionManagementBlock block = blocks.get(permission.getName());
        if (allowRow) {
            block = block.setAllowRow(BranchPermissionRow.newAllowRow(groups));
        } else {
            block = block.setRestrictRow(BranchPermissionRow.newRestrictRow(groups));
        }
        blocks.put(permission.getName(), block);
    }

    private void initDataForView() {
        branch = branchService.getAll().get(0);
        BranchAccessList groupAccessList = branchService.getGroupAccessListFor(branch);
        for (BranchPermission permission : groupAccessList.getPermissions()) {
            BranchPermissionRow allowRow = BranchPermissionRow.newAllowRow(groupAccessList.getAllowed(permission));
            BranchPermissionRow restrictRow = BranchPermissionRow.newRestrictRow(groupAccessList.getRestricted(permission));
            blocks.put(permission.getName(), new BranchPermissionManagementBlock(permission, allowRow, restrictRow));
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
        List<Group> allGroups = groupService.getAll();
        allGroups.removeAll(addedGroups);
        return new ManageUserGroupsDialogVm(permission, allowAccess)
                .setAvailableGroups(allGroups).setAddedGroups(addedGroups);
    }

    private Component getComponent(String id) {
        return Executions.getCurrent().getDesktop().getFirstPage().getFellow(id);
    }

    public ListModel getBlocksListModel() {
        return new BindingListModelList(Lists.newArrayList(blocks.values()), true);
    }

    public ManageUserGroupsDialogVm getGroupsDialogVm() {
        return groupsDialogVm;
    }

    public List<BranchPermissionManagementBlock> getBlocks() {
        return Lists.newArrayList(blocks.values());
    }
}

