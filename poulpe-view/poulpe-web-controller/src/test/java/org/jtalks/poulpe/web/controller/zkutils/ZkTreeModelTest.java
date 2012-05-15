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
package org.jtalks.poulpe.web.controller.zkutils;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.TreeNode;

import java.util.ArrayList;

import static org.testng.Assert.*;

/**
 * @author stanislav bashkirtsev
 */
public class ZkTreeModelTest {
    ZkTreeModel<Object> sut;

    @BeforeMethod
    public void setUp() throws Exception {
        sut = new ZkTreeModel<Object>(new DefaultTreeNode<Object>(new Object(), new ArrayList<TreeNode<Object>>()));
        sut.getRoot().add(new DefaultTreeNode<Object>(new Object(), new ArrayList<TreeNode<Object>>()));
        sut.getRoot().add(new DefaultTreeNode<Object>(new Object(), new ArrayList<TreeNode<Object>>()));
        sut.getRoot().add(new DefaultTreeNode<Object>(new Object()));
        sut.getRoot().getChildAt(1).add(new DefaultTreeNode<Object>(new Object()));
        sut.getRoot().getChildAt(1).add(new DefaultTreeNode<Object>(new Object()));
        sut.getRoot().getChildAt(1).add(new DefaultTreeNode<Object>(new Object()));
    }

    @Test
    public void testFindWithRootDataSpecified() throws Exception {
        Object toSearch = sut.getRoot().getData();
        assertSame(sut.find(toSearch), sut.getRoot());
    }

    @Test
    public void testFind() throws Exception {
        Object toSearch = sut.getRoot().getChildAt(1).getChildAt(2).getData();
        assertSame(sut.find(toSearch), sut.getRoot().getChildAt(1).getChildAt(2));
    }

    @Test
    public void testRemoveChild() throws Exception {
        TreeNode<Object> childToRemove = sut.getChild(1, 1);
        assertSame(sut.removeChild(1, 1), childToRemove);
        assertNotSame(sut.getChild(1, 1), childToRemove);
    }

    @Test
    public void testRemoveChild_thatDoesntExist() throws Exception {
        assertNull(sut.removeChild(0, 100));
    }

    @Test
    public void testRemoveSelected() {
        TreeNode<Object> child = sut.getChild(1, 1);
        sut.addToSelection(child);

        sut.removeSelected();
        assertTrue(sut.getSelection().isEmpty());
        assertNotSame(sut.getChild(1, 1), child);
    }

    @Test
    public void testRemoveSelected_withNoSelection() {
        assertNull(sut.removeSelected());
    }


    @Test
    public void testGetChild() throws Exception {
        assertSame(sut.getChild(1, 0), sut.getRoot().getChildAt(1).getChildAt(0));
    }

    @Test
    public void testGetChild_withNoSuchPathExisting() throws Exception {
        assertNull(sut.getChild(5));
    }

    @Test
    public void testGetChild_withEmptyPath() throws Exception {
        assertSame(sut.getChild(), sut.getRoot());
    }

    @Test
    public void testFindWithoutSuchElement() throws Exception {
        assertNull(sut.find(new Object()));
    }
}
