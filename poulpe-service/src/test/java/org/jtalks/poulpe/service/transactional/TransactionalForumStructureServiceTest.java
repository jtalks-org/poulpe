package org.jtalks.poulpe.service.transactional;

import org.jtalks.common.model.entity.ComponentType;
import org.jtalks.poulpe.model.dao.BranchDao;
import org.jtalks.poulpe.model.dao.ComponentDao;
import org.jtalks.poulpe.model.dao.SectionDao;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertFalse;

/**
 * @author stanislav bashkirtsev
 */
public class TransactionalForumStructureServiceTest {
    TransactionalForumStructureService sut;
    private SectionDao sectionDao;
    private BranchDao branchDao;
    private ComponentDao componentDao;


    @BeforeMethod
    public void setUp() throws Exception {
        componentDao = mock(ComponentDao.class);
        branchDao = mock(BranchDao.class);
        sectionDao = mock(SectionDao.class);
        sut = new TransactionalForumStructureService(sectionDao, branchDao, componentDao);
    }

    @Test(dataProvider = "provideJcommuneWithSectionsAndBranches")
    public void testDeleteSectionWithBranches(Jcommune jcommune) throws Exception {
        PoulpeSection sectionToRemove = jcommune.getSections().get(0);
        doReturn(jcommune).when(componentDao).getByType(ComponentType.FORUM);
        sut.deleteSectionWithBranches(sectionToRemove);
        assertFalse(jcommune.getSections().contains(sectionToRemove));
        verify(componentDao).saveOrUpdate(jcommune);
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testDeleteSectionAndMoveBranches() throws Exception {
        sut.deleteSectionAndMoveBranches(null, null);
    }

    @DataProvider
    private Object[][] provideJcommuneWithSectionsAndBranches() {
        Jcommune jcommune = new Jcommune();
        PoulpeSection sectionA = new PoulpeSection("SectionA");
        sectionA.addOrUpdateBranch(new PoulpeBranch("BranchA"));
        sectionA.addOrUpdateBranch(new PoulpeBranch("BranchB"));
        jcommune.addSection(sectionA);
        PoulpeSection sectionB = new PoulpeSection("SectionB");
        sectionB.addOrUpdateBranch(new PoulpeBranch("BranchD"));
        sectionB.addOrUpdateBranch(new PoulpeBranch("BranchE"));
        jcommune.addSection(sectionB);
        return new Object[][]{{jcommune}};
    }
}
