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
package org.jtalks.poulpe.web.controller.section;

import static org.jtalks.poulpe.web.controller.utils.ObjectCreator.fakeSection;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;

import org.jtalks.common.validation.ValidationResult;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.validator.ValidationFailureHandler;
import org.jtalks.poulpe.web.controller.ZkHelper;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * Tests for {@link ZkSectionView}
 * 
 * @author Alexey Grigorev
 */
public class ZkSectionViewTest {

    private ZkSectionView sectionView;

    @Mock SectionPresenter presenter;
    @Mock ValidationFailureHandler handler;
    @Mock ZkHelper zkHelper;
    @Mock TreeComponentFactory treeComponentFactory;
    
    @Mock Window editSectionDialog;
    @Mock Textbox sectionName;
    @Mock Textbox sectionDescription;

    PoulpeSection section = fakeSection();
    List<PoulpeSection> sections = Collections.singletonList(section);

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        sectionView = new ZkSectionView();

        sectionView.setPresenter(presenter);
        
        sectionView.setTestDependencies(handler, zkHelper, treeComponentFactory);
        sectionView.setUiElements(editSectionDialog, sectionName, sectionDescription);
    }

    @Test
    public void afterCompose() {
        sectionView.afterCompose();

        verify(zkHelper).wireByConvention();
        verify(presenter).initView(sectionView);
        verify(editSectionDialog).setVisible(false);
    }

    @Test
    public void validationFailure() {
        sectionView.validationFailure(ValidationResult.EMPTY);
        verify(handler).validationFailure(ValidationResult.EMPTY);
    }
    
    @Test
    public void showSection() {
        sectionView.addSection(section);
        
        verify(zkHelper).addComponent(any(ZkSectionTreeComponent.class));
        verify(treeComponentFactory).sectionTreeComponent(section);
    }
    
    @Test
    public void showSections() {
        sectionView.addSections(sections);
        
        verify(zkHelper).removeAll(ZkSectionTreeComponent.class);
        verify(zkHelper).addComponent(any(ZkSectionTreeComponent.class));
        verify(treeComponentFactory).sectionTreeComponent(section);
    }
    
    @Test
    public void openNewSectionDialog() {
        sectionView.openNewSectionDialog();
        
        verify(sectionName).setText("");
        verify(sectionDescription).setText("");
        verify(editSectionDialog).setVisible(true);
    }
    
    @Test
    public void openEditSectionDialog() {
        sectionView.openEditSectionDialog(section);
        
        verify(sectionName).setText(section.getName());
        verify(sectionDescription).setText(section.getDescription());
        verify(editSectionDialog).setVisible(true);
    }

    @Test
    public void save() {
        sectionView.openNewSectionDialog();
        sectionView.save();
        verify(presenter).addNewSection(anyString(), anyString());
    }
    
    @Test
    public void edit() {
        sectionView.openEditSectionDialog(section);
        sectionView.save();
        verify(presenter).editSection(anyString(), anyString());
    }
}
