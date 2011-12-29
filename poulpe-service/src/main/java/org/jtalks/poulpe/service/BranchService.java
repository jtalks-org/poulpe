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
package org.jtalks.poulpe.service;

import org.jtalks.poulpe.model.dto.branches.BranchAccessChanges;
import org.jtalks.poulpe.model.dto.branches.BranchAccessList;
import org.jtalks.poulpe.model.permissions.JtalksPermission;
import org.jtalks.common.service.EntityService;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Group;
import org.jtalks.poulpe.service.exceptions.NotUniqueException;

import java.util.Collection;
import java.util.List;

/**
 * @author Vitaliy Kravchenko
 * @author Kirill Afonin
 */
public interface BranchService extends EntityService<Branch> {

    /**
     * Get list of all persistence objects T currently present in database.
     *
     * @return - list of persistence objects T.
     */
    List<Branch> getAll();

    /**
     * Mark the branch as deleted.
     *
     * @param selectedBranch branch to delete
     * @deprecated because there are two other delete methods
     */
    void deleteBranch(Branch selectedBranch);

    /**
     * Save or update branch.
     *
     * @param selectedBranch instance to save
     * @throws NotUniqueException if branch with the same name already exists
     */
    void saveBranch(Branch selectedBranch) throws NotUniqueException;

    /**
     * Removes all topics inside {@code victim} and then removes {@code victim} branch itself.
     *
     * @param victim the branch to be removed
     */
    void deleteBranchRecursively(Branch victim);

    /**
     * Moves all topics inside {@code victim} to another {@code recipient} branch and then removes {@code victim}
     * branch.
     *
     * @param victim    the branch to be removed
     * @param recipient the branch to take topics of {@code victim}
     */
    void deleteBranchMovingTopics(Branch victim, Branch recipient);

    /**
     * Checks if the branch is duplicated.
     *
     * @param branch branch to check
     */
    boolean isDuplicated(Branch branch);

    BranchAccessList getGroupAccessListFor(Branch branch);

    void grantPermissions(Branch branch, JtalksPermission permission, Collection<Group> groups);

    void restrictPermissions(Branch branch, JtalksPermission permission, Collection<Group> groups);

    void deletePermissions(Branch branch, JtalksPermission permission, Collection<Group> groups);

    void changeGrants(Branch branch, BranchAccessChanges changes);

    void changeRestrictions(Branch branch, BranchAccessChanges changes);
}