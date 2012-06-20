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
package org.jtalks.poulpe.model.dao.hibernate;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.jtalks.common.model.dao.hibernate.AbstractHibernateParentRepository;
import org.jtalks.common.model.entity.Group;
import org.jtalks.poulpe.model.dao.GroupDao;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import ru.javatalks.utils.general.Assert;

import java.util.List;

/**
 * Hibernate implementation of {@link GroupDao}
 * 
 * @author Vitaliy Kravchenko
 * @author Pavel Vervenko
 */
public class GroupHibernateDao extends AbstractHibernateParentRepository<Group>
		implements GroupDao {
	private final static String MODERAING_GROUP_ID = "moderating_group_id";

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Group> getAll() {
		return getSession().createQuery("from Group").list();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Group> getByName(String name) {
		Assert.throwIfNull(name, "name");
		if (StringUtils.isBlank(name)) {
			return this.getAll();
		}

		Query query = getSession().getNamedQuery("findGroupByName");
		query.setString("name", name);
		return query.list();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(Group group) {
		getSession().update(group);

		group.getUsers().clear();
		saveOrUpdate(group);
		super.delete(group);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PoulpeBranch> getModeratingBranches(Group group) {
		getSession().flush();
		Query query = getSession().createQuery(
				"from PoulpeBranch where MODERATORS_GROUP_ID=:"
						+ MODERAING_GROUP_ID);
		query.setParameter(MODERAING_GROUP_ID, group.getId());
		return query.list();
	}

}