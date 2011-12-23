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
    public void testMoveToAddedAndReturnMoved(List<Group> testGroups) throws Exception {
        vm.setAvailableGroups(testGroups);
        vm.getAvailableGroups().addSelection(testGroups.get(0));
        Set<Group> movedGroups = vm.moveToAddedAndReturnMoved();
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
    public void testMoveToAddedAndReturnMoved_withNoSelection(List<Group> testGroups) throws Exception {
        vm.setAvailableGroups(testGroups);
        Set<Group> movedGroups = vm.moveToAddedAndReturnMoved();
        assertTrue(movedGroups.isEmpty());
        assertEquals(vm.getAvailableGroups().size(), 2);
        assertEquals(vm.getAddedGroups().size(), 0);
    }

    @Test
    public void testMoveToAddedAndReturnMoved_withNoElements() throws Exception {
        vm.setAvailableGroups(Group.createGroupsWithNames());
        Set<Group> movedGroups = vm.moveToAddedAndReturnMoved();
        assertTrue(movedGroups.isEmpty());
        assertEquals(vm.getAvailableGroups().size(), 0);
        assertEquals(vm.getAddedGroups().size(), 0);
    }

    @DataProvider(name = "testGroups")
    public Object[][] provideTestGroups() {
        return new Object[][]{{Group.createGroupsWithNames("1", "2")}};
    }
}
