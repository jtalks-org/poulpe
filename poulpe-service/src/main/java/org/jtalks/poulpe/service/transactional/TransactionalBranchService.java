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

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.jtalks.common.security.acl.AclManager;
import org.jtalks.common.security.acl.AclManagerImpl;
import org.jtalks.common.security.acl.BasicAclBuilder;
import org.jtalks.common.security.acl.JtalksPermission;
import org.jtalks.common.service.transactional.AbstractTransactionalEntityService;
import org.jtalks.poulpe.model.dao.BranchDao;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Group;
import org.jtalks.poulpe.service.BranchService;
import org.jtalks.poulpe.service.exceptions.NotUniqueException;
import org.jtalks.poulpe.service.security.*;

import java.util.Collection;
import java.util.List;

/**
 * @author Vitaliy Kravchenko
 * @author Pavel Vervenko
 */
public class TransactionalBranchService extends AbstractTransactionalEntityService<Branch, BranchDao> implements
        BranchService {
    private final AclManager aclManager;

    /**
     * Create an instance of entity based service
     */
    public TransactionalBranchService(BranchDao branchDao, AclManagerImpl aclManager) {
        this.dao = branchDao;
        this.aclManager = aclManager;
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
    public Table<JtalksPermission, Group, Boolean> getGroupAccessListFor(Branch branch) {
        Table<JtalksPermission, Group, Boolean> table = HashBasedTable.create();
        table.put(BranchPermission.CREATE_TOPICS, new Group("Moderator"), true);
        table.put(BranchPermission.CREATE_TOPICS, new Group("Admins"), true);
        table.put(BranchPermission.CREATE_TOPICS, new Group("Activated Users"), true);
        table.put(BranchPermission.CREATE_TOPICS, new Group("Banned Users"), false);
        table.put(BranchPermission.CREATE_TOPICS, new Group("Naughty Users"), false);
        table.put(BranchPermission.CREATE_TOPICS, new Group("Trolls"), false);
        return table;
    }

    @Override
    public void grantPermissions(Branch branch, JtalksPermission permission, Collection<Group> groups) {
        new BasicAclBuilder(aclManager)
                .grant(permission).setOwner(groups.toArray(new Group[]{})).on(branch).flush();

    }

    @Override
    public void restrictPermissions(Branch branch, JtalksPermission permission, Collection<Group> groups) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deletePermissions(Branch branch, JtalksPermission permission, Collection<Group> groups) {
        new BasicAclBuilder(aclManager)
                .grant(permission).setOwner(groups.toArray(new Group[]{})).on(branch).flush();
    }
}