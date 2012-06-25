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

import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.web.controller.zkutils.ZkTreeModel;
import org.jtalks.poulpe.web.controller.zkutils.ZkTreeNode;
import org.zkoss.zul.TreeNode;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * A tree model specifically dedicated to work with forum structure.
 *
 * @author stanislav bashkirtsev
 */
public class ForumStructureTreeModel extends ZkTreeModel<ForumStructureItem> {
    private static final long serialVersionUID = 20110138264143L;

    public ForumStructureTreeModel(@Nonnull ZkTreeNode<ForumStructureItem> root) {
        super(root);
    }

    /**
     * Puts the specified branch to the specified section. It moves the branch from its parent if it's already existing
     * one or it will simply create a new node inside the section.The branch doesn't change its position if it's already
     * in the specified section.
     *
     * @param branchToPut        a branch item to be moved/added to the specified section
     * @param destinationSection a section that is accepting a specified branch
     * @return this
     */
    public ForumStructureTreeModel putBranch(ForumStructureItem branchToPut, ForumStructureItem destinationSection) {
        TreeNode<ForumStructureItem> destinationSectionNode = find(destinationSection);
        ZkTreeNode<ForumStructureItem> branchNodeToPut = (ZkTreeNode<ForumStructureItem>) find(branchToPut);
        if (branchNodeToPut == null) {
            branchNodeToPut = new ZkTreeNode<ForumStructureItem>(branchToPut);
        }
        branchNodeToPut.moveTo(destinationSectionNode);
        addToSelection(branchNodeToPut);
        addOpenObject(destinationSectionNode);
        return this;
    }

    public ForumStructureTreeModel moveBranchIfSectionChanged(PoulpeBranch branch) {
        ZkTreeNode<ForumStructureItem> branchNode = (ZkTreeNode<ForumStructureItem>) find(
                new ForumStructureItem(branch));
        ZkTreeNode<ForumStructureItem> sectionNode = (ZkTreeNode<ForumStructureItem>) find(
                new ForumStructureItem(branch.getSection()));
        if (branchNode == null) {
            branchNode = new ZkTreeNode<ForumStructureItem>(new ForumStructureItem(branch));
        }
        branchNode.moveTo(sectionNode);
        setSelectedNode(branchNode);
        addOpenObject(sectionNode);
        return this;
    }

    public void addIfAbsent(PoulpeSection section) {
        TreeNode<ForumStructureItem> sectionNode = find(new ForumStructureItem(section));
        if (sectionNode == null) {
            sectionNode = createSectionNode(section);
            getRoot().add(sectionNode);
        }
    }

    public ForumStructureItem getSelectedSection() {
        return getSelectedData(0);
    }

    public ZkTreeNode<ForumStructureItem> removeBranch(PoulpeBranch branch) {
        ForumStructureItem nodeData = new ForumStructureItem(branch);
        ZkTreeNode<ForumStructureItem> nodeToRemove = (ZkTreeNode<ForumStructureItem>) find(nodeData);
        nodeToRemove.removeFromParent();
        return nodeToRemove;
    }

    public ZkTreeNode<ForumStructureItem> removeSection(PoulpeSection branch) {
        ForumStructureItem nodeData = new ForumStructureItem(branch);
        ZkTreeNode<ForumStructureItem> nodeToRemove = (ZkTreeNode<ForumStructureItem>) find(nodeData);
        nodeToRemove.removeFromParent();
        return nodeToRemove;
    }

    public List<PoulpeSection> getSections() {
        List<TreeNode<ForumStructureItem>> sectionNodes = getRoot().getChildren();
        List<PoulpeSection> sections = new ArrayList<PoulpeSection>();
        for (TreeNode<ForumStructureItem> sectionNode : sectionNodes) {
            sections.add(sectionNode.getData().getSectionItem());
        }
        return sections;
    }

    private ZkTreeNode<ForumStructureItem> createSectionNode(PoulpeSection section) {
        ForumStructureItem sectionItem = new ForumStructureItem(section);
        return new ZkTreeNode<ForumStructureItem>(sectionItem, new ArrayList<TreeNode<ForumStructureItem>>());
    }
}
