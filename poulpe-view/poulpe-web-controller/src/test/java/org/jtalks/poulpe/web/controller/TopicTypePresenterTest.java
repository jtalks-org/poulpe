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

import static org.jtalks.poulpe.web.controller.utils.ObjectCreator.getFakeTopicType;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.jtalks.poulpe.web.controller.topictype.TopicTypePresenter.*;

import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.poulpe.model.entity.TopicType;
import org.jtalks.poulpe.service.TopicTypeService;
import org.jtalks.poulpe.service.exceptions.NotUniqueException;
import org.jtalks.poulpe.web.controller.topictype.TopicTypePresenter;
import org.jtalks.poulpe.web.controller.topictype.TopicTypeView;
import org.mockito.Mock;
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
    @Mock
    private WindowManager windowManager;
    @Mock
    protected EditListener<TopicType> listener;
    
    private static final String title = "title";
    private static final String desc = "desc";
    private static final int topicTypeId = 12;
    private TopicType activeTopicType = getFakeTopicType(topicTypeId, title, desc);
    private TopicType activeTopicTypeWithEmptyView = getFakeTopicType(topicTypeId, "", desc);

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter.setTopicTypeService(topicTypeService);
        presenter.initView(view);
        presenter.setWindowManager(windowManager);
        presenter.setListener(listener);
    }

    @Test
    public void validateTopicTypeEmptyTitleTest() {
        assertEquals(presenter.validateTopicType(activeTopicTypeWithEmptyView), TITLE_CANT_BE_VOID);
    }

    @Test
    public void validateTopicTypeNullTitleTest() {
        TopicType topicType = new TopicType(); // title is null
        assertEquals(presenter.validateTopicType(topicType), TITLE_CANT_BE_VOID);
    }

    @Test
    public void validateTopicTypeCorrectTitleTest() {
        assertEquals(presenter.validateTopicType(activeTopicType), null);
    }

    @Test
    public void initializeForCreateTest() {
        presenter.initializeForCreate(view, listener);
        verify(view).hideEditAction();
    }

    @Test
    public void initializeForEditTestOK() throws NotFoundException {
        when(topicTypeService.get(Long.valueOf(topicTypeId))).thenReturn(activeTopicType);

        presenter.initializeForEdit(view, activeTopicType, listener);

        verify(view).hideCreateAction();
        verify(view).showTypeTitle(activeTopicType.getTitle());
        verify(view).showTypeDescription(activeTopicType.getDescription());
    }
    

    @Test
    public void initializeForEditTestError() throws NotFoundException {
        when(topicTypeService.get(Long.valueOf(topicTypeId))).thenThrow(new NotFoundException());

        presenter.initializeForEdit(view, activeTopicType, listener);

        verify(view).hideCreateAction();
        verify(view).openErrorPopupInTopicTypeDialog(TITLE_DOESNT_EXISTS);
        verify(view, never()).showTypeTitle(activeTopicType.getTitle());
        verify(view, never()).showTypeTitle(activeTopicType.getDescription());
    }

    @Test
    public void closeViewTest() {
        presenter.closeView();
        
        verify(windowManager).closeWindow(view);
    }

    @Test
    public void onCreateActionTestOK() {
        givenInputDataIsCorrect();

        presenter.onCreateAction();

        verify(windowManager).closeWindow(view);
        verify(listener).onCreate(any(TopicType.class));
    }

    @Test
    public void onCreateActionTestEmptyName() {
        givenEmptyTitle();

        presenter.onCreateAction();

        verify(windowManager, never()).closeWindow(view);
        verify(listener, never()).onCreate(any(TopicType.class));
    }

    @Test
    public void onUpdateActionTestOK() {
        givenInputDataIsCorrect();

        presenter.onUpdateAction();

        verify(windowManager).closeWindow(any(TopicTypeView.class));
        verify(listener).onUpdate(any(TopicType.class));
    }

    @Test
    public void onUpdateActionTestEmptyName() {
        givenEmptyTitle();

        presenter.onUpdateAction();

        verify(windowManager, never()).closeWindow(view);
        verify(listener, never()).onUpdate(any(TopicType.class));
    }

    @Test
    public void onCancelEditActionTest() {
        presenter.onCancelEditAction();

        verify(windowManager).closeWindow(view);
        verify(listener).onCloseEditorWithoutChanges();
    }

    @Test
    public void saveTestOK() throws NotUniqueException {
        givenInputDataIsCorrect();

        presenter.save();

        verify(view, never()).openErrorPopupInTopicTypeDialog(anyString());
        verify(topicTypeService).saveTopicType(any(TopicType.class));
    }

    @Test
    public void saveTestError() throws NotUniqueException {
        givenEmptyTitle();

        presenter.save();

        verify(view).openErrorPopupInTopicTypeDialog(TITLE_CANT_BE_VOID);
        verify(topicTypeService, never()).saveTopicType(any(TopicType.class));
    }

    @Test
    public void updateTestOK() throws NotUniqueException {
        givenInputDataIsCorrect();

        presenter.update();

        verify(view, never()).openErrorPopupInTopicTypeDialog(anyString());
        verify(topicTypeService).updateTopicType(any(TopicType.class));
    }

    @Test
    public void updateTestError() throws NotUniqueException {
        givenEmptyTitle();

        presenter.update();

        verify(view).openErrorPopupInTopicTypeDialog(TITLE_CANT_BE_VOID);
        verify(topicTypeService, never()).updateTopicType(any(TopicType.class));
    }
    
    @Test
    public void onTitleLoseFocusTestError() {
        givenInputDataIsCorrect();
        givenTopicTypeNameExists();

        presenter.onTitleLoseFocus();

        verify(view).openErrorPopupInTopicTypeDialog(TITLE_ALREADY_EXISTS);
    }

    private void givenTopicTypeNameExists() {
        when(topicTypeService.isTopicTypeNameExists(anyString(), anyLong())).thenReturn(true);
    }

    @Test
    public void onTitleLoseFocusTestOK() {
        givenInputDataIsCorrect();
        givenTopicTypeDoesNotExists();

        presenter.onTitleLoseFocus();

        verify(view, never()).openErrorPopupInTopicTypeDialog(TITLE_ALREADY_EXISTS);
    }

    private void givenTopicTypeDoesNotExists() {
        when(topicTypeService.isTopicTypeNameExists(anyString(), anyLong())).thenReturn(false);
    }

    private void givenInputDataIsCorrect() {
        when(view.getTypeTitle()).thenReturn(title);
        when(view.getTypeDescription()).thenReturn(desc);
    }
    
    private void givenEmptyTitle() {
        when(view.getTypeTitle()).thenReturn("");
        when(view.getTypeDescription()).thenReturn(desc);
    }
}
