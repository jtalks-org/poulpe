/**
 * Copyright (C) 2011  jtalks.org Team
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
 * Also add information on how to contact you by electronic and paper mail.
 * Creation date: Apr 12, 2011 / 8:05:19 PM
 * The jtalks.org Project
 */
package org.jtalks.poulpe.service.transactional;

import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.jtalks.common.service.transactional.AbstractTransactionalEntityService;
import org.jtalks.poulpe.model.entity.Section;
import org.jtalks.poulpe.model.dao.SectionDao;
import org.jtalks.poulpe.service.SectionService;
import org.jtalks.poulpe.service.exceptions.NotUniqueException;

/**
 * 
 * @author tanya birina
 * 
 */
public class TransactionalSectionService extends AbstractTransactionalEntityService<Section, SectionDao> implements
        SectionService {
    /**
     * Create an instance of entity based service
     * 
     * @param sectionDao
     *            - data access object
     */
    public TransactionalSectionService(SectionDao sectionDao) {
        this.dao = sectionDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSectionExists(String section) {
        return dao.isSectionNameExists(section);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Section> getAll() {
        return dao.getAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteSection(Section section) {
        return dao.delete(section.getId());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveSection(Section section) throws NotUniqueException {
        try {
            dao.saveOrUpdate(section);
        } catch (ConstraintViolationException e) {
            throw new NotUniqueException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean deleteRecursively(Section victim) {
        return dao.deleteRecursively(victim.getId());
    }

    /** {@inheritDoc} */
    @Override
    public boolean deleteAndMoveBranchesTo(Section victim, Section recipient) {
        if (victim.getId() == recipient.getId()) {
            throw new IllegalArgumentException("Victim and recipient can't be the same section");
        }
        return dao.deleteAndMoveBranchesTo(victim.getId(), recipient.getId());
    }

}
