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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.jtalks.poulpe.model.dao.TopicTypeDao;
import org.jtalks.poulpe.model.entity.TopicType;
import org.jtalks.poulpe.service.exceptions.NotUniqueException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TransactionalTopicTypeServiceTest extends TestCase {

    private TransactionalTopicTypeService service;

    @Mock
    private TopicTypeDao dao;

    @Override
    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        service = new TransactionalTopicTypeService(dao, null);
    }

    @Test
    public void testGetAll() {
        List<TopicType> expectedList = new ArrayList<TopicType>();
        when(dao.getAll()).thenReturn(expectedList);

        List<TopicType> actualList = service.getAll();

        assertEquals(expectedList, actualList);
        verify(dao).getAll();
    }

    /*
    @Test
    public void testSaveTopicType() throws NotUniqueException {
        TopicType topicType = new TopicType();
        try {
            topicType.setTitle(null);
            service.saveOrUpdate(topicType);
            fail("null name not allowed");
        } catch (IllegalArgumentException e) {
            // ok
        }
        
        try {
            topicType.setTitle("");
            service.saveOrUpdate(topicType);
            fail("null name not allowed");
        } catch (IllegalArgumentException e) {
            // ok
        }
        
        when(dao.isTopicTypeNameExists(anyString())).thenReturn(true);
        try {
            topicType.setTitle("some type");
            service.saveOrUpdate(topicType);
            
        } catch (NotUniqueException e) {
            verify(dao).isTopicTypeNameExists("some type");
        }
        
        // test successful
        reset(dao);
        when(dao.isTopicTypeNameExists(anyString())).thenReturn(false);
        service.saveOrUpdate(topicType);
        verify(dao).isTopicTypeNameExists("some type");
        verify(dao).saveOrUpdate(topicType);
    }*/
    /*
    @Test
    public void testUpdateTopicType() throws NotUniqueException {
        TopicType topicType = new TopicType();
        try {
            topicType.setTitle(null);
            service.updateTopicType(topicType);
            fail("null name not allowed");
        } catch (IllegalArgumentException e) {
            // ok
        }
        
        try {
            topicType.setTitle("");
            service.updateTopicType(topicType);
            fail("null name not allowed");
        } catch (IllegalArgumentException e) {
            // ok
        }
        
        when(dao.isTopicTypeNameExists(anyString(), anyLong())).thenReturn(true);
        try {
            topicType.setTitle("some type");
            topicType.setId(123);
            service.updateTopicType(topicType);
            fail();
        } catch (NotUniqueException e) {
            verify(dao).isTopicTypeNameExists("some type", 123);
        }

        // test successful
        reset(dao);
        when(dao.isTopicTypeNameExists(anyString(), anyLong())).thenReturn(false);
        service.updateTopicType(topicType);
        verify(dao).isTopicTypeNameExists("some type", 123);
        verify(dao).saveOrUpdate(topicType);
    }*/

    @Test
    public void testDeleteTopicType() {
        TopicType topicType = new TopicType();
        Long testId = 12L;
        topicType.setId(testId);
        service.deleteTopicType(topicType);
        verify(dao).delete(testId);
    }
    
    @Test
    public void testDeleteTopicTypes() {
        TopicType topicType1 = new TopicType();
        topicType1.setId(12L);
        TopicType topicType2 = new TopicType();
        topicType2.setId(13L);
        List<TopicType> list = Arrays.asList(topicType1, topicType2);
        service.deleteTopicTypes(list);
        for (TopicType topicType : list) {
            verify(dao).delete(topicType.getId());
        }
    }

}
