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
package org.jtalks.poulpe.model.entity;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jtalks.poulpe.model.entity.User;
import org.jtalks.poulpe.model.dao.hibernate.ObjectsFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

public class BranchModeratorsTest {
    
    private Branch branch = new Branch();
    
    private User user1 = ObjectsFactory.createUser();
    private User user2 = ObjectsFactory.createUser();
    private User user3 = ObjectsFactory.createUser();
    
    @BeforeMethod
    public void setUp() {
        branch.setModerators(Lists.newArrayList(user1));
    }
    
    @Test
    public void isModeratedBy() {
        assertTrue(branch.isModeratedBy(user1));
    }
    
    @Test
    public void addModerator() {
        branch.addModerator(user3);
        assertTrue(branch.isModeratedBy(user3));
    }
    
    @Test
    public void addModerators() {
        branch.addModerators(user2, user3);
        assertTrue(branch.isModeratedBy(user2));
        assertTrue(branch.isModeratedBy(user3));
    }

    @Test
    public void removeModerator() {
        branch.removeModerator(user1);
        assertFalse(branch.isModeratedBy(user1));
    }

}
