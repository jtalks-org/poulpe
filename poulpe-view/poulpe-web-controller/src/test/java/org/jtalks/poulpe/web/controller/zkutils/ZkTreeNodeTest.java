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
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.TreeNode;

import javax.print.DocFlavor;
import java.util.ArrayList;

import static java.util.Arrays.asList;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertSame;

/**
 * @author stanislav bashkirtsev
 */
public class ZkTreeNodeTest {
    ZkTreeNode<String> sut;

    @BeforeMethod
    public void setUp() throws Exception {
        sut = new ZkTreeNode<String>("a");
        ZkTreeModel<String> tree = new ZkTreeModel<String>(new ZkTreeNode<String>("0", new ArrayList<TreeNode<String>>()));
        tree.getRoot().add(sut);
    }

    @Test
    public void testMoveNode() throws Exception {
        TreeNode<String> moveFrom = new ZkTreeNode<String>("b", sut);
        TreeNode<String> destination = new DefaultTreeNode<String>("c", new ArrayList<TreeNode<String>>());

        sut.moveTo(destination);
        assertSame(sut.getParent(), destination);
        assertNotSame(sut, moveFrom);
    }

    @Test
    public void testMoveNodeWithoutParent() throws Exception {
        TreeNode<String> destination = new DefaultTreeNode<String>("g", new ArrayList<TreeNode<String>>());

        sut.moveTo(destination);
        assertSame(sut.getParent(), destination);
    }

    @Test
    public void testMoveNodeToTheSameParent() throws Exception {
        TreeNode<String> moveFrom = new DefaultTreeNode<String>("d", asList(sut));

        sut.moveTo(moveFrom);
        assertSame(sut.getParent(), moveFrom);
    }

    @Test
    public void testSelect() {
        sut.select();
        sut.getModel().isSelected(sut);
    }
}
