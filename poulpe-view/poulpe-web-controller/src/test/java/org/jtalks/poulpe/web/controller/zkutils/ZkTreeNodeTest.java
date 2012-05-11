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

import static java.util.Arrays.asList;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertSame;

/**
 * @author stanislav bashkirtsev
 */
public class ZkTreeNodeTest {
    ZkTreeNode<Object> sut;

    @BeforeMethod
    public void setUp() throws Exception {
        sut = new ZkTreeNode<Object>(new Object());
    }

    @Test
    public void testMoveNode() throws Exception {
        TreeNode<Object> moveFrom = new ZkTreeNode<Object>(new Object(), sut);
        TreeNode<Object> destination = new DefaultTreeNode<Object>(new Object(), new ArrayList<TreeNode<Object>>());

        sut.moveTo(destination);
        assertSame(sut.getParent(), destination);
        assertNotSame(sut, moveFrom);
    }

    @Test
    public void testMoveNodeWithoutParent() throws Exception {
        TreeNode<Object> destination = new DefaultTreeNode<Object>(new Object(), new ArrayList<TreeNode<Object>>());

        sut.moveTo(destination);
        assertSame(sut.getParent(), destination);
    }

    @Test
    public void testMoveNodeToTheSameParent() throws Exception {
        TreeNode<Object> moveFrom = new DefaultTreeNode<Object>(new Object(), asList(sut));

        sut.moveTo(moveFrom);
        assertSame(sut.getParent(), moveFrom);
    }
}
