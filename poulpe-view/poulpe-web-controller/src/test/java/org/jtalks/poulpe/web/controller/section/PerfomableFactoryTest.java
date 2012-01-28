package org.jtalks.poulpe.web.controller.section;

import static org.jtalks.poulpe.web.controller.utils.ObjectCreator.fakeSection;
import static org.jtalks.poulpe.web.controller.utils.ObjectCreator.sectionWithBranches;
import static org.mockito.Mockito.verify;

import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Section;
import org.jtalks.poulpe.service.SectionService;
import org.jtalks.poulpe.web.controller.DialogManager.Performable;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Alexey Grigorev
 */
public class PerfomableFactoryTest {

    private PerfomableFactory perfomableFactory;
    
    @Mock SectionPresenter presenter;
    @Mock SectionService service;
    @Mock SectionViewImpl view;
    @Mock SectionTreeComponentImpl currentSectionTreeComponent;

    private Section section = sectionWithBranches();
    private Branch branch = section.getBranches().get(0);
    
    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);

        perfomableFactory = new PerfomableFactory(presenter);
        perfomableFactory.setCurrentSectionTreeComponent(currentSectionTreeComponent);
        perfomableFactory.setSectionService(service);
        perfomableFactory.setSectionView(view);
    }
    
    @Test
    public void testCreatePerformableCreateSection() {
        Performable perf = perfomableFactory.saveSection(section);
        
        perf.execute();

        verify(view).showSection(section);
        verify(view).closeNewSectionDialog();
        verify(service).saveSection(section);
    }

    @Test
    public void testUpdatePerformableEditSection() {
        Performable perf = perfomableFactory.updateSection(section);
        
        perf.execute();

        verify(currentSectionTreeComponent).updateSectionInView(section);
        verify(view).closeEditSectionDialog();
        verify(service).saveSection(section);
    }

    @Test
    public void testDeleteBranchPerformable() {
        Performable perf = perfomableFactory.deleteBranch(branch);
        perf.execute();
        verify(service).saveSection(section);
        verify(presenter).updateView();
    }

    @Test
    public void testDeleteSectionSaveBranchesPerformable() {
        Section section1 = fakeSection(), section2 = fakeSection();
        Performable perf = perfomableFactory.deleteSection(section1, section2);

        perf.execute();

        verify(service).deleteAndMoveBranchesTo(section1, section2);
    }

    @Test
    public void testDeleteSectionWithoutSaveBranchesPerformable() {
        Performable perf = perfomableFactory.deleteSection(section, null);

        perf.execute();
        verify(service).deleteRecursively(section);
    }

}
