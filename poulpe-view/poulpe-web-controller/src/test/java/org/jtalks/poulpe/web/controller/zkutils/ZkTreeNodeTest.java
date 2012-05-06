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
