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
package org.jtalks.poulpe.web.controller;

import org.jtalks.poulpe.model.entity.TopicType;
import org.jtalks.poulpe.service.TopicTypeService;
import org.jtalks.poulpe.web.controller.topictype.TopicTypePresenter;
import org.jtalks.poulpe.web.controller.topictype.TopicTypePresenter.TopicTypeView;
import org.mockito.Mock;
import static org.testng.Assert.assertEquals;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Pavel Vervenko
 * @author Vahluev Vyacheslav
 */
public class TopicTypePresenterTest {

    private TopicTypePresenter presenter = new TopicTypePresenter();
    @Mock
    private TopicTypeView view;
    @Mock
    private TopicTypeService topicTypeService;
    
    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter.setTopicTypeService(topicTypeService);
        presenter.initView(view);
    }
    
    @Test
    public void validateTopicTypeEmptyTitleTest() {
    	TopicType topicType = new TopicType("", "");
    	
    	assertEquals(presenter.validateTopicType(topicType),
    			presenter.ERROR_TOPICTYPE_TITLE_CANT_BE_VOID);    	
    }
    
    @Test
    public void validateTopicTypeNullTitleTest() {
    	TopicType topicType = new TopicType();
    	topicType.setTitle(null);
    	
    	assertEquals(presenter.validateTopicType(topicType),
    			presenter.ERROR_TOPICTYPE_TITLE_CANT_BE_VOID);    	
    }
    
    @Test
    public void validateTopicTypeCorrectTitleTest() {
    	TopicType topicType = new TopicType("Title","Desc");    	
    	
    	assertEquals(presenter.validateTopicType(topicType),
    			null);    	
    }
    
}
