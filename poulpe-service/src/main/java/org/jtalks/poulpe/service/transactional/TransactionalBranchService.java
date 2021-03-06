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
import org.jtalks.poulpe.model.dao.BranchDao;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.service.BranchService;

import java.util.List;

/**
 * Implementation of {@link BranchService}.
 * 
 * @author Vitaliy Kravchenko
 * @author Pavel Vervenko
 * @author Vyacheslav Zhivaev
 */
public class TransactionalBranchService extends AbstractTransactionalEntityService<PoulpeBranch, BranchDao> implements
        BranchService {

    /**
     * Create an instance of entity based service.
     * 
     * @param branchDao instance of {@link BranchDao}
     */
    public TransactionalBranchService(BranchDao branchDao) {
        this.dao = branchDao;
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

}