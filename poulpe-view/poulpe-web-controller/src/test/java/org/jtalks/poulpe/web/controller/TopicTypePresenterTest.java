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
import org.jtalks.poulpe.web.controller.topictype.TopicTypePresenter.TopicTypeView;
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
    private TopicType fake = getFakeTopicType(topicTypeId, title, desc);

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
        TopicType topicType = new TopicType("", "");

        assertEquals(presenter.validateTopicType(topicType), ERROR_TOPICTYPE_TITLE_CANT_BE_VOID);
    }

    @Test
    public void validateTopicTypeNullTitleTest() {
        TopicType topicType = new TopicType(); // title is null

        assertEquals(presenter.validateTopicType(topicType), ERROR_TOPICTYPE_TITLE_CANT_BE_VOID);
    }

    @Test
    public void validateTopicTypeCorrectTitleTest() {
        TopicType topicType = new TopicType(title, desc);

        assertEquals(presenter.validateTopicType(topicType), null);
    }

    @Test
    public void initializeForCreateTest() {
        presenter.initializeForCreate(view, listener);

        verify(view).hideEditAction();
    }

    @Test
    public void initializeForEditTestOK() throws NotFoundException {
        when(topicTypeService.get(Long.valueOf(topicTypeId))).thenReturn(fake);

        presenter.initializeForEdit(view, fake, listener);

        verify(view).hideCreateAction();
        verify(view).showTypeTitle(fake.getTitle());
        verify(view).showTypeDescription(fake.getDescription());
    }
    

    @Test
    public void initializeForEditTestError() throws NotFoundException {
        when(topicTypeService.get(Long.valueOf(topicTypeId))).thenThrow(new NotFoundException());

        presenter.initializeForEdit(view, fake, listener);

        verify(view).hideCreateAction();
        verify(view).openErrorPopupInTopicTypeDialog(ERROR_TOPICTYPE_TITLE_DOESNT_EXISTS);
        verify(view, never()).showTypeTitle(fake.getTitle());
        verify(view, never()).showTypeTitle(fake.getDescription());
    }

    @Test
    public void onTitleLoseFocusTestError() {
        when(topicTypeService.isTopicTypeNameExists(anyString(), anyLong())).thenReturn(true);
        when(view.getTypeTitle()).thenReturn(title);

        presenter.onTitleLoseFocus();

        verify(view).openErrorPopupInTopicTypeDialog(ERROR_TOPICTYPE_TITLE_ALREADY_EXISTS);
    }

    @Test
    public void onTitleLoseFocusTestOK() {
        when(topicTypeService.isTopicTypeNameExists(anyString(), anyLong())).thenReturn(false);
        when(view.getTypeTitle()).thenReturn(title);

        presenter.onTitleLoseFocus();

        verify(view, never()).openErrorPopupInTopicTypeDialog(ERROR_TOPICTYPE_TITLE_ALREADY_EXISTS);
    }

    @Test
    public void closeViewTest() {
        presenter.closeView();

        verify(windowManager).closeWindow(any(TopicTypeView.class));
    }

    @Test
    public void onCreateActionTestOK() {
        givenTopicType();

        presenter.onCreateAction();

        verify(windowManager).closeWindow(any(TopicTypeView.class));
        verify(listener).onCreate(any(TopicType.class));
    }

    @Test
    public void onCreateActionTestEmptyName() {
        givenTopicTypeWithEmptyTitle();

        presenter.onCreateAction();

        verify(windowManager, never()).closeWindow(any(TopicTypeView.class));
        verify(listener, never()).onCreate(any(TopicType.class));
    }

    @Test
    public void onUpdateActionTestOK() {
        givenTopicType();

        presenter.onUpdateAction();

        verify(windowManager).closeWindow(any(TopicTypeView.class));
        verify(listener).onUpdate(any(TopicType.class));
    }

    @Test
    public void onUpdateActionTestEmptyName() {
        givenTopicTypeWithEmptyTitle();

        presenter.onUpdateAction();

        verify(windowManager, never()).closeWindow(any(TopicTypeView.class));
        verify(listener, never()).onUpdate(any(TopicType.class));
    }

    @Test
    public void onCancelEditActionTest() {
        presenter.onCancelEditAction();

        verify(windowManager).closeWindow(any(TopicTypeView.class));
        verify(listener).onCloseEditorWithoutChanges();
    }

    @Test
    public void saveTestOK() throws NotUniqueException {
        givenTopicType();

        presenter.save();

        verify(view, never()).openErrorPopupInTopicTypeDialog(anyString());
        verify(topicTypeService).saveTopicType(any(TopicType.class));
    }

    @Test
    public void saveTestError() throws NotUniqueException {
        givenTopicTypeWithEmptyTitle();

        presenter.save();

        verify(view).openErrorPopupInTopicTypeDialog(ERROR_TOPICTYPE_TITLE_CANT_BE_VOID);
        verify(topicTypeService, never()).saveTopicType(any(TopicType.class));
    }

    @Test
    public void updateTestOK() throws NotUniqueException {
        givenTopicType();

        presenter.update();

        verify(view, never()).openErrorPopupInTopicTypeDialog(anyString());
        verify(topicTypeService).updateTopicType(any(TopicType.class));
    }

    @Test
    public void updateTestError() throws NotUniqueException {
        givenTopicTypeWithEmptyTitle();

        presenter.update();

        verify(view).openErrorPopupInTopicTypeDialog(ERROR_TOPICTYPE_TITLE_CANT_BE_VOID);
        verify(topicTypeService, never()).updateTopicType(any(TopicType.class));
    }

    private void givenTopicType() {
        when(view.getTypeTitle()).thenReturn(title);
        when(view.getTypeDescription()).thenReturn(desc);
    }
    
    private void givenTopicTypeWithEmptyTitle() {
        when(view.getTypeTitle()).thenReturn("");
        when(view.getTypeDescription()).thenReturn(desc);
    }
}
