package org.jtalks.poulpe.web.controller.zkutils;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.TreeNode;

import java.util.ArrayList;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;

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
