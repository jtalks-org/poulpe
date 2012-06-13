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

import java.util.Collection;
import java.util.List;

import org.jtalks.common.service.transactional.AbstractTransactionalEntityService;
import org.jtalks.common.validation.EntityValidator;
import org.jtalks.poulpe.model.dao.TopicTypeDao;
import org.jtalks.poulpe.model.entity.TopicType;
import org.jtalks.poulpe.service.TopicTypeService;

/**
 * Implementation of {@link TopicType}
 * 
 * @author Pavel Vervenko
 * @author Alexey Grigorev
 */
public class TransactionalTopicTypeService extends AbstractTransactionalEntityService<TopicType, TopicTypeDao>
        implements TopicTypeService {

    private final EntityValidator validator;

    /**
     * Create an instance of entity based service
     * 
     * @param topicDao - data access object, which should be able do all CRUD
     * operations.
     * @param validator entity validator
     */
    public TransactionalTopicTypeService(TopicTypeDao topicDao, EntityValidator validator) {
        this.dao = topicDao;
        this.validator = validator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TopicType> getAll() {
        return dao.getAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteTopicType(TopicType topicType) {
        dao.delete(topicType.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveOrUpdate(TopicType topicType) {
        validator.throwOnValidationFailure(topicType);
        dao.saveOrUpdate(topicType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteTopicTypes(Collection<TopicType> topicTypes) {
        for (TopicType topicType : topicTypes) {
            deleteTopicType(topicType);
        }
    }

}
