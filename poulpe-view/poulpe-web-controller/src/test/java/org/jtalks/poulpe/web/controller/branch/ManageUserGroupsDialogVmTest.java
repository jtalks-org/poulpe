package org.jtalks.poulpe.web.controller.branch;

import org.jtalks.poulpe.model.entity.Group;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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
        vm = new ManageUserGroupsDialogVm();
    }

    @Test(dataProvider = "testGroups")
    public void testMoveSelectedToAddedGroups(List<Group> testGroups) throws Exception {
        vm.setAvailableGroups(testGroups);
        vm.getAvailableGroups().addSelection(testGroups.get(0));
        Set<Group> movedGroups = vm.moveSelectedToAddedGroups();
        //check sizes of after moving the moving
        assertEquals(movedGroups.size(), 1);
        assertEquals(vm.getAvailableGroups().size(), 1);
        assertEquals(vm.getAddedGroups().size(), 1);
        //check elements actually moved
        assertSame(movedGroups.iterator().next(), testGroups.get(0));
        assertSame(vm.getAvailableGroups().iterator().next(), testGroups.get(1));
        assertSame(vm.getAddedGroups().iterator().next(), testGroups.get(0));
    }

    @Test(dataProvider = "testGroups")
    public void testMoveSelectedToAddedGroups_withNoSelection(List<Group> testGroups) throws Exception {
        vm.setAvailableGroups(testGroups);
        Set<Group> movedGroups = vm.moveSelectedToAddedGroups();
        assertTrue(movedGroups.isEmpty());
        assertEquals(vm.getAvailableGroups().size(), 2);
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
        vm.getAddedGroups().addSelection(testGroups.get(1));
        Set<Group> movedGroups = vm.moveSelectedFromAddedGroups();
        //check sizes of after moving the moving
        assertEquals(movedGroups.size(), 1);
        assertEquals(vm.getAvailableGroups().size(), 1);
        assertEquals(vm.getAddedGroups().size(), 1);
        //check elements actually moved
        assertSame(movedGroups.iterator().next(), testGroups.get(1));
        assertSame(vm.getAvailableGroups().iterator().next(), testGroups.get(1));
        assertSame(vm.getAddedGroups().iterator().next(), testGroups.get(0));
    }

    @Test(dataProvider = "testGroups")
    public void testMoveSelectedFromAddedGroups_withNoSelection(List<Group> testGroups) throws Exception {
        vm.setAddedGroups(testGroups);
        vm.getAddedGroups();
        Set<Group> movedGroups = vm.moveSelectedFromAddedGroups();

        assertTrue(movedGroups.isEmpty());
        assertTrue(vm.getAvailableGroups().isEmpty());
        assertEquals(vm.getAddedGroups().size(), 2);
    }

    @Test
    public void testMoveSelectedFromAddedGroups_withNoElements() throws Exception {
        Set<Group> movedGroups = vm.moveSelectedFromAddedGroups();
        assertTrue(movedGroups.isEmpty());
        assertEquals(vm.getAvailableGroups().size(), 0);
        assertEquals(vm.getAddedGroups().size(), 0);
    }

    @Test(dataProvider = "testGroups")
    public void testMoveAllToAddedGroups(List<Group> groups) throws Exception {
        vm.setAvailableGroups(groups).getAvailableGroups().addSelection(groups.get(1));
        Set<Group> moved = vm.moveAllToAddedGroups();
        assertTrue(vm.getAvailableGroups().isEmpty());
        assertEquals(moved.size(), 2);
        assertEquals(vm.getAddedGroups().size(), 2);
    }

    @Test
    public void testMoveAllToAddedGroups_withEmptyList() throws Exception {
        Set<Group> moved = vm.moveAllToAddedGroups();
        assertTrue(vm.getAvailableGroups().isEmpty());
        assertTrue(moved.isEmpty());
        assertTrue(vm.getAddedGroups().isEmpty());
    }


    @Test(dataProvider = "testGroups")
    public void testMoveAllFromAddedGroups(List<Group> groups) throws Exception {
        vm.setAddedGroups(groups).getAddedGroups().addSelection(groups.get(1));
        Set<Group> moved = vm.moveAllFromAddedGroups();
        assertTrue(vm.getAddedGroups().isEmpty());
        assertEquals(vm.getAvailableGroups().size(), 2);
        assertEquals(moved.size(), 2);
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
        return new Object[][]{{Group.createGroupsWithNames("1", "2")}};
    }
}
