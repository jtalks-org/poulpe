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
package org.jtalks.poulpe.web.controller.group;

import org.jtalks.common.model.entity.Group;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * @author Leonid Kazancev
 */
public class BranchGroupMapTest {
    private static final String FIELD_ITERATOR = "iterator";
    private BranchGroupMap branchGroupMap;
    private List<ModeratingGroupComboboxRow> branchesCollection;

    private Group wasModeratorGroup;
    private Group groupToSet;

    private List<PoulpeBranch> branchesList;
    private List<Group> groupsList;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        branchesList = provideRandomBranchesList();
        groupsList = provideRandomGroupsList();
        branchGroupMap = new BranchGroupMap(branchesList, groupsList);
        branchesCollection = branchGroupMap.getBranchesCollection();

        wasModeratorGroup = new Group("iAmOld");
        groupToSet = new Group("groupToSet");
    }

    @Test
    public void constructionTestAvalableGroups() throws NoSuchFieldException, IllegalAccessException {
        List<Group> allAvalableGroups = branchGroupMap.getAllAvailableGroups();
        int expectedSize = groupsList.size();
        int actualSize = allAvalableGroups.size();
        assertNotNull(allAvalableGroups);
        assertNotSame(allAvalableGroups, groupsList);
        assertEquals(actualSize, expectedSize);
        for (int i = 0; i < actualSize; i++) {
            Group expectedGroup = groupsList.get(i);
            Group actualGroup = allAvalableGroups.get(i);
            assertSame(actualGroup, expectedGroup);
        }
    }

    @Test
    public void constructionTestBranchesCollections() throws NoSuchFieldException, IllegalAccessException {
        int expectedSize = branchesList.size();
        int actualSize = branchesCollection.size();

        assertNotNull(branchesCollection);
        assertNotSame(branchesCollection, branchesList);
        assertEquals(actualSize, expectedSize);
        for (int i = 0; i < actualSize; i++) {
            PoulpeBranch expectedBranch = branchesList.get(i);
            PoulpeBranch actualBranch = branchesCollection.get(i).getCurrentBranch();
            assertSame(actualBranch, expectedBranch);
        }
    }

    @Test
    public void setSelectedGroupForAllBranchesTest() {
        branchGroupMap.setSelectedGroupForAllBranches(groupToSet);

        for (ModeratingGroupComboboxRow item : branchesCollection) {
            Group selectedGroup = item.getSelectedGroup();
            assertSame(groupToSet, selectedGroup);
        }
    }

    @Test
    public void setModeratingGroupForAllBranchesTest() throws NoSuchFieldException, IllegalAccessException {
        branchGroupMap.setSelectedGroupForAllBranches(groupToSet);
        PoulpeBranch firstBranch = branchesCollection.get(0).getCurrentBranch();
        PoulpeBranch secondBranch = branchesCollection.get(1).getCurrentBranch();

        branchGroupMap.setModeratingGroupForAllBranches(wasModeratorGroup);
        assertNotNull(getIterator());

        assertTrue(branchesCollection.isEmpty());
        verify(firstBranch).setModeratorsGroup(groupToSet);
        verify(secondBranch).setModeratorsGroup(groupToSet);
    }

    @Test
    public void setModeratingGroupForCurrentBranchTest() throws NoSuchFieldException, IllegalAccessException {
        branchGroupMap.setSelectedGroupForAllBranches(groupToSet);
        PoulpeBranch firstBranch = branchesCollection.get(0).getCurrentBranch();
        PoulpeBranch secondBranch = branchesCollection.get(1).getCurrentBranch();

        int branchesWaitingForChangesCount = branchesCollection.size();
        branchGroupMap.setModeratingGroupForCurrentBranch(wasModeratorGroup, firstBranch);
        assertNotNull(getIterator());

        if (branchesWaitingForChangesCount == 0) {
            assertEquals(branchesWaitingForChangesCount, 0);
        } else {
            assertEquals(branchesCollection.size(), branchesWaitingForChangesCount - 1);
        }
        verify(firstBranch).setModeratorsGroup(groupToSet);
        verify(secondBranch, times(0)).setModeratorsGroup(any(Group.class));
    }


    private List<Group> provideRandomGroupsList() {
        return Arrays.asList(new Group("2"), new Group("3"));
    }

    private List<PoulpeBranch> provideRandomBranchesList() {
        PoulpeBranch firstBranch = spy(new PoulpeBranch("2"));
        PoulpeBranch secondBranch = spy(new PoulpeBranch("3"));
        doNothing().when(firstBranch).setModeratorsGroup(any(Group.class));
        doNothing().when(secondBranch).setModeratorsGroup(any(Group.class));
        return Arrays.asList(firstBranch, secondBranch);
    }

    private Object getFieldValue(String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Class branchGroupMapClass = branchGroupMap.getClass();
        Field field = branchGroupMapClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(branchGroupMap);
    }

    private Iterator getIterator() throws NoSuchFieldException, IllegalAccessException {
        return (Iterator) getFieldValue(FIELD_ITERATOR);
    }


}
