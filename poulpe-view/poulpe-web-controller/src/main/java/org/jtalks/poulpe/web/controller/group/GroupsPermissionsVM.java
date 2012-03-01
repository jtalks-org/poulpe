/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jtalks.poulpe.web.controller.group;

import java.util.List;

import javax.annotation.Nonnull;

import org.jtalks.common.security.acl.AclManager;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.zkmacro.BranchPermissionManagementBlock;
import org.jtalks.poulpe.web.controller.zkmacro.BranchPermissionRow;
import org.zkoss.bind.annotation.Init;

import com.google.common.collect.Lists;

/**
 * View-Model for 'Group Permissions'.
 *
 * @author Vyacheslav Zhivaev
 *
 */
public class GroupsPermissionsVM {

    private final WindowManager windowManager;
    private final GroupService groupService;
    private final AclManager aclManager;
    private final List<BranchPermissionManagementBlock> blocks;

    public GroupsPermissionsVM(@Nonnull WindowManager windowManager, @Nonnull GroupService groupService,
            @Nonnull AclManager aclManager) {
        this.windowManager = windowManager;
        this.groupService = groupService;
        this.aclManager = aclManager;
        blocks = Lists.newLinkedList();
    }

    /**
     * @return the blocks
     */
    public List<BranchPermissionManagementBlock> getBlocks() {
        return blocks;
    }

    @Init
    public void updateView() {
        blocks.clear();
        
//        BranchPermissionRow allowRow = BranchPermissionRow.newAllowRow();
//        BranchPermissionRow restrictRow = BranchPermissionRow.newRestrictRow();
//        blocks.add(new BranchPermissionManagementBlock(ComponentPermission, allowRow, restrictRow));
    }

}
