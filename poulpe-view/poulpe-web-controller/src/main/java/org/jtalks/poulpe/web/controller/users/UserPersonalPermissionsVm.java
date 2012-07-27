package org.jtalks.poulpe.web.controller.users;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import org.jtalks.common.model.entity.Entity;
import org.jtalks.common.model.permissions.GeneralPermission;
import org.jtalks.common.model.permissions.JtalksPermission;
import org.jtalks.poulpe.model.dto.PermissionForEntity;
import org.jtalks.poulpe.model.dto.PermissionsMap;
import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.zkmacro.EntityPermissionsBlock;
import org.jtalks.poulpe.web.controller.zkmacro.PermissionManagementBlock;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.util.resource.Labels;

import com.google.common.collect.Lists;
import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.permissions.ProfilePermission;

/**
 * Works with user permissions like editing own profile or sending private messages. These permissions might be
 * restricted the some groups (like Banned Users might not have permissions to send private messages because they may
 * send spam or they can't edit their profile because they can put spam into their signature).
 *
 * @author stanislav bashkirtsev
 */
public class UserPersonalPermissionsVm {

    // Internal state
    private final List<EntityPermissionsBlock> blocks;

    public UserPersonalPermissionsVm() {
    
        blocks = Lists.newArrayList();
        updateView();
    }
    
    public List<EntityPermissionsBlock> getBlocks() {
        return Collections.unmodifiableList(blocks);
    }
    /**
     * Update VM state.
     */
    private void updateView() {
       blocks.clear();
       List<PermissionManagementBlock> blocks_in=Lists.newArrayList();
       List<ProfilePermission> permissions = ProfilePermission.getAllAsList();
       PermissionsMap<ProfilePermission> permissionsMap = new PermissionsMap<ProfilePermission>(permissions); 

        for (ProfilePermission permission : permissions) {            
        blocks_in.add(new PermissionManagementBlock(permission, permissionsMap, Labels
                     .getLabel("permissions.allow_label"), Labels.getLabel("permissions.restrict_label")));        
        }
       
        Entity entity = new Group("some");
        
        blocks.add(new EntityPermissionsBlock(entity, "111", blocks_in));
    }    
    
    
}
