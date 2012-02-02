package org.jtalks.poulpe.web.controller.section;

import static org.jtalks.poulpe.web.controller.utils.ObjectCreator.fakeBranch;
import static org.jtalks.poulpe.web.controller.utils.ObjectCreator.sectionWithBranches;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Section;
import org.jtalks.poulpe.web.controller.ZkInitializer;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.zkoss.zul.Button;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.Tree;


/**
 * @author Alexey Grigorev
 */
public class ZkSectionTreeComponentTest {
    
    private ZkSectionTreeComponent sectionTreeComponent;
    private ZkSectionTreeComponent spy;

    @Mock SectionPresenter presenter;
    @Mock ZkInitializer zkInitializer;
    @Mock Tree sectionTree;
    
    private Section section = sectionWithBranches();
    private Branch branch = fakeBranch();

    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);
        
        sectionTreeComponent = new ZkSectionTreeComponent(zkInitializer);
        sectionTreeComponent.setSectionTree(sectionTree);
        sectionTreeComponent.init(section, presenter);
        
        spy = spy(sectionTreeComponent);
    }

    @Test
    public void initialization() {
        verify(zkInitializer).wireByConvention();
        verify(zkInitializer).wireToZul(ZkSectionTreeComponent.ZUL_REF);
    }
    
    @Test
    public void sectionTreeInitialized() {
        verify(sectionTree).setModel(any(DefaultTreeModel.class));
    }

    @Test
    public void newBranchDialog() {
        sectionTreeComponent.newBranchDialog();
        verify(presenter).openNewBranchDialog(sectionTreeComponent);
    }

    @Test
    public void editDialog() {
        sectionTreeComponent.editDialog();
        verify(presenter).openEditDialog(sectionTreeComponent);
    }

    @Test
    public void moderationButton() {
        sectionTreeComponent.moderationDialog();
        verify(presenter).openModerationWindow();
    }

    @Test
    public void deleteDialog() {
        doReturn(section).when(spy).getSelectedOrFirstElement();
        spy.deleteDialog();
        verify(presenter).openDeleteDialog(section);
    }

    @Test
    public void disablePermissionsButtonWhenSection() {
        Button permissionsButton = mockPermissionButton();
        sectionTreeComponent.disablePermissionsButtonIfNeeded(section);
        verify(permissionsButton).setDisabled(true);
    }
    
    @Test
    public void enablePermissionsButtonWhenBranch() {
        Button permissionsButton = mockPermissionButton();
        sectionTreeComponent.disablePermissionsButtonIfNeeded(branch);
        verify(permissionsButton).setDisabled(false);
    }

    private Button mockPermissionButton() {
        Button permissionsButton = mock(Button.class);
        when(sectionTree.getFellow("permissionsButton")).thenReturn(permissionsButton);
        return permissionsButton;
    }
}
