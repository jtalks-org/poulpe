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
package org.jtalks.poulpe.web.controller.zkmacro;

import org.jtalks.common.model.entity.Group;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

public class DualListVmTest {
    DualListVm list;
    List<Group> leftGroups;
    List<Group> rightGroups;
    List<Group> fullList;

    @BeforeMethod
    public void setUp() throws Exception {
        list = new DualListVm();
        leftGroups = Arrays.asList(new Group("first"), new Group("Second"));
        rightGroups = Arrays.asList(new Group("third"), new Group("4th"));
        fullList = new ArrayList<Group>();
        fullList.addAll(leftGroups);
        fullList.addAll(rightGroups);
    }

    @Test
    public void testNewDualList() {
        assertNotNull(list.getLeft());
        assertNotNull(list.getRight());
        assertNotNull(list.getStateAfterEdit());
        assertNotNull(list.getAvailFilterTxt());
        assertNotNull(list.getExistFilterTxt());
        assertNotNull(list.getConsistentState());
        assertNotNull(list.getLeftSelected());
        assertNotNull(list.getRightSelected());
    }

    @Test
    public void testInitVm() {
        list.initVm(fullList, rightGroups);

        assertEquals(list.getLeft(), leftGroups);
        assertEquals(list.getRight(), rightGroups);
        assertEquals(list.getStateAfterEdit(), rightGroups);
    }

    @Test
    public void testAvailFilterTxt() {
        list.setAvailFilterTxt("New Text");

        assertEquals(list.getAvailFilterTxt(), "New Text");
    }

    @Test
    public void testExistFilterTxt() {
        list.setExistFilterTxt("My Filter Text");

        assertEquals(list.getExistFilterTxt(), "My Filter Text");
    }

    @Test
    public void testAdd() {
        list.initVm(fullList, rightGroups);
        list.getLeft().addToSelection(leftGroups.get(0));
        list.add();

        assertTrue(list.getRight().contains(leftGroups.get(0)));
        assertTrue(!list.getLeft().contains(leftGroups.get(0)));
    }

    @Test
    public void testAddAll() {
        list.initVm(fullList, rightGroups);
        list.addAll();

        assertTrue(list.getRight().containsAll(leftGroups));
        assertEquals(list.getLeft().size(), 0);
    }

    @Test
    public void testRemove() {
        list.initVm(fullList, rightGroups);
        list.getRight().addToSelection(rightGroups.get(0));
        list.remove();

        assertTrue(!list.getRight().contains(rightGroups.get(0)));
        assertTrue(list.getLeft().contains(rightGroups.get(0)));
    }

    @Test
    public void testRemoveAll() {
        list.initVm(fullList, rightGroups);
        list.removeAll();

        assertTrue(list.getLeft().containsAll(rightGroups));
        assertEquals(list.getRight().size(), 0);
    }
}
