package org.jtalks.poulpe.web.controller.section.mvvm;

import com.google.common.collect.Lists;
import org.jtalks.common.model.entity.ComponentType;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.service.ComponentService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.zkoss.zul.ListModelList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

/**
 * @author stanislav bashkirtsev
 */
public class SectionsAndBranchesVmTest {
    private ComponentService componentService;
    private SectionsAndBranchesVm vm;

    @BeforeMethod
    public void setUp() throws Exception {
        componentService = mock(ComponentService.class);
        vm = new SectionsAndBranchesVm(componentService);
    }

    @Test(dataProvider = "provideRandomJcommuneWithSections")
    public void testGetSections(Jcommune jcommune) throws Exception {
        when(componentService.getByType(ComponentType.FORUM)).thenReturn(jcommune);
        ListModelList<PoulpeSection> sectionsList = vm.getSections();
        assertEquals(sectionsList.size(), jcommune.getSections().size());
        assertSame(sectionsList.get(0), jcommune.getSections().get(0));
    }

    @DataProvider
    public Object[][] provideRandomJcommuneWithSections() {
        Jcommune jcommune = new Jcommune();
        jcommune.setSections(Lists.newArrayList(new PoulpeSection()));
        return new Object[][]{{jcommune}};
    }
}
