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
package org.jtalks.poulpe.service.transactional;

import java.util.List;

import org.jtalks.common.model.permissions.BranchPermission;
import org.jtalks.common.service.transactional.AbstractTransactionalEntityService;
import org.jtalks.common.validation.EntityValidator;
import org.jtalks.poulpe.logic.PermissionManager;
import org.jtalks.poulpe.model.dao.BranchDao;
import org.jtalks.poulpe.model.dto.PermissionChanges;
import org.jtalks.poulpe.model.dto.GroupsPermissions;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.service.BranchService;

/**
 * Implementation of {@link BranchService}.
 * 
 * @author Vitaliy Kravchenko
 * @author Pavel Vervenko
 * @author Vyacheslav Zhivaev
 */
public class TransactionalBranchService extends AbstractTransactionalEntityService<PoulpeBranch, BranchDao> implements
        BranchService {
    private final PermissionManager branchPermissionManager;
    private final EntityValidator validator;

    /**
     * Create an instance of entity based service.
     * 
     * @param branchDao instance of {@link BranchDao}
     * @param branchPermissionManager instance of {@link PermissionManager}
     * @param validator instance of {@link EntityValidator}
     */
    public TransactionalBranchService(BranchDao branchDao, PermissionManager branchPermissionManager,
            EntityValidator validator) {
        this.dao = branchDao;
        this.branchPermissionManager = branchPermissionManager;
        this.validator = validator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PoulpeBranch> getAll() {
        return dao.getAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveBranch(PoulpeBranch selectedBranch) {
        validator.throwOnValidationFailure(selectedBranch);
        dao.saveOrUpdate(selectedBranch);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteBranchRecursively(PoulpeBranch victim) {
        dao.delete(victim.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteBranchMovingTopics(PoulpeBranch victim, PoulpeBranch recipient) {
        dao.delete(victim.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GroupsPermissions<BranchPermission> getPermissionsFor(PoulpeBranch branch) {
        return branchPermissionManager.getPermissionsMapFor(branch);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeGrants(PoulpeBranch branch, PermissionChanges changes) {
        branchPermissionManager.changeGrants(branch, changes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeRestrictions(PoulpeBranch branch, PermissionChanges changes) {
        branchPermissionManager.changeRestrictions(branch, changes);
    }
}