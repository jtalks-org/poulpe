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
package org.jtalks.poulpe.web.controller.branch;

import org.jtalks.common.model.entity.Group;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.*;

/**
 * @author stanislav bashkirtsev
 */
public class ManageUserGroupsDialogVmTest {
    private ManageUserGroupsDialogVm vm;

    @BeforeMethod
    protected void setUp() throws Exception {
        vm = new ManageUserGroupsDialogVm(null, true);
    }

    @Test(dataProvider = "testGroups")
    public void testGetRemovedFromAdded(List<Group> testGroups) throws Exception {
        vm.setAddedGroups(testGroups);
        moveFromAdded(vm, testGroups.get(0));
        Collection<Group> removedFromAdded = vm.getRemovedFromAdded();
        assertEquals(removedFromAdded.size(), 1);
        assertSame(removedFromAdded.iterator().next(), testGroups.get(0));
    }

    @Test(dataProvider = "testGroups")
    public void testGetNewInAdded(List<Group> testGroups) throws Exception {
        vm.setAvailableGroups(testGroups);
        moveToAdded(vm, testGroups.get(1));
        Collection<Group> newInAdded = vm.getNewAdded();
        assertEquals(newInAdded.size(), 1);
        assertSame(newInAdded.iterator().next(), testGroups.get(1));
    }

    /**
     * <pre>
     * 1.Adds elements to the Added Groups.
     * 2.Moves some element from Added Groups.
     * 3.Moves the same element back.
     * 4.Checks that {@link ManageUserGroupsDialogVm#getNewAdded()} returns empty list.
     * </pre>
     *
     * @param testGroups 2 random test groups
     * @throws Exception who would care
     */
    @Test(dataProvider = "testGroups")
    public void testGetNewInAdded_afterElementReturnedBack(List<Group> testGroups) throws Exception {
        vm.setAddedGroups(testGroups);
        moveFromAdded(vm, testGroups.get(0));
        moveToAdded(vm, testGroups.get(0));

        assertTrue(vm.getNewAdded().isEmpty());
    }

    @Test(dataProvider = "testGroups")
    public void testMoveSelectedToAddedGroups(List<Group> testGroups) throws Exception {
        vm.setAvailableGroups(testGroups);
        Collection<Group> movedGroups = moveToAdded(vm, testGroups.get(0));
        //check sizes of after moving the moving
        assertEquals(movedGroups.size(), 1);
        assertEquals(vm.getAvailableGroups().size(), testGroups.size() - 1);
        assertEquals(vm.getAddedGroups().size(), 1);
        //check elements actually moved
        assertSame(movedGroups.iterator().next(), testGroups.get(0));
        assertSame(vm.getAvailableGroups().iterator().next(), testGroups.get(1));
        assertSame(vm.getAddedGroups().iterator().next(), testGroups.get(0));
    }

    @Test(dataProvider = "testGroups")
    public void testMoveSelectedToAddedGroups_withNoSelection(List<Group> testGroups) throws Exception {
        vm.setAvailableGroups(testGroups);
        Collection<Group> movedGroups = vm.moveSelectedToAddedGroups();
        assertTrue(movedGroups.isEmpty());
        assertEquals(vm.getAvailableGroups().size(), testGroups.size());
        assertEquals(vm.getAddedGroups().size(), 0);
    }

    @Test
    public void testMoveSelectedToAddedGroups_withNoElements() throws Exception {
        Set<Group> movedGroups = vm.moveSelectedToAddedGroups();
        assertTrue(movedGroups.isEmpty());
        assertEquals(vm.getAvailableGroups().size(), 0);
        assertEquals(vm.getAddedGroups().size(), 0);
    }

    @Test(dataProvider = "testGroups")
    public void testMoveSelectedFromAddedGroups(List<Group> testGroups) throws Exception {
        vm.setAddedGroups(testGroups);
        Collection<Group> movedGroups = moveFromAdded(vm, testGroups.get(1));
        //check sizes of after moving the moving
        assertEquals(movedGroups.size(), 1);
        assertEquals(vm.getAvailableGroups().size(), 1);
        assertEquals(vm.getAddedGroups().size(), testGroups.size() - 1);
        //check elements actually moved
        assertSame(movedGroups.iterator().next(), testGroups.get(1));
        assertSame(vm.getAvailableGroups().iterator().next(), testGroups.get(1));
        assertSame(vm.getAddedGroups().iterator().next(), testGroups.get(0));
    }

    @Test(dataProvider = "testGroups")
    public void testMoveSelectedFromAddedGroups_withNoSelection(List<Group> testGroups) throws Exception {
        vm.setAddedGroups(testGroups);
        vm.getAddedGroups();
        Collection<Group> movedGroups = vm.moveSelectedFromAddedGroups();

        assertTrue(movedGroups.isEmpty());
        assertTrue(vm.getAvailableGroups().isEmpty());
        assertEquals(vm.getAddedGroups().size(), testGroups.size());
    }

    @Test
    public void testMoveSelectedFromAddedGroups_withNoElements() throws Exception {
        Set<Group> movedGroups = vm.moveSelectedFromAddedGroups();
        assertTrue(movedGroups.isEmpty());
        assertEquals(vm.getAvailableGroups().size(), 0);
        assertEquals(vm.getAddedGroups().size(), 0);
    }

    /**
     * <pre>
     * 1. Fill the list of added.
     * 2. Select some of elements just for to ensure this doesn't impact anything
     * 3. Check all elements moved to added list
     * </pre>
     *
     * @param testGroups 2 random test groups
     * @throws Exception who cares
     */
    @Test(dataProvider = "testGroups")
    public void testMoveAllToAddedGroups(List<Group> testGroups) throws Exception {
        vm.setAvailableGroups(testGroups).getAvailableGroups().addToSelection(testGroups.get(1));
        Set<Group> moved = vm.moveAllToAddedGroups();
        assertTrue(vm.getAvailableGroups().isEmpty());
        assertEquals(moved.size(), testGroups.size());
        assertEquals(vm.getAddedGroups().size(), testGroups.size());
    }

    @Test
    public void testMoveAllToAddedGroups_withEmptyList() throws Exception {
        Set<Group> moved = vm.moveAllToAddedGroups();
        assertTrue(vm.getAvailableGroups().isEmpty());
        assertTrue(moved.isEmpty());
        assertTrue(vm.getAddedGroups().isEmpty());
    }


    @Test(dataProvider = "testGroups")
    public void testMoveAllFromAddedGroups(List<Group> testGroups) throws Exception {
        vm.setAddedGroups(testGroups).getAddedGroups().addToSelection(testGroups.get(1));
        Set<Group> moved = vm.moveAllFromAddedGroups();
        assertTrue(vm.getAddedGroups().isEmpty());
        assertEquals(vm.getAvailableGroups().size(), testGroups.size());
        assertEquals(moved.size(), testGroups.size());
    }

    @Test
    public void testMoveAllFromAddedGroups_withEmptyList() throws Exception {
        Set<Group> moved = vm.moveAllFromAddedGroups();
        assertTrue(vm.getAvailableGroups().isEmpty());
        assertTrue(vm.getAddedGroups().isEmpty());
        assertTrue(moved.isEmpty());
    }

    @DataProvider(name = "testGroups")
    public Object[][] provideTestGroups() {
        return new Object[][]{{Group.createGroupsWithNames("1", "2", "3")}};
    }

    /**
     * Selects the element at added groups and invokes {@link ManageUserGroupsDialogVm#moveSelectedFromAddedGroups()} to
     * make the view model move the elements from the Added List.
     *
     * @param vm     a view model to change its lists
     * @param toMove an element to move from the added list to the list of available groups
     * @return the groups been moved
     */
    private Collection<Group> moveFromAdded(ManageUserGroupsDialogVm vm, Group toMove) {
        vm.getAddedGroups().addToSelection(toMove);
        return vm.moveSelectedFromAddedGroups();
    }

    /**
     * Selects the element in available groups and invokes {@link ManageUserGroupsDialogVm#moveSelectedToAddedGroups ()}
     * to make the view model move the elements to the Added List.
     *
     * @param vm     a view model to change its lists
     * @param toMove an element to move from the available list to the list of added groups
     * @return the groups been moved
     */
    private Collection<Group> moveToAdded(ManageUserGroupsDialogVm vm, Group toMove) {
        vm.getAvailableGroups().addToSelection(toMove);
        return vm.moveSelectedToAddedGroups();
    }
}
