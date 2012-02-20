package org.jtalks.poulpe.web.controller.section;

import static org.jtalks.poulpe.web.controller.utils.ObjectCreator.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.util.List;

import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.web.controller.ZkHelper;
import org.jtalks.poulpe.web.controller.section.ZkDeleteSectionDialogView.SectionComboboxItemRenderer;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;

/**
 * @author Alexey Grigorev
 */
public class ZkDeleteSectionDialogViewTest {

    ZkDeleteSectionDialogView deleteSectionDialogView;
    
    @Mock ZkHelper zkInitializer;

    @Mock DeleteSectionDialogPresenter presenter;
    
    @Mock Radiogroup deleteMode;
    @Mock Radio removeAndMoveMode;
    @Mock Combobox selectedSection;
    
    private PoulpeSection section = fakeSection();
    
    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);
        
        deleteSectionDialogView = new ZkDeleteSectionDialogView();
        deleteSectionDialogView.setZkHelper(zkInitializer);
        deleteSectionDialogView.setPresenter(presenter);
        deleteSectionDialogView.setUiElements(deleteMode, removeAndMoveMode, selectedSection);
        
        initModel();
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void initModel() {
        ListModel model = mock(ListModelList.class);
        when(selectedSection.getModel()).thenReturn(model);
    }
    
    @Test
    public void afterCompose() {
        deleteSectionDialogView.afterCompose();
        
        verify(zkInitializer).wireByConvention();
        verify(presenter).setView(deleteSectionDialogView);
        verify(selectedSection).setItemRenderer(any(SectionComboboxItemRenderer.class));
    }
    
    @Test 
    public void showDialog() {
        deleteSectionDialogView.showDialog(section);
        assertEquals(deleteSectionDialogView.getSectionToDelete(), section);
    }
    
    @Test 
    public void dialogBecomesVisible() {
        deleteSectionDialogView.showDialog();
        assertTrue(deleteSectionDialogView.isVisible());
    }
    
    @Test 
    public void showDialogInit() {
        deleteSectionDialogView.showDialog();
        
        verify(presenter).refreshSectionsCombobox();
        verify(deleteMode).setSelectedIndex(0);
    }
    
    @Test 
    public void initSectionsCombobox() {
        List<PoulpeSection> sections = fakeSections();
        
        deleteSectionDialogView.initSectionsCombobox(sections);
        
        verify(selectedSection).setModel(isA(ListModelList.class));
        verify(selectedSection).setDisabled(false);
        verify(selectedSection).setRawValue(null);
        
        verify(removeAndMoveMode).setDisabled(false);
    }
    
    @Test 
    public void initEmptyAndDisabledCombobox() {
        deleteSectionDialogView.initEmptyAndDisabledCombobox();
        
        verify(selectedSection).setModel(null);
        verify(selectedSection).setDisabled(true);
        
        verify(removeAndMoveMode).setDisabled(true);
    }
    
    @Test 
    public void closeDialog() {
        deleteSectionDialogView.closeDialog();
        assertFalse(deleteSectionDialogView.isVisible());
    }
    
    @Test 
    public void deleteSection() {
        deleteSectionDialogView.deleteSection();
        verify(presenter).delete();
    }
    
    @Test 
    public void sectionComboboxItemRenderer() throws Exception {
        SectionComboboxItemRenderer renderer = new SectionComboboxItemRenderer();
        Comboitem item = mock(Comboitem.class);
        
        renderer.render(item, section, 0);
        
        verify(item).setLabel(section.getName());
        verify(item).setDescription(section.getDescription());
    }

}
