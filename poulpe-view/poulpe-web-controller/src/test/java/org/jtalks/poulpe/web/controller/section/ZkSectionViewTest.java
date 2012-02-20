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

import static org.testng.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Matchers.*;

import org.jtalks.poulpe.validator.ValidationFailureHandler;
import org.jtalks.poulpe.web.controller.ZkHelper;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ZkSectionViewTest {
    
    private ZkSectionView sectionView;

    @Mock SectionPresenter presenter;
    @Mock ValidationFailureHandler handler;
    @Mock ZkHelper zkHelper;
    
    @Mock Window editSectionDialog;
    @Mock Textbox sectionName;
    @Mock Textbox sectionDescription;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        sectionView = new ZkSectionView();
        
        sectionView.setPresenter(presenter);
        sectionView.setValidationFailureHandler(handler);
        sectionView.setZkHelper(zkHelper);
        
        sectionView.setUiElements(editSectionDialog, sectionName, sectionDescription);
    }
    
    @Test
    public void afterCompose() {
        sectionView.afterCompose();
        
        verify(zkHelper).wireByConvention();
        verify(presenter).initView(sectionView);
        verify(editSectionDialog).setVisible(false);
    }
    
    
}
