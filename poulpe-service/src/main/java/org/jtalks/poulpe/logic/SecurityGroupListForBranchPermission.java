/**
 * Copyright (C) 2012  JTalks.org Team
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
package org.jtalks.poulpe.logic;

import java.util.List;

import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.permissions.BranchPermission;
import org.jtalks.poulpe.model.dto.SecurityGroupList;
import org.jtalks.poulpe.service.GroupService;

/**
 * The class filters list of all available security groups (see {@link SecurityGroupList}) and returns only those ones 
 * which are allowed for certain {@link BranchPermission}.
 * 
 * @author Evgeny Surovtsev
 *
 */
public class SecurityGroupListForBranchPermission {
	/**
	 * Constructor for SecurityGroupListForBranchPermission.
	 */
	public SecurityGroupListForBranchPermission(GroupService groupService) {
		this.groupService = groupService;
	}
	
	/**
	 * The method returns list of available security groups (persistent and special) for defined Branch permission.
	 * 
	 * @param branchPermission The Branch Permission which a list of security groups will be returned for. 
	 */
	public List<Group> getSecurityGroupList(BranchPermission branchPermission) {
		SecurityGroupList securityGroupList = groupService.getSecurityGroups();
		if (!isAnonymousAllowed(branchPermission)) {
			securityGroupList.removeAnonymousGroup();
		}
        return securityGroupList.getAllGroups();
	}
	
	/**
	 * Return if a given BranchPermission is allowed for Anonymous 
	 */
	private boolean isAnonymousAllowed(BranchPermission branchPermission) {
		return (branchPermission == BranchPermission.VIEW_TOPICS);
	}
	
	private GroupService groupService;
}
