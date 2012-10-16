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

import static org.testng.Assert.*;

import org.jtalks.common.model.entity.Entity;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.model.entity.TopicType;
import org.jtalks.poulpe.model.fixtures.TestFixtures;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.TreeNode;

/**
 * The test class for {@link TreeNodeFactory}
 *
 * @author Konstantin Akimov
 */
@Test
// TODO: what is going on here?
public class TreeNodeFactoryTest {
    PoulpeSection emptySection = TestFixtures.section();
    PoulpeSection section = TestFixtures.sectionWithBranches();
    PoulpeBranch branch = TestFixtures.branch();

    @Test(dataProvider = "provideJcommuneWithSectionsAndBranches", enabled = false)
    public void testBuildForumStructure(Jcommune jcommune) throws Exception {
        TreeNode root = TreeNodeFactory.buildForumStructure(jcommune);
        assertEquals(root.getChildCount(), 2);
        assertSame(root.getChildAt(0).getChildAt(0).getData(), jcommune.getSections().get(0).getBranches().get(0));
        assertSame(root.getChildAt(1).getChildAt(1).getData(), jcommune.getSections().get(1).getBranches().get(1));
    }

    @Test(enabled = false)
    public void getTreeNodeEmptySection() {
        DefaultTreeNode<PoulpeSection> testNode = TreeNodeFactory.getTreeNode(emptySection);
        assertEquals(testNode.getData(), emptySection);
        assertTrue(testNode.getChildren().isEmpty());
    }

    @Test(enabled = false)
    public void getTreeNodeBranch() {
        DefaultTreeNode<PoulpeBranch> testNode = TreeNodeFactory.getTreeNode(branch);
        assertEquals(testNode.getData(), branch);
        assertNull(testNode.getChildren());
        assertTrue(testNode.isLeaf());
    }

    @Test(enabled = false)
    public void getTreeNodeNull() {
        DefaultTreeNode<?> testNode = TreeNodeFactory.getTreeNode((Entity) null);
        assertNull(testNode);
    }

    @Test(enabled = false)
    public void getTreeNodeNotSuitableEntity() {
        DefaultTreeNode<?> testNode = TreeNodeFactory.getTreeNode(new TopicType());
        assertNull(testNode);
    }

    /**
     * Test suitable entities with relations
     */
    @Test(enabled = false)
    public void getTreeNodeWithRelationsTest() {
        DefaultTreeNode<?> testNode = TreeNodeFactory.getTreeNode(section);

        assertEquals(testNode.getData(), section);
        assertEquals(testNode.getChildren().size(), section.getPoulpeBranches().size());
        assertEquals(testNode.getChildAt(0).getData(), section.getPoulpeBranches().get(0));
    }

    @SuppressWarnings("unchecked")
    private void assertChildrenAreLeafs(DefaultTreeNode<PoulpeSection> node) {
        for (Object obj : node.getChildren()) {
            DefaultTreeNode<PoulpeBranch> subnode = (DefaultTreeNode<PoulpeBranch>) obj;
            assertTrue(subnode.isLeaf());
        }
    }

    @DataProvider
    public Object[][] provideJcommuneWithSectionsAndBranches() {
        Jcommune jcommune = TestFixtures.jcommuneWithSections();
        return new Object[][] { { jcommune } };
    }

}
