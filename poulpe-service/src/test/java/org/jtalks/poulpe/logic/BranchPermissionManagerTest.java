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
