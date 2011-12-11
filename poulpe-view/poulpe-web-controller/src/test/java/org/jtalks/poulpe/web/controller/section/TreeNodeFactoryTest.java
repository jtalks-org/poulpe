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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Section;
import org.jtalks.poulpe.model.entity.TopicType;
import org.testng.annotations.Test;

/**
 * The test class for {@link TreeNodeFactory}
 * 
 * @author costa
 * 
 */
public class TreeNodeFactoryTest {

    private static final String SECTION_DESCRIPTION = "Test section description";
    private static final String SECTION_NAME = "Test section name";
    private static final String BRANCH_DESCRIPTION = "Test branch description";
    private static final String BRANCH_NAME = "Test branch name";
    private static final String BRANCH_UIID = "Test branch uiid";
    private static final int SECTION_ID = 1;
    private static final int SECTION_POSITION = 1;

    private Section createTestSection() {
        Section section = new Section();
        section.setDescription(SECTION_DESCRIPTION);
        section.setName(SECTION_NAME);
        section.setId(SECTION_ID);
        section.setPosition(SECTION_POSITION);
        return section;
    }

    private Branch createTestBranch() {
        Branch branch = new Branch();
        branch.setDeleted(false);
        branch.setDescription(BRANCH_DESCRIPTION);
        branch.setName(BRANCH_NAME);
        branch.setUuid(BRANCH_UIID);
        return branch;
    }

    private Section createTestSectionWithBranches() {
        Section section = createTestSection();
        section.setBranches(new ArrayList<Branch>());
        section.getBranches().add(createTestBranch());
        section.getBranches().add(createTestBranch());
        section.getBranches().add(createTestBranch());
        return section;
    }

    /**
     * Test single suitable entities
     */
    @Test
    public void getTreeNodeTest() {
        Section sectionWithOutChildren = createTestSection();
        ExtendedTreeNode testNode = TreeNodeFactory
                .getTreeNode(sectionWithOutChildren);

        assertEquals(testNode.getData(), sectionWithOutChildren);
        assertEquals(testNode.getChildren(), null);
        assertEquals(testNode.isExpanded(), true);

        Branch testBranch = createTestBranch();
        testNode = TreeNodeFactory.getTreeNode(testBranch);

        assertEquals(testNode.getData(), testBranch);
        assertEquals(testNode.getChildren(),null);
        assertEquals(testNode.isExpanded(), true);
        assertEquals(testNode.isLeaf(), true);
        
        testNode = TreeNodeFactory.getTreeNode(null);
        assertNull(testNode);
        
        testNode = TreeNodeFactory.getTreeNode(new TopicType());
        assertNull(testNode);
    }

    /**
     * Test suitable entities with relations
     */
    @Test
    public void getTreeNodeWithRelationsTest() {
        Section sectionWithBranchs = createTestSectionWithBranches();
        ExtendedTreeNode testNode = TreeNodeFactory
                .getTreeNode(sectionWithBranchs);

        assertEquals(testNode.getData(), sectionWithBranchs);
        assertEquals(testNode.getChildren().size(), 3);
        assertEquals(testNode.isExpanded(), true);
        assertNotNull(testNode.getChildAt(0));
        assertTrue(testNode.getChildAt(0).getData() instanceof Branch);
        assertEquals(testNode.getChildAt(0).getData(), sectionWithBranchs
                .getBranches().get(0));
    }

    @Test
    public void getTreeNodesTest() {
        List<Section> sections = new ArrayList<Section>();
        sections.add(createTestSectionWithBranches());
        sections.add(createTestSectionWithBranches());
        sections.add(createTestSectionWithBranches());
        sections.add(createTestSectionWithBranches());

        List<ExtendedTreeNode<Section>> nodes = TreeNodeFactory.getTreeNodes(sections);
        assertEquals(nodes.size(), sections.size());
        for (ExtendedTreeNode node : nodes) {
            assertTrue(node.getData() instanceof Section);
            assertEquals(node.getChildCount(), ((Section) node.getData())
                    .getBranches().size());
            for (Object obj : node.getChildren()) {
                assertTrue(obj instanceof ExtendedTreeNode);
                ExtendedTreeNode subnode = (ExtendedTreeNode) obj;
                assertTrue(subnode.getData() instanceof Branch);
                assertTrue(subnode.isLeaf());
            }
        }

    }
    
    @Test
    public void getTreeNodesWithNullsTest() {
        List<Section> sections = new ArrayList<Section>();
        sections.add(createTestSectionWithBranches());
        sections.add(null);
        sections.add(null);
        sections.add(createTestSectionWithBranches());

        List<ExtendedTreeNode<Section>> nodes = TreeNodeFactory.getTreeNodes(sections);
        assertEquals(nodes.size(), 2);
        for (ExtendedTreeNode node : nodes) {
            assertTrue(node.getData() instanceof Section);
            assertEquals(node.getChildCount(), ((Section) node.getData())
                    .getBranches().size());
            for (Object obj : node.getChildren()) {
                assertTrue(obj instanceof ExtendedTreeNode);
                ExtendedTreeNode subnode = (ExtendedTreeNode) obj;
                assertTrue(subnode.getData() instanceof Branch);
                assertTrue(subnode.isLeaf());
            }
        }

    }

}
