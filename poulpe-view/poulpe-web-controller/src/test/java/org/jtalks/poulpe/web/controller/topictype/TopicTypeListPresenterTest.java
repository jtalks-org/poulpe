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

import org.apache.commons.lang.math.RandomUtils;
import org.jtalks.poulpe.model.entity.TopicType;
import org.jtalks.poulpe.service.TopicTypeService;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.EditListener;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.utils.ObjectCreator;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests for {@link TopicTypeListPresenter}
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

    @SuppressWarnings("unchecked")
    @Test
    public void testOnAddAction() {
        doAnswer(answerWindowForCreate()).when(windowManager)
                .openTopicTypeWindowForCreate(any(EditListener.class));

        presenter.initView(view);
        presenter.onAddAction();

        verifyViewShowTopicTypeList(2);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testOnEditAction() {
        TopicType topicType = buildFakeTopicType();

        doAnswer(answerWindowForEdit()).when(windowManager)
                .openTopicTypeWindowForEdit(eq(topicType), any(EditListener.class));

        presenter.initView(view);
        presenter.onEditAction(topicType);

        verifyViewShowTopicTypeList(2);
    }

    @Test
    public void testOnDeleteActionWithSelection() {
        givenAllFromService();
        givenSelectionExist();

        doNothing().when(topicTypeService).deleteTopicTypes(anyCollectionOf(TopicType.class));
        doAnswer(answerConfirmDeletion()).when(dialogManager)
                .confirmDeletion(anyListOf(String.class), any(DialogManager.Performable.class));

        presenter.initView(view);
        presenter.onDeleteAction();

        verifyViewShowTopicTypeList(2);
    }

    @Test
    public void testOnDeleteActionWithoutSelection() {
        givenSelectionNotExist();

        presenter.initView(view);
        presenter.onDeleteAction();

        verify(dialogManager).notify(anyString());
    }

    private void givenSelectionExist() {
        when(view.getSelectedTopicType()).thenReturn(buildFakeTopicTypeList());
    }

    private void givenSelectionNotExist() {
        when(view.getSelectedTopicType()).thenReturn(Collections.<TopicType>emptyList());
    }

    private void givenAllFromService() {
        when(topicTypeService.getAll()).thenReturn(buildFakeTopicTypeList());
    }

    /**
     * Fake answer for {@link WindowManager#openTopicTypeWindowForCreate(EditListener)}.
     *
     * @return answer instance
     */
    private Answer<?> answerWindowForCreate() {
        return new Answer<Void>() {
            @SuppressWarnings("unchecked")
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ((EditListener<TopicType>) invocation.getArguments()[0]).onCreate(null);
                return null;
            }
        };
    }

    /**
     * Fake answer for {@link WindowManager#openTopicTypeWindowForEdit(TopicType, EditListener)}.
     *
     * @return answer instance
     */
    private Answer<?> answerWindowForEdit() {
        return new Answer<Void>() {
            @SuppressWarnings("unchecked")
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ((EditListener<TopicType>) invocation.getArguments()[1]).onUpdate(null);
                return null;
            }
        };
    }

    /**
     * Fake answer for {@link DialogManager#confirmDeletion(String, Performable)}.
     *
     * @return answer instance
     */
    private Answer<?> answerConfirmDeletion() {
        return new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ((DialogManager.Performable) invocation.getArguments()[1]).execute();
                return null;
            }
        };
    }

    private void verifyViewShowTopicTypeList(int wantedNumberOfInvocations) {
        verify(view, times(wantedNumberOfInvocations)).showTopicTypeList(anyListOf(TopicType.class));
    }

    /**
     * Gets dummy TopicType for testing
     *
     * @return fake TopicType instance
     */
    public TopicType buildFakeTopicType() {
        return ObjectCreator.getFakeTopicType(RandomUtils.nextLong(),
                "test topic title", "test topic description");
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
