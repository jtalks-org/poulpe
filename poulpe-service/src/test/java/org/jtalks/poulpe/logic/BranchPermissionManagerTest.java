package org.jtalks.poulpe.logic;

import org.aspectj.lang.annotation.Before;
import org.jtalks.common.security.acl.AclManager;
import org.jtalks.poulpe.model.dao.BranchDao;
import org.jtalks.poulpe.model.dao.GroupDao;
import org.jtalks.poulpe.model.entity.Branch;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.mockito.Mockito.when;

/**
 * @author stanislav bashkirtsev
 */
public class BranchPermissionManagerTest {
    @Mock
    GroupDao groupDao;
    @Mock
    AclManager aclManager;
    BranchPermissionManager manager;

    @BeforeMethod
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
        manager = new BranchPermissionManager(aclManager, groupDao);
    }
    @Test
    public void testGetGroupAccessListFor() throws Exception {
//        when(aclManager.getBranchPermissions(branch)).thenReturn()
    }
    
    @DataProvider
    public Object[][] provide(){
        return new Object[][]{{new Branch()}};
    }
}
