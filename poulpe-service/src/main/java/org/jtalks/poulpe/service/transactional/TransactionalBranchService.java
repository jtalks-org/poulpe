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

import org.jtalks.common.service.transactional.AbstractTransactionalEntityService;
import org.jtalks.poulpe.logic.BranchPermissionManager;
import org.jtalks.poulpe.model.dao.BranchDao;
import org.jtalks.poulpe.model.dto.branches.BranchAccessChanges;
import org.jtalks.poulpe.model.dto.branches.BranchAccessList;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Group;
import org.jtalks.poulpe.model.permissions.JtalksPermission;
import org.jtalks.poulpe.service.BranchService;
import org.jtalks.poulpe.service.exceptions.NotUniqueException;
import org.jtalks.poulpe.validation.EntityValidator;

import java.util.Collection;
import java.util.List;

/**
 * @author Vitaliy Kravchenko
 * @author Pavel Vervenko
 */
public class TransactionalBranchService extends AbstractTransactionalEntityService<Branch, BranchDao> implements
        BranchService {
    private final BranchPermissionManager branchPermissionManager;
    private final EntityValidator validator;

    /**
     * Create an instance of entity based service
     */
    public TransactionalBranchService(BranchDao branchDao, BranchPermissionManager branchPermissionManager, EntityValidator validator) {
        this.dao = branchDao;
        this.branchPermissionManager = branchPermissionManager;
        this.validator = validator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Branch> getAll() {
        return dao.getAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveBranch(Branch selectedBranch) {
        validator.throwOnValidationFailure(selectedBranch);
        dao.saveOrUpdate(selectedBranch);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteBranchRecursively(Branch victim) {
        dao.delete(victim.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteBranchMovingTopics(Branch victim, Branch recipient) {
        dao.delete(victim.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDuplicated(Branch branch) {
        return dao.isBranchDuplicated(branch);
    }

    @Override
    public BranchAccessList getGroupAccessListFor(Branch branch) {
        return branchPermissionManager.getGroupAccessListFor(branch);
    }

    @Override
    public void changeGrants(Branch branch, BranchAccessChanges changes){
        branchPermissionManager.changeGrants(branch, changes);
    }

    @Override
    public void changeRestrictions(Branch branch, BranchAccessChanges changes){
        branchPermissionManager.changeRestrictions(branch, changes);
    }

}