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

import com.google.common.collect.Table;
import org.jtalks.common.security.acl.AclManagerImpl;
import org.jtalks.common.security.acl.BasicAclBuilder;
import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.common.service.transactional.AbstractTransactionalEntityService;
import org.jtalks.poulpe.model.dao.BranchDao;
import org.jtalks.poulpe.model.dto.groups.BranchAccessList;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Group;
import org.jtalks.poulpe.model.permissions.BranchPermission;
import org.jtalks.poulpe.model.permissions.JtalksPermission;
import org.jtalks.poulpe.service.BranchService;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.service.exceptions.NotUniqueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Vitaliy Kravchenko
 * @author Pavel Vervenko
 */
public class TransactionalBranchService extends AbstractTransactionalEntityService<Branch, BranchDao> implements
        BranchService {
    private final AclManagerImpl aclManager;
    private final GroupService groupService;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Create an instance of entity based service
     */
    public TransactionalBranchService(BranchDao branchDao, AclManagerImpl aclManager, GroupService groupService) {
        this.dao = branchDao;
        this.aclManager = aclManager;
        this.groupService = groupService;
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
    public void deleteBranch(Branch selectedBranch) {
        // TODO: check returned value?
        dao.delete(selectedBranch.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveBranch(Branch selectedBranch) throws NotUniqueException {
        if (dao.isBranchDuplicated(selectedBranch)) {
            throw new NotUniqueException();
        }

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
        BranchAccessList branchAccessList = BranchAccessList.create(BranchPermission.getAllAsList());
        Table<Integer, Long, Boolean> branchPermissions = aclManager.getBranchPermissions(branch);
        for (BranchPermission permission : branchAccessList.getPermissions()) {
            Map<Long, Boolean> row = branchPermissions.row(permission.getMask());
            for (Map.Entry<Long, Boolean> entry : row.entrySet()) {
                try {
                    Group group = groupService.get(entry.getKey());
                    branchAccessList.put(permission, group, entry.getValue());
                } catch (NotFoundException e) {
                    logger.warn("A group with ID {} was removed, but this ID is still registered as a Permission owner in " +
                            "ACL tables.", entry.getKey());
                }
            }
        }
        return branchAccessList;
    }

    @Override
    public void grantPermissions(Branch branch, JtalksPermission permission, Collection<Group> groups) {
        new BasicAclBuilder(aclManager).grant(permission).setOwner(groups.toArray(new Group[]{})).on(branch).flush();

    }

    @Override
    public void restrictPermissions(Branch branch, JtalksPermission permission, Collection<Group> groups) {
        new BasicAclBuilder(aclManager).restrict(permission).setOwner(groups.toArray(new Group[]{})).on(branch).flush();
    }

    @Override
    public void deletePermissions(Branch branch, JtalksPermission permission, Collection<Group> groups) {
        new BasicAclBuilder(aclManager).delete(permission).setOwner(groups.toArray(new Group[]{})).on(branch).flush();
    }
}