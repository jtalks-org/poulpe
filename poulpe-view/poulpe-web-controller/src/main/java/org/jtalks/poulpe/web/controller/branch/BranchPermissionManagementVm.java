package org.jtalks.poulpe.web.controller.branch;

import com.google.common.collect.Lists;
import org.jtalks.poulpe.model.dto.groups.GroupAccessList;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Group;
import org.jtalks.poulpe.service.BranchService;
import org.jtalks.poulpe.service.security.BranchPermission;
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
import java.util.*;
import java.util.regex.Pattern;

/**
 * A View Model for page that allows user to specify what actions can be done with the specific branch and what user
 * groups can do them.
 *
 * @author stanislav bashkirtsev
 */
public class BranchPermissionManagementVm {
    private final List<BranchPermissionManagementBlock> blocks = new ArrayList<BranchPermissionManagementBlock>();
    private final ManageUserGroupsDialogVm userGroupsDialogVm = new ManageUserGroupsDialogVm();
    private final BranchService branchService;
    private Branch branch;

    public BranchPermissionManagementVm(@Nonnull BranchService branchService) {
        this.branchService = branchService;
        initDataForView();
    }

    @Command
    public void showGroupsDialog(@BindingParam("params") String params) {
        Map<String, String> parsedParams = parseParams(params);
        Integer blockId = Integer.parseInt(parsedParams.get("blockId"));
        BranchPermissionManagementBlock branchPermissionManagementBlock = blocks.get(blockId);
        String mode = parsedParams.get("mode");
        List<Group> toFillAddedGroupsGrid = getGroupsDependingOnMode(mode, branchPermissionManagementBlock);
        Window branchDialog = (Window) getComponent("branchPermissionManagementWindow");
        renewDialogData(userGroupsDialogVm, toFillAddedGroupsGrid);
        Executions.createComponents("/sections/ManageGroupsDialog.zul", branchDialog, null);
    }

    private void initDataForView() {
        List<BranchPermissionManagementRow> rows = new LinkedList<BranchPermissionManagementRow>();
        GroupAccessList groupAccessListForCurrentBranch = branchService.getGroupAccessListFor(branch);
        rows.add(new BranchPermissionManagementRow("Allowed", groupAccessListForCurrentBranch.getAllowed()));
        rows.add(new BranchPermissionManagementRow("Restricted", groupAccessListForCurrentBranch.getRestricted()));
        blocks.add(new BranchPermissionManagementBlock(BranchPermission.CREATE_TOPICS, rows.get(0), rows.get(1)));
        blocks.add(new BranchPermissionManagementBlock(BranchPermission.CREATE_TOPICS, rows.get(0), rows.get(1)));
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

    private void renewDialogData(ManageUserGroupsDialogVm userGroupsDialogVm1, List<Group> addedGroups) {
        userGroupsDialogVm1.setAvailableGroups(Lists.newArrayList(new Group
                ("Moderators", ""), new Group("Registered Users", ""), new Group("Activated Users", "")));
        userGroupsDialogVm1.setAddedGroups(addedGroups);
    }

    private Component getComponent(String id) {
        return Executions.getCurrent().getDesktop().getFirstPage().getFellow(id);
    }

    public ListModel getBlocksListModel() {
        return new BindingListModelList(blocks, true);
    }

    public ManageUserGroupsDialogVm getUserGroupsDialogVm() {
        return userGroupsDialogVm;
    }

    public List<BranchPermissionManagementBlock> getBlocks() {
        return blocks;
    }
}

