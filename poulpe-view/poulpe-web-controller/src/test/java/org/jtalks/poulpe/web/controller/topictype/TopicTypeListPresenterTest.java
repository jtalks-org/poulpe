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
package org.jtalks.poulpe.web.controller.topictype;

import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.jtalks.poulpe.model.entity.TopicType;
import org.jtalks.poulpe.service.TopicTypeService;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.EditListener;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.utils.ObjectCreator;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.jtalks.poulpe.web.controller.topictype.TopicTypeListPresenter}
 *
 * @author Vyacheslav Zhivaev
 *
 */
public class TopicTypeListPresenterTest {

    // SUT
    TopicTypeListPresenter presenter;

    @Mock
    TopicTypeService topicTypeService;
    @Mock
    WindowManager windowManager;
    @Mock
    DialogManager dialogManager;
    @Mock
    TopicTypeListView view;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new TopicTypeListPresenter();
        presenter.setDialogManager(dialogManager);
        presenter.setTopicTypeService(topicTypeService);
        presenter.setWindowManager(windowManager);
    }

    @Test
    public void testInitView() {
        List<TopicType> types = buildFakeTopicTypeList();
        when(topicTypeService.getAll()).thenReturn(types);
        presenter.initView(view);
        verify(view).showTopicTypeList(types);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testOnAddAction() {
        presenter.onAddAction();
        verify(windowManager).openTopicTypeWindowForCreate(isA(EditListener.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testOnEditAction() {
        TopicType t = buildFakeTopicType();
        presenter.onEditAction(t);
        verify(windowManager).openTopicTypeWindowForEdit(eq(t), isA(EditListener.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testOnDeleteActionWithSelection() {
        givenSelectionExist();
        presenter.onDeleteAction();
        verify(dialogManager).confirmDeletion(isA(List.class), isA(DialogManager.Performable.class));
    }

    @Test
    public void testOnDeleteActionWithoutSelection() {
        givenSelectionNotExist();
        presenter.onDeleteAction();
        verify(dialogManager).notify(isA(String.class));
    }

    private void givenSelectionExist() {
        when(view.getSelectedTopicType()).thenReturn(buildFakeTopicTypeList());
        presenter.initView(view);
    }

    private void givenSelectionNotExist() {
        when(view.getSelectedTopicType()).thenReturn(Collections.<TopicType>emptyList());
        presenter.initView(view);
    }

    /**
     * Gets dummy TopicType for testing
     *
     * @return fake TopicType instance
     */
    public TopicType buildFakeTopicType() {
        return ObjectCreator.getFakeTopicType(
                (long) (Math.random() * Long.MAX_VALUE)
                , "test topic title"
                , "test topic description");
    }

    /**
     * Gets dummy immutable list of TopicType for testing
     *
     * @return list of fake TopicType, immutable instance
     */
    public List<TopicType> buildFakeTopicTypeList() {
        return Collections.nCopies(10, buildFakeTopicType());
    }

}
