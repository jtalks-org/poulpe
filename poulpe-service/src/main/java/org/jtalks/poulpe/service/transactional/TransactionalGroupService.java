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

import org.jtalks.common.service.transactional.AbstractTransactionalEntityService;
import org.jtalks.poulpe.model.dao.GroupDao;
import org.jtalks.poulpe.model.entity.Group;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.validation.EntityValidator;


/**
 * 
 * @author Vitaliy Kravchenko
 * @author Pavel Vervenko
 */
public class TransactionalGroupService extends AbstractTransactionalEntityService<Group, GroupDao>
        implements GroupService {
	 private final EntityValidator validator;

    /**
     * Create an instance of entity based service
     *
     * @param branchDao - data access object, which should be able do all CRUD operations.
     */
    public TransactionalGroupService(GroupDao groupDao, EntityValidator validator) {
        this.dao = groupDao;
        this.validator = validator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Group> getAll() {
        return dao.getAll();
    }
    
    @Override
    public List<Group> getAllMatchedByName(String name) {
        return dao.getMatchedByName(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteGroup(Group group) {
          // TODO: check returned value? 
          dao.delete(group.getId());
    }

    /**
     * {@inheritDoc}
     */
	@Override
	public void saveGroup(Group selectedGroup) {
		validator.throwOnValidationFailure(selectedGroup);
		dao.saveOrUpdate(selectedGroup);
	}
    
    
  
  
}