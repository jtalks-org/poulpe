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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jtalks.poulpe.model.dao.TopicTypeDao;
import org.jtalks.poulpe.model.entity.TopicType;
import org.jtalks.poulpe.validation.EntityValidator;
import org.jtalks.poulpe.validation.ValidationError;
import org.jtalks.poulpe.validation.ValidationException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Pavel Vervenko
 * @author Alexey Grigorev
 */
public class TransactionalTopicTypeServiceTest {

    private TransactionalTopicTypeService topicTypeService;

    @Mock TopicTypeDao dao;
    @Mock EntityValidator entityValidator;

    private long topicTypeId = 10;
    private TopicType topicType = topicTypeWithId(topicTypeId);

    private TopicType topicTypeWithId(long topicTypeId) {
        TopicType topicType = new TopicType();
        topicType.setId(topicTypeId);
        return topicType;
    }

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        topicTypeService = new TransactionalTopicTypeService(dao, entityValidator);
    }

    @Test
    public void testGetAll() {
        topicTypeService.getAll();
        verify(dao).getAll();
    }

    @Test
    public void saveTopic() {
        topicTypeService.saveOrUpdate(topicType);
        verify(dao).saveOrUpdate(topicType);
    }

    @Test
    public void saveTopicValidated() {
        topicTypeService.saveOrUpdate(topicType);
        verify(entityValidator).throwOnValidationFailure(topicType);
    }

    @Test(expectedExceptions = ValidationException.class)
    public void saveTopicWithConstraintsViolations() {
        givenConstraintsViolations();
        topicTypeService.saveOrUpdate(topicType);
    }

    private void givenConstraintsViolations() {
        Set<ValidationError> dontCare = Collections.<ValidationError> emptySet();
        doThrow(new ValidationException(dontCare)).when(entityValidator).throwOnValidationFailure(any(TopicType.class));
    }

    @Test
    public void testDeleteTopicType() {
        topicTypeService.deleteTopicType(topicType);
        verify(dao).delete(topicTypeId);
    }

    @Test
    public void testDeleteTopicTypes() {
        List<TopicType> list = Arrays.asList(topicType);
        topicTypeService.deleteTopicTypes(list);
        verify(dao).delete(topicTypeId);
    }

}
