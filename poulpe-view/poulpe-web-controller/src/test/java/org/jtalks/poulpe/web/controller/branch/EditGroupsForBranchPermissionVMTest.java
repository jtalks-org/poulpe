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


/**
 * @author stanislav bashkirtsev
 * @author Vyacheslav Zhivaev
 */
public class EditGroupsForBranchPermissionVMTest {
//
//    /**
//     * Answer used for BranchServiceChange for checking methods:
//     * <p>
//     * {@link BranchService#changeGrants(PoulpeBranch, PermissionChanges)}
//     * {@link BranchService#changeRestrictions(PoulpeBranch, PermissionChanges)}
//     * <p>
//     * Usage:
//     * <ol>
//     * <li>
//     * initialize BranchServiceChangeACLAnswer
//     * <li>
//     * set data to check by method {@link #setDataToCheck(PoulpeBranch, JtalksPermission, PoulpeGroup[], PoulpeGroup[])}
//     * <li>
//     * add doAnswer commands by shorthand method: {@link #addAnswerForACLRelatedMethods(BranchService)}
//     * <li>
//     * run your checks
//     * 
//     * @author Vyacheslav Zhivaev
//     */
//    public class BranchServiceChangeACLAnswer<E> implements Answer<E> {
//        private PoulpeBranch branch;
//        private JtalksPermission permission;
//        private List<PoulpeGroup> grantsGroups;
//        private List<PoulpeGroup> restrictionsGroups;
//
//        /**
//         * Define data which will be checked to equality when service method will be fired.
//         */
//        public void setDataToCheck(PoulpeBranch branch, JtalksPermission permission, List<PoulpeGroup> grantsGroups,
//                List<PoulpeGroup> restrictionsGroups) {
//            this.branch = branch;
//            this.permission = permission;
//            this.grantsGroups = grantsGroups;
//            this.restrictionsGroups = restrictionsGroups;
//        }
//
//        /**
//         * {@inheritDoc}
//         * 
//         * @return always {@code null} value
//         */
//        @Override
//        public E answer(InvocationOnMock invocation) throws Throwable {
//            if (invocation.getArguments().length < 2) {
//                fail("Invalid numbers of arguments passed to BranchService");
//            }
//
//            PoulpeBranch branchArg = (PoulpeBranch) invocation.getArguments()[0];
//            assertEquals(branchArg, branch);
//
//            PermissionChanges changeset = (PermissionChanges) invocation.getArguments()[1];
//            assertEquals(changeset.getPermission(), permission);
//
//            List<PoulpeGroup> groups = Lists.newArrayList(changeset.getNewlyAddedGroupsAsArray());
//            assertTrue(grantsGroups.containsAll(groups));
//            assertTrue(ListUtils.subtract(groups, grantsGroups).isEmpty());
//
//            groups = Lists.newArrayList(changeset.getRemovedGroupsAsArray());
//            assertTrue(restrictionsGroups.containsAll(groups));
//            assertTrue(ListUtils.subtract(groups, restrictionsGroups).isEmpty());
//
//            return null;
//        }
//
//        /**
//         * Adds {@link Mockito#doAnswer(Answer)} commands for current BranchService.
//         * 
//         * @param branchService the service to add commands
//         */
//        public void addAnswerForACLRelatedMethods(BranchService branchService) {
//            doAnswer(this).when(branchService).changeGrants(any(PoulpeBranch.class), any(PermissionChanges.class));
//            doAnswer(this).when(branchService).changeRestrictions(any(PoulpeBranch.class), any(PermissionChanges.class));
//        }
//    };
//
//    // sut
//    private EditGroupsForBranchPermissionVm vm;
//
//    @Mock
//    private WindowManager windowManager;
//    @Mock
//    private BranchService branchService;
//    @Mock
//    private GroupService groupService;
//
//    private SelectedEntity<Object> selectedEntity;
//
//    @DataProvider(name = "EditGroupsForBranchPermissionVMTestData")
//    public Object[][] provideTestData() {
//        PoulpeBranch branch = ObjectsFactory.fakeBranch();
//        BranchPermissions branchAccessList = new BranchPermissions();
////        branchAccessList.put(permission, group, allow)
//        PermissionForEntity aclModePermission = new PermissionForEntity(branch, false, BranchPermission.CREATE_TOPICS);
//        List<PoulpeGroup> grantsGroups = ObjectsFactory.fakeGroupList(RandomUtils.nextInt(10));
//        List<PoulpeGroup> restrictionsGroups = ObjectsFactory.fakeGroupList(RandomUtils.nextInt(10));
//        
//        return new Object[][] {
//                { ObjectsFactory.fakeGroup(), branch, branchAccessList, aclModePermission, grantsGroups, restrictionsGroups }
//        };
//    }
//
//    @BeforeMethod
//    protected void beforeMethod(PoulpeGroup group, PoulpeBranch branch, BranchPermissions branchAcl,
//            PermissionForEntity aclModePermission, List<PoulpeGroup> grantsGroups, List<PoulpeGroup> restrictionsGroups) {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    /**
//     * Builds SUT.
//     */
//    private void buildSut(PoulpeBranch branch, BranchPermissions branchAcl, PermissionForEntity aclModePermission,
//            List<PoulpeGroup> grantsGroups, List<PoulpeGroup> restrictionsGroups) {
//        BranchServiceChangeACLAnswer<?> answer = new BranchServiceChangeACLAnswer<Object>();
//        answer.setDataToCheck(branch, aclModePermission.getPermission(), grantsGroups, restrictionsGroups);
//
//        selectedEntity = new SelectedEntity<Object>();
//        selectedEntity.setEntity(aclModePermission);
//
//        when(branchService.getGroupAccessListFor(branch)).thenReturn(branchAcl);
//        answer.addAnswerForACLRelatedMethods(branchService);
//
//        vm = new EditGroupsForBranchPermissionVm(windowManager, branchService, groupService, selectedEntity);
//    }
//
//    @Test(dataProvider = "EditGroupsForBranchPermissionVMTestData")
//    public void cancelTest(PoulpeGroup group, PoulpeBranch branch, BranchPermissions branchAcl,
//            PermissionForEntity aclModePermission, List<PoulpeGroup> grantsGroups, List<PoulpeGroup> restrictionsGroups) {
//        buildSut(branch, branchAcl, aclModePermission, grantsGroups, restrictionsGroups);
//        
//        vm.cancel();
//
//        assertEquals((PoulpeBranch) selectedEntity.getEntity(), branch);
//        verify(windowManager).open(anyString());
//    }
//
//    @Test(dataProvider = "EditGroupsForBranchPermissionVMTestData")
//    public void saveTest(PoulpeGroup group, PoulpeBranch branch, BranchPermissions branchAcl,
//            PermissionForEntity aclModePermission, List<PoulpeGroup> grantsGroups, List<PoulpeGroup> restrictionsGroups) {
//        buildSut(branch, branchAcl, aclModePermission, grantsGroups, restrictionsGroups);
//        
//        assertTrue(vm.getAvail().contains(group));
//        assertFalse(vm.getExist().contains(group));
//
//        vm.getAvail().remove(group);
//        vm.getExist().add(group);
//
//        vm.save();
//
//        verify(branchService).changeGrants(branch, any(PermissionChanges.class));
//    }
//
//    @Test(dataProvider = "EditGroupsForBranchPermissionVMTestData")
//    public void updateVmTest(PoulpeGroup group, PoulpeBranch branch, BranchPermissions branchAcl,
//            PermissionForEntity aclModePermission, List<PoulpeGroup> grantsGroups, List<PoulpeGroup> restrictionsGroups) {
//        buildSut(branch, branchAcl, aclModePermission, grantsGroups, restrictionsGroups);
//        
//        vm.updateVm();
//    }
//
// @Test(dataProvider = "testGroups")
// public void testGetRemovedFromAdded(List<PoulpeGroup> testGroups) throws Exception {
// vm.setAddedGroups(testGroups);
// moveFromAdded(vm, testGroups.get(0));
// Collection<PoulpeGroup> removedFromAdded = vm.getRemovedFromAdded();
// assertEquals(removedFromAdded.size(), 1);
// assertSame(removedFromAdded.iterator().next(), testGroups.get(0));
// }
//
// @Test(dataProvider = "testGroups")
// public void testGetNewInAdded(List<PoulpeGroup> testGroups) throws Exception {
// vm.setAvailableGroups(testGroups);
// moveToAdded(vm, testGroups.get(1));
// Collection<PoulpeGroup> newInAdded = vm.getNewAdded();
// assertEquals(newInAdded.size(), 1);
// assertSame(newInAdded.iterator().next(), testGroups.get(1));
// }
//
// /**
// * <pre>
// * 1.Adds elements to the Added Groups.
// * 2.Moves some element from Added Groups.
// * 3.Moves the same element back.
// * 4.Checks that {@link EditGroupsForBranchPermissionVm#getNewAdded()} returns empty list.
// * </pre>
// *
// * @param testGroups 2 random test groups
// * @throws Exception who would care
// */
// @Test(dataProvider = "testGroups")
// public void testGetNewInAdded_afterElementReturnedBack(List<PoulpeGroup> testGroups) throws Exception {
// vm.setAddedGroups(testGroups);
// moveFromAdded(vm, testGroups.get(0));
// moveToAdded(vm, testGroups.get(0));
//
// assertTrue(vm.getNewAdded().isEmpty());
// }
//
// @Test(dataProvider = "testGroups")
// public void testMoveSelectedToAddedGroups(List<PoulpeGroup> testGroups) throws Exception {
// vm.setAvailableGroups(testGroups);
// Collection<PoulpeGroup> movedGroups = moveToAdded(vm, testGroups.get(0));
// // check sizes of after moving the moving
// assertEquals(movedGroups.size(), 1);
// assertEquals(vm.getAvailableGroups().size(), testGroups.size() - 1);
// assertEquals(vm.getAddedGroups().size(), 1);
// // check elements actually moved
// assertSame(movedGroups.iterator().next(), testGroups.get(0));
// assertSame(vm.getAvailableGroups().iterator().next(), testGroups.get(1));
// assertSame(vm.getAddedGroups().iterator().next(), testGroups.get(0));
// }
//
// @Test(dataProvider = "testGroups")
// public void testMoveSelectedToAddedGroups_withNoSelection(List<PoulpeGroup> testGroups) throws Exception {
// vm.setAvailableGroups(testGroups);
// Collection<PoulpeGroup> movedGroups = vm.moveSelectedToAddedGroups();
// assertTrue(movedGroups.isEmpty());
// assertEquals(vm.getAvailableGroups().size(), testGroups.size());
// assertEquals(vm.getAddedGroups().size(), 0);
// }
//
// @Test
// public void testMoveSelectedToAddedGroups_withNoElements() throws Exception {
// Set<PoulpeGroup> movedGroups = vm.moveSelectedToAddedGroups();
// assertTrue(movedGroups.isEmpty());
// assertEquals(vm.getAvailableGroups().size(), 0);
// assertEquals(vm.getAddedGroups().size(), 0);
// }
//
// @Test(dataProvider = "testGroups")
// public void testMoveSelectedFromAddedGroups(List<PoulpeGroup> testGroups) throws Exception {
// vm.setAddedGroups(testGroups);
// Collection<PoulpeGroup> movedGroups = moveFromAdded(vm, testGroups.get(1));
// // check sizes of after moving the moving
// assertEquals(movedGroups.size(), 1);
// assertEquals(vm.getAvailableGroups().size(), 1);
// assertEquals(vm.getAddedGroups().size(), testGroups.size() - 1);
// // check elements actually moved
// assertSame(movedGroups.iterator().next(), testGroups.get(1));
// assertSame(vm.getAvailableGroups().iterator().next(), testGroups.get(1));
// assertSame(vm.getAddedGroups().iterator().next(), testGroups.get(0));
// }
//
// @Test(dataProvider = "testGroups")
// public void testMoveSelectedFromAddedGroups_withNoSelection(List<PoulpeGroup> testGroups) throws Exception {
// vm.setAddedGroups(testGroups);
// vm.getAddedGroups();
// Collection<PoulpeGroup> movedGroups = vm.moveSelectedFromAddedGroups();
//
// assertTrue(movedGroups.isEmpty());
// assertTrue(vm.getAvailableGroups().isEmpty());
// assertEquals(vm.getAddedGroups().size(), testGroups.size());
// }
//
// @Test
// public void testMoveSelectedFromAddedGroups_withNoElements() throws Exception {
// Set<PoulpeGroup> movedGroups = vm.moveSelectedFromAddedGroups();
// assertTrue(movedGroups.isEmpty());
// assertEquals(vm.getAvailableGroups().size(), 0);
// assertEquals(vm.getAddedGroups().size(), 0);
// }
//
// /**
// * <pre>
// * 1. Fill the list of added.
// * 2. Select some of elements just for to ensure this doesn't impact anything
// * 3. Check all elements moved to added list
// * </pre>
// *
// * @param testGroups 2 random test groups
// * @throws Exception who cares
// */
// @Test(dataProvider = "testGroups")
// public void testMoveAllToAddedGroups(List<PoulpeGroup> testGroups) throws Exception {
// vm.setAvailableGroups(testGroups).getAvailableGroups().addToSelection(testGroups.get(1));
// Set<PoulpeGroup> moved = vm.moveAllToAddedGroups();
// assertTrue(vm.getAvailableGroups().isEmpty());
// assertEquals(moved.size(), testGroups.size());
// assertEquals(vm.getAddedGroups().size(), testGroups.size());
// }
//
// @Test
// public void testMoveAllToAddedGroups_withEmptyList() throws Exception {
// Set<PoulpeGroup> moved = vm.moveAllToAddedGroups();
// assertTrue(vm.getAvailableGroups().isEmpty());
// assertTrue(moved.isEmpty());
// assertTrue(vm.getAddedGroups().isEmpty());
// }
//
// @Test(dataProvider = "testGroups")
// public void testMoveAllFromAddedGroups(List<PoulpeGroup> testGroups) throws Exception {
// vm.setAddedGroups(testGroups).getAddedGroups().addToSelection(testGroups.get(1));
// Set<PoulpeGroup> moved = vm.moveAllFromAddedGroups();
// assertTrue(vm.getAddedGroups().isEmpty());
// assertEquals(vm.getAvailableGroups().size(), testGroups.size());
// assertEquals(moved.size(), testGroups.size());
// }
//
// @Test
// public void testMoveAllFromAddedGroups_withEmptyList() throws Exception {
// Set<PoulpeGroup> moved = vm.moveAllFromAddedGroups();
// assertTrue(vm.getAvailableGroups().isEmpty());
// assertTrue(vm.getAddedGroups().isEmpty());
// assertTrue(moved.isEmpty());
// }
//
// @DataProvider(name = "testGroups")
// public Object[][] provideTestGroups() {
// return new Object[][] { { Arrays.asList(new PoulpeGroup("1"), new PoulpeGroup("2"), new PoulpeGroup("3")) } };
// }
//
// /**
// * Selects the element at added groups and invokes
// * {@link EditGroupsForBranchPermissionVm#moveSelectedFromAddedGroups()} to make
// * the view model move the elements from the Added List.
// *
// * @param vm a view model to change its lists
// * @param toMove an element to move from the added list to the list of
// * available groups
// * @return the groups been moved
// */
// private Collection<PoulpeGroup> moveFromAdded(EditGroupsForBranchPermissionVm vm, PoulpeGroup toMove) {
// vm.getAddedGroups().addToSelection(toMove);
// return vm.moveSelectedFromAddedGroups();
// }
//
// /**
// * Selects the element in available groups and invokes
// * {@link EditGroupsForBranchPermissionVm#moveSelectedToAddedGroups ()} to make the
// * view model move the elements to the Added List.
// *
// * @param vm a view model to change its lists
// * @param toMove an element to move from the available list to the list of
// * added groups
// * @return the groups been moved
// */
// private Collection<PoulpeGroup> moveToAdded(EditGroupsForBranchPermissionVm vm, PoulpeGroup toMove) {
// vm.getAvailableGroups().addToSelection(toMove);
// return vm.moveSelectedToAddedGroups();
// }
}
