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

import java.util.ArrayList;
import java.util.List;

import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Section;
import org.jtalks.poulpe.service.SectionService;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Bekrenev Dmitry
 * */

public class TestDeleteSectionDialogPresenter {
    
    DeleteSectionDialogPresenter presenter = new DeleteSectionDialogPresenter();
    
    @Mock SectionService    sectionService;
    @Mock DeleteSectionDialogView view;
    
    @Captor ArgumentCaptor<Section> deleteSectionCaptor;
    @Captor ArgumentCaptor<Section> selectedSectionCaptor;
    @Captor ArgumentCaptor<List<Section>> initSectionCaptor;
    
    @BeforeMethod
    public void setUp(){
        MockitoAnnotations.initMocks(this); 
        presenter.setView(view);
        presenter.setSectionService(sectionService);
    }
    
    @Test
    public void testInitView(){
      List<Section> sections = new ArrayList<Section>();
      
      sections.add(new Section());
      sections.add(new Section());
      sections.add(new Section());
      sections.add(new Section());
      
      when(sectionService.getAll()).thenReturn(sections);
      
      presenter.initView();
      
      verify(view).initSectionList(initSectionCaptor.capture());
      assertEquals(initSectionCaptor.getValue().size(), 4);
      
    }
    
    @Test
    public void testDeleteIfmodeDeleteAll(){
        Section deleteSection = new Section();
        deleteSection.setName("Section for delete");
        deleteSection.addOrUpdateBranch(new Branch());
        deleteSection.addOrUpdateBranch(new Branch());
        when(view.getDeleteMode()).thenReturn("deleteAll");
        when(view.getDeleteSection()).thenReturn(deleteSection);
        
        presenter.delete();
        
        verify(sectionService).deleteRecursively(deleteSectionCaptor.capture());
        assertEquals(deleteSectionCaptor.getValue().getName(), "Section for delete");
        verify(view).closeDialog();
        verify(view).getDeleteMode();
    }
    
    @Test
    public void testDeleteIfmodeMoveBranches(){
        Section deleteSection = new Section();
        Section selectedSection = new Section();
        
        selectedSection.setName("Selected section");
        deleteSection.setName("Sect1ion for delete");


        when(view.getDeleteMode()).thenReturn("deleteAndMoveBranches");
        when(view.getDeleteSection()).thenReturn(deleteSection);
        when(view.getSelectedSection()).thenReturn(selectedSection);
        
        presenter.delete();
        verify(sectionService).deleteAndMoveBranchesTo(deleteSectionCaptor.capture(), selectedSectionCaptor.capture());
        assertEquals(deleteSectionCaptor.getValue().getName(), deleteSection.getName());
        assertEquals(selectedSectionCaptor.getValue().getName(), selectedSection.getName());
        verify(view).closeDialog();
        verify(view).getDeleteMode();
    }
}
