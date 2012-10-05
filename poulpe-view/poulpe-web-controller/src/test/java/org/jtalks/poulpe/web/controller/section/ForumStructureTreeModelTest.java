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
package org.jtalks.poulpe.web.controller.section;

import org.apache.commons.lang3.RandomStringUtils;
import org.jtalks.common.model.entity.Entity;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.web.controller.zkutils.ZkTreeNode;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.zkoss.zul.TreeNode;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

/**
 * @author stanislav bashkirtsev
 */
public class ForumStructureTreeModelTest {
    ForumStructureTreeModel sut;

    @BeforeMethod
    public void setUp() throws Exception {
        sut = new ForumStructureTreeModel(createSectionNode());
        sut.getRoot().add(createSectionNode());
        sut.getRoot().add(createSectionNodeWithBranches());
        sut.getRoot().add(createSectionNodeWithBranches());
    }

    @Test
    public void putBranchShouldMoveIt() throws Exception {
        ForumStructureItem branchToPut = sut.getChild(1, 0).getData();
        ForumStructureItem destinationSection = sut.getChild(0).getData();
        sut.putBranch(branchToPut, destinationSection);
        assertSame(sut.getChild(0, 0).getData(), branchToPut);
        assertNotSame(sut.getChild(1, 0).getData(), branchToPut);
    }

    @Test
    public void putNewBranchShouldCreateNewNode() throws Exception {
        ForumStructureItem branchToPut = new ForumStructureItem(new PoulpeBranch("aa"));
        ForumStructureItem destinationSection = sut.getChild(0).getData();
        sut.putBranch(branchToPut, destinationSection);
        assertSame(sut.getChild(0, 0).getData(), branchToPut);
    }

    @Test
    public void getSectionsShouldReturnAllOfThem() throws Exception {
        assertEquals(sut.getSections().size(), sut.getRoot().getChildren().size());
    }

    @Test
    public void getSectionsWithNoSectionsShouldReturnAllEmptyList() throws Exception {
        sut = new ForumStructureTreeModel(createBranchNode());//actually any node will be good for the root
        assertTrue(sut.getSections().isEmpty());
    }

    @Test
    public void testRemoveSection() throws Exception {
        PoulpeSection sectionToRemove = sut.getSections().get(0);
        assertSame(sut.removeSection(sectionToRemove).getData().getSectionItem(), sectionToRemove);
        assertNull(sut.find(new ForumStructureItem(sectionToRemove)));
    }

    @Test
    public void removeSectionShouldDoNothingIfSectionNotFound() throws Exception {
        int sizeBeforeRemoval = sut.getSections().size();
        assertNull(sut.removeSection(new PoulpeSection("to-remove")));
        assertEquals(sut.getSections().size(), sizeBeforeRemoval);
    }

    @Test
    public void removeSectionShouldDoNothingIfNullPassed(){
        int sizeBeforeRemoval = sut.getSections().size();
        assertNull(sut.removeSection(null));
        assertEquals(sut.getSections().size(), sizeBeforeRemoval);
    }

    @Test
    public void testRemoveBranch() throws Exception {
        PoulpeBranch branchToRemove = sut.getChild(1, 0).getData().getBranchItem();
        assertSame(sut.removeBranch(branchToRemove).getData().getBranchItem(), branchToRemove);
        assertNull(sut.find(new ForumStructureItem(branchToRemove)));
    }

    private ZkTreeNode<ForumStructureItem> createBranchNode() {
        PoulpeBranch branch = new PoulpeBranch(RandomStringUtils.random(10));
        return new ZkTreeNode<ForumStructureItem>(new ForumStructureItem(branch));
    }

    private ZkTreeNode<ForumStructureItem> createSectionNode() {
        PoulpeSection section = new PoulpeSection(RandomStringUtils.random(10));
        return new ZkTreeNode<ForumStructureItem>(
                new ForumStructureItem(section), new ArrayList<TreeNode<ForumStructureItem>>());
    }

    private TreeNode<ForumStructureItem> createSectionNodeWithBranches() {
        TreeNode<ForumStructureItem> sectionNode = createSectionNode();
        sectionNode.add(createBranchNode());
        sectionNode.add(createBranchNode());
        sectionNode.add(createBranchNode());
        return sectionNode;
    }

    @Test
    public void testExpandTree() {

        // before expand tree
        assertEquals(0, sut.getOpenCount());
        assertNull(sut.getOpenPaths());

        int[] path = new int[]{1};
        assertFalse(sut.isPathOpened(path));

        sut.expandTree();
        // after expand tree
        assertEquals(3, sut.getOpenCount());
        assertTrue(sut.isPathOpened(path));

        sut.expandTree();
        // after second calling to expand tree
        assertEquals(3, sut.getOpenCount());
    }

    @Test
    public void testExpandTreeWithNoElements() {
        // remove open paths
        if(sut.getOpenCount() > 0) {
            removeOpenPaths();
        }
        // remove all elements from tree
        if(sut.getSections().size() > 0) {
            removeSections();
        }
        // when no elements in tree
        sut.expandTree();
        assertEquals(0, sut.getOpenCount());
        assertNull(sut.getOpenPaths());
    }

    @Test
    public void testCheckEffectAfterDropBranchToSection() {
        TreeNode<ForumStructureItem> dragged = sut.getChild(1, 0);
        TreeNode<ForumStructureItem> target = sut.getChild(0);
        assertFalse(sut.noEffectAfterDropNode(dragged, target));
        target = sut.getChild(1);
        assertTrue(sut.noEffectAfterDropNode(dragged, target));
    }
    
    @Test
    public void testCheckEffectAfterDropBranchToBranch() {
        TreeNode<ForumStructureItem> dragged = sut.getChild(1, 1);
        TreeNode<ForumStructureItem> target = sut.getChild(1, 0);
        assertFalse(sut.noEffectAfterDropNode(dragged, target));
        target = sut.getChild(1, 2);
        assertTrue(sut.noEffectAfterDropNode(dragged, target));
        target = sut.getChild(2, 0);
        assertFalse(sut.noEffectAfterDropNode(dragged, target));
    }

    @Test
    public void testCheckEffectAfterDropSectionToSection() {
        TreeNode<ForumStructureItem> dragged = sut.getChild(1);
        TreeNode<ForumStructureItem> target = sut.getChild(2);
        assertTrue(sut.noEffectAfterDropNode(dragged, target));
        dragged = sut.getChild(2);
        target = sut.getChild(1);
        assertFalse(sut.noEffectAfterDropNode(dragged, target));
    }

    @Test
    public void testCheckEffectAfterDropNotBranchOrSection() {
        Entity notBranchOrSection = new Entity() {
        };
        TreeNode<ForumStructureItem> dragged = new ZkTreeNode<ForumStructureItem>(
                new ForumStructureItem(notBranchOrSection));
        assertTrue(sut.noEffectAfterDropNode(dragged, dragged));
    }

    private void removeSections() {
        List<PoulpeSection> poulpeSections = sut.getSections();
        for(PoulpeSection ps:poulpeSections) {
            sut.removeSection(ps);
        }
    }

    private void removeOpenPaths() {
        for(int i = 0; i < sut.getRoot().getChildCount();i++) {
            int[] child = sut.getPath(sut.getRoot().getChildAt(i));
            if(sut.isPathOpened(child)) {
                sut.removeOpenPath(child);
            }
        }
    }
}
