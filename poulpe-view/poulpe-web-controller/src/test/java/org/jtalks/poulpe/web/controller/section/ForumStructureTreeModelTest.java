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
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.zkoss.zul.TreeNode;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.*;

/**
 * @author stanislav bashkirtsev
 * @author Guram Savinov
 */
public class ForumStructureTreeModelTest {
    private ForumStructureTreeModel sut;
    private ForumStructureTreeModel spy;

    @BeforeMethod
    public void setUp() throws Exception {
        sut = new ForumStructureTreeModel(createSectionNode());
        sut.getRoot().add(createSectionNode());
        sut.getRoot().add(createSectionNodeWithBranches());
        sut.getRoot().add(createSectionNodeWithBranches());
        spy = Mockito.spy(sut);
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
    public void removeSection() throws Exception {
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
    public void removeSectionShouldDoNothingIfNullPassed() {
        int sizeBeforeRemoval = sut.getSections().size();
        assertNull(sut.removeSection(null));
        assertEquals(sut.getSections().size(), sizeBeforeRemoval);
    }

    @Test
    public void removeBranch() throws Exception {
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
    public void expandTree() {
        sut.expandTree();
        assertEquals(sut.getRoot().getChildCount(), sut.getOpenCount());
    }

    @Test
    public void expandTreeToAlreadyOpenedTree() {
        for (TreeNode<ForumStructureItem> child : sut.getRoot().getChildren()) {
            sut.addOpenObject(child);
        }
        sut.expandTree();
        assertEquals(sut.getRoot().getChildCount(), sut.getOpenCount());
    }

    @Test
    public void expandTreeWithNoElements() {
        removeOpenPaths();
        removeAllSections();
        sut.expandTree();
        assertEquals(0, sut.getOpenCount());
    }

    @Test
    public void checkEffectAfterDropBranchToAnotherSection() {
        TreeNode<ForumStructureItem> dragged = sut.getChild(1, 0);
        TreeNode<ForumStructureItem> target = sut.getChild(0);
        assertFalse(sut.noEffectAfterDropNode(dragged, target));
    }

    @Test
    public void checkEffectAfterDropBranchToSameSection() {
        TreeNode<ForumStructureItem> dragged = sut.getChild(1, 0);
        TreeNode<ForumStructureItem> target = sut.getChild(1);
        assertTrue(sut.noEffectAfterDropNode(dragged, target));
    }

    @Test
    public void checkEffectAfterDropBranchToPreviousBranch() {
        TreeNode<ForumStructureItem> dragged = sut.getChild(1, 1);
        TreeNode<ForumStructureItem> target = sut.getChild(1, 0);
        assertFalse(sut.noEffectAfterDropNode(dragged, target));
    }

    @Test
    public void checkEffectAfterDropBranchToNextBranch() {
        TreeNode<ForumStructureItem> dragged = sut.getChild(1, 1);
        TreeNode<ForumStructureItem> target = sut.getChild(1, 2);
        assertTrue(sut.noEffectAfterDropNode(dragged, target));
    }

    @Test
    public void checkEffectAfterDropBranchToBranchInAnotherSection() {
        TreeNode<ForumStructureItem> dragged = sut.getChild(1, 1);
        TreeNode<ForumStructureItem> target = sut.getChild(2, 0);
        assertFalse(sut.noEffectAfterDropNode(dragged, target));
    }
    
    @Test
    public void checkEffectAfterDropSectionToPreviousSection() {
        TreeNode<ForumStructureItem> dragged = sut.getChild(2);
        TreeNode<ForumStructureItem> target = sut.getChild(1);
        assertFalse(sut.noEffectAfterDropNode(dragged, target));
    }

    @Test
    public void checkEffectAfterDropSectionToNextSection() {
        TreeNode<ForumStructureItem> dragged = sut.getChild(1);
        TreeNode<ForumStructureItem> target = sut.getChild(2);
        assertTrue(sut.noEffectAfterDropNode(dragged, target));
    }

    @Test
    public void checkEffectAfterDropEntity() {
        TreeNode<ForumStructureItem> entityItem = new ZkTreeNode<ForumStructureItem>(
                new ForumStructureItem(new Entity() {
                }));
        assertTrue(sut.noEffectAfterDropNode(entityItem, entityItem));
    }

    @Test
    public void onDropBranchToBranch() {
        TreeNode<ForumStructureItem> dragged = sut.getChild(1, 0);
        TreeNode<ForumStructureItem> target = sut.getChild(1, 1);
        spy.onDropNode(dragged, target);
        verify(spy).dropNodeBefore(dragged, target);
        verify(spy).setSelectedNode(dragged);
    }

    @Test
    public void onDropBranchToSection() {
        TreeNode<ForumStructureItem> dragged = sut.getChild(1, 0);
        TreeNode<ForumStructureItem> target = sut.getChild(2);
        spy.onDropNode(dragged, target);
        verify(spy).dropNodeIn(dragged, target);
        verify(spy).setSelectedNode(dragged);
    }

    @Test
    public void onDropSectionToSection() {
        TreeNode<ForumStructureItem> dragged = sut.getChild(0);
        TreeNode<ForumStructureItem> target = sut.getChild(2);
        spy.onDropNode(dragged, target);
        verify(spy).dropNodeBefore(dragged, target);
        verify(spy).setSelectedNode(dragged);
    }

    @Test
    public void onDropEntity() {
        TreeNode<ForumStructureItem> entityItem = new ZkTreeNode<ForumStructureItem>(
                new ForumStructureItem(new Entity() {
                }));
        spy.onDropNode(entityItem, entityItem);
        verify(spy, never()).dropNodeBefore(entityItem, entityItem);
        verify(spy, never()).dropNodeIn(entityItem, entityItem);
        verify(spy, never()).setSelectedNode(entityItem);
    }

    @Test
    public void getSelectedSection() {
        TreeNode<ForumStructureItem> selected = sut.getChild(1);
        sut.setSelectedNode(selected);
        PoulpeSection section = selected.getData().getSectionItem();
        assertEquals(sut.getSelectedSection(), section);
    }

    @Test
    public void getSectionOfSelectedBranch() {
        TreeNode<ForumStructureItem> selected = sut.getChild(1, 1);
        sut.setSelectedNode(selected);
        PoulpeSection section = selected.getParent().getData().getSectionItem();
        assertEquals(sut.getSelectedSection(), section);
    }

    @Test
    public void getSelectedSectionWhenNothingIsSelected() {
        assertNull(sut.getSelectedSection());
    }

    @Test
    public void addSectionIfAbsent() {
        PoulpeSection absent = new PoulpeSection();
        sut.addIfAbsent(absent);
        assertTrue(sut.getSections().contains(absent));
    }

    @Test
    public void addSectionIfPresent() {
        PoulpeSection present = sut.getChild(1).getData().getSectionItem();
        List<PoulpeSection> sections = sut.getSections();
        sut.addIfAbsent(present);
        assertEquals(sut.getSections(), sections);
    }

    private void removeAllSections() {
        List<PoulpeSection> poulpeSections = sut.getSections();
        for (PoulpeSection ps : poulpeSections) {
            sut.removeSection(ps);
        }
    }

    private void removeOpenPaths() {
        TreeNode<ForumStructureItem> root = sut.getRoot();
        int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            int[] path = sut.getPath(root.getChildAt(i));
            if (sut.isPathOpened(path)) {
                sut.removeOpenPath(path);
            }
        }
    }

}
