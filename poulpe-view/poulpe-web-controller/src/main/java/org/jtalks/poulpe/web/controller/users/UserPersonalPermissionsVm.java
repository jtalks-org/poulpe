package org.jtalks.poulpe.web.controller.users;

import com.google.common.collect.Lists;
import org.jtalks.common.model.entity.Entity;
import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.permissions.ProfilePermission;
import org.jtalks.poulpe.model.dto.GroupsPermissions;
import org.jtalks.poulpe.web.controller.zkmacro.PermissionManagementBlock;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import org.jtalks.common.model.dao.GroupDao;
import org.jtalks.common.model.permissions.JtalksPermission;
import org.jtalks.poulpe.model.dto.PermissionForEntity;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.group.BranchGroupMap;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zul.ListModelList;

/**
 * Works with user permissions like editing own profile or sending private
 * messages. These permissions might be restricted the some groups (like Banned
 * Users might not have permissions to send private messages because they may
 * send spam or they can't edit their profile because they can put spam into
 * their signature).
 *
 * @author stanislav bashkirtsev
 * @author Enykey
 */
public class UserPersonalPermissionsVm {

    private static final String 
            PERSONAL_PERMISSION_MANAGEMENT_PAGE = "WEB-INF/pages/users/PersonalPermissions.zul",
            MANAGE_GROUPS_DIALOG_ZUL = "WEB-INF/pages/users/EditGroupsForPersonalPermission.zul";
    
    
    private final GroupService groupService;
    private final WindowManager windowManager;
    private SelectedEntity<Object> selectedEntity;
    private final List<PermissionManagementBlock> blocks = Lists.newArrayList();
    private Group group;
    /**
     * Construct View-Model for 'User Personal Permissions' view.
     *
     * @param groupService the group service instance
     * @param selectedEntity the selected entity instance
     * @param windowManager the window manager instance
     */
    public UserPersonalPermissionsVm(@Nonnull GroupService groupService, @Nonnull SelectedEntity<Object> selectedEntity,
            @Nonnull WindowManager windowManager) {
        this.groupService = groupService;
        this.selectedEntity = selectedEntity;
        this.windowManager = windowManager;
    }

    /**
     * Update VM state.
     */
    @Init
    public void updateView() {
        blocks.clear();
        List<ProfilePermission> permissions = ProfilePermission.getAllAsList();
        GroupsPermissions<ProfilePermission> permissionsMap = groupService.getPermissionsFor();;

        for (ProfilePermission permission : permissions) {
            blocks.add(new PermissionManagementBlock(permission, permissionsMap, Labels
                    .getLabel("permissions.allow_label"), Labels.getLabel("permissions.restrict_label")));
        }
    }

    /**
     * Command for showing dialog with editing groups list for current permission.
     *
     * @param permission the permission for which editing window shows
     * @param mode       the mode for permission, can be only {@code "allow"} or {@code "restrict"}
     */
    @Command
    public void showGroupsDialog(@BindingParam("permission") JtalksPermission permission,
                                 @BindingParam("mode") String mode) {
        selectedEntity.setEntity(new PermissionForEntity(group, mode, permission));
        windowManager.open(MANAGE_GROUPS_DIALOG_ZUL);
    }    

    /**
     * Method opens page with permissions to choosen branch
     *
     * @param windowManager the window manager instance
     */
    public static void showPage(WindowManager windowManager) {
        windowManager.open(PERSONAL_PERMISSION_MANAGEMENT_PAGE);
    }
   
    
    public List<PermissionManagementBlock> getBlocks() {
        return Collections.unmodifiableList(blocks);
    }
}
