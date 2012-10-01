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
        sut.getRoot().add(createSectionNode());
        sut.getRoot().add(createSectionNode());
        sut.getRoot().getChildAt(1).add(createBranchNode());
        sut.getRoot().getChildAt(1).add(createBranchNode());
        sut.getRoot().getChildAt(1).add(createBranchNode());
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

    @Test
    public void testExpandTree() {

        sut.expandTree();

        int[] path = new int[]{1};
        assertTrue(sut.isPathOpened(path));

        removeOpenPaths();
        assertNull(sut.getOpenPaths());

        removeSections();
        assertEquals(0, sut.getSections().size());

        sut.expandTree();
        assertNull(sut.getOpenPaths());



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
