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
package org.jtalks.poulpe.web.controller.ranks;

import org.jtalks.common.model.entity.Rank;
import org.jtalks.poulpe.service.RankService;
import org.jtalks.poulpe.web.controller.rank.RankManagementVm;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;


public class RankManagementVmTest {

    RankManagementVm vm;
    RankService rankService;

    @BeforeMethod
    public void setUp() throws Exception {
        rankService = mock(RankService.class);
        vm = new RankManagementVm(rankService);

    }

    @Test
    public void testOpenAddNewRankDialog() {
        assertNull(vm.getSelected());
        assertFalse(vm.isShowDialog());

        vm.openAddNewRankDialog();

        assertNotNull(vm.getSelected());
        assertTrue(vm.isShowDialog());
    }

    @Test
    public void testShowEditDialog() {
        Rank selectedRank = new Rank("Rank", 100);
        vm.setSelected(selectedRank);
        assertFalse(vm.isShowDialog());
        vm.openEditRankDialog();
        assertTrue(vm.isShowDialog());
        assertEquals(vm.getSelected(), selectedRank);
    }

    @Test
    public void testDeleteRank() {
        Rank selectedRank = new Rank("Rank", 100);
        vm.setSelected(selectedRank);
        vm.getRanks().add(selectedRank);

        vm.deleteRank();

        assertNull(vm.getSelected());
        assertTrue(vm.getRanks().isEmpty());
    }

    @Test
    public void testSaveNewRank() {
        Rank selectedRank = new Rank("Rank", 100);
        assertTrue(vm.getRanks().isEmpty());
        vm.setSelected(selectedRank);
        vm.saveRank();

        assertTrue(vm.getRanks().contains(selectedRank));
        assertFalse(vm.isShowDialog());
    }

    @Test
    public void testSaveExistingRank() {
        Rank selectedRank = new Rank("Rank", 100);
        vm.getRanks().add(selectedRank);
        vm.setSelected(selectedRank);

        int biggerPostCount = selectedRank.getPostCount() + 1;
        selectedRank.setPostCount(biggerPostCount);

        vm.saveRank();
        assertEquals(vm.getRanks().size(), 1);
        assertTrue(vm.getRanks().contains(selectedRank));
        assertFalse(vm.isShowDialog());
    }

    @Test
    public void testCancel() {
        vm.setSelected(new Rank("", 1));
        vm.openEditRankDialog();
        assertTrue(vm.isShowDialog());
        assertNotNull(vm.getSelected());

        vm.cancel();

        assertFalse(vm.isShowDialog());
        assertNull(vm.getSelected());
    }

}
