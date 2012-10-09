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

import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.web.controller.zkutils.ZkTreeModel;
import org.jtalks.poulpe.web.controller.zkutils.ZkTreeNode;
import org.zkoss.zul.TreeNode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * A tree model specifically dedicated to work with forum structure.
 *
 * @author stanislav bashkirtsev
 * @author Guram Savinov
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

    public PoulpeSection getSelectedSection() {
        ForumStructureItem selectedData = getSelectedData(0);
        if (selectedData != null) {
            return selectedData.getSectionItem();
        }
        return null;
    }

    /**
     * Removes a branch from the tree or does nothing if the branch wasn't found.
     *
     * @param branch a branch to remove from the tree
     * @return a node that was containing that branch or null if no such node found and thus branch wasn't removed
     */
    public ZkTreeNode<ForumStructureItem> removeBranch(@Nullable PoulpeBranch branch) {
        ForumStructureItem itemToRemove = new ForumStructureItem(branch);
        return removeItem(itemToRemove);
    }

    /**
     * Deletes a section from the tree or does nothing if the section can't be found.
     *
     * @param section a section to be removed from the tree
     * @return the removed node or null if no section was found and thus nothing was removed
     */
    public ZkTreeNode<ForumStructureItem> removeSection(@Nullable PoulpeSection section) {
        ForumStructureItem nodeData = new ForumStructureItem(section);
        return removeItem(nodeData);
    }

    private ZkTreeNode<ForumStructureItem> removeItem(ForumStructureItem itemToRemove) {
        ZkTreeNode<ForumStructureItem> nodeToRemove = (ZkTreeNode<ForumStructureItem>) find(itemToRemove);
        if (nodeToRemove != null) {
            nodeToRemove.removeFromParent();
        }
        return nodeToRemove;
    }

    /**
     * @return a list of sections or empty list if there are no sections in forum structure
     */
    public List<PoulpeSection> getSections() {
        List<PoulpeSection> sections = new ArrayList<PoulpeSection>();
        List<TreeNode<ForumStructureItem>> sectionNodes = getRoot().getChildren();
        if (sectionNodes == null) {
            return sections;
        }
        for (TreeNode<ForumStructureItem> sectionNode : sectionNodes) {
            sections.add(sectionNode.getData().getSectionItem());
        }
        return sections;
    }

    private ZkTreeNode<ForumStructureItem> createSectionNode(PoulpeSection section) {
        ForumStructureItem sectionItem = new ForumStructureItem(section);
        return new ZkTreeNode<ForumStructureItem>(sectionItem, new ArrayList<TreeNode<ForumStructureItem>>());
    }

    /**
     * Handler of event when branch node dragged and dropped to another node.
     *
     * @param draggedNode the node represents dragged branch item
     * @param targetNode  the node represents target item (it can be branch or section item)
     */
    public void onDropBranch(TreeNode<ForumStructureItem> draggedNode,
                             TreeNode<ForumStructureItem> targetNode) {
        ForumStructureItem targetItem = targetNode.getData();
        if (targetItem.isBranch()) {
            dropNodeBefore(draggedNode, targetNode);
        } else {
            dropNodeIn(draggedNode, targetNode);
        }
        setSelectedNode(draggedNode);
    }

    /**
     * Handler of event when section node dragged and dropped to another section node.
     *
     * @param draggedNode the node represents dragged section item
     * @param targetNode  the node represents target section item
     */
    public void onDropSection(TreeNode<ForumStructureItem> draggedNode,
                              TreeNode<ForumStructureItem> targetNode) {
        dropNodeBefore(draggedNode, targetNode);
        setSelectedNode(draggedNode);
    }

    /**
     * Checks that node dropping haven't effect.
     *
     * @param draggedNode the node to move
     * @param targetNode  the target node
     * @return {@code true} if dropping have no effect, otherwise return {@code false}
     */
    public boolean noEffectAfterDropNode(TreeNode<ForumStructureItem> draggedNode, TreeNode<ForumStructureItem> targetNode) {
        ForumStructureItem draggedItem = draggedNode.getData();
        if (draggedItem.isBranch()) {
            TreeNode<ForumStructureItem> draggedParentNode = draggedNode.getParent();
            ForumStructureItem targetItem = targetNode.getData();
            if (targetItem.isSection()) {
                return draggedParentNode.equals(targetNode);
            }
            TreeNode<ForumStructureItem> targetParentNode = targetNode.getParent();
            if (draggedParentNode.equals(targetParentNode)) {
                int draggedIndex = draggedParentNode.getIndex(draggedNode);
                int targetIndex = draggedParentNode.getIndex(targetNode);
                return targetIndex - 1 == draggedIndex;
            } else {
                return false;
            }
        } else if (draggedItem.isSection()) {
            int draggedIndex = getRoot().getIndex(draggedNode);
            int targetIndex = getRoot().getIndex(targetNode);
            return targetIndex - 1 == draggedIndex;
        }
        return true;
    }

    /**
     * Since the {@link Jcommune} object is the root of the Forum Structure tree, this method can fetch this object from
     * the tree and return it.
     *
     * @return the {@link Jcommune} object that is the root of the Forum Structure tree
     */
    public Jcommune getRootAsJcommune() {
        return (Jcommune) (Object) getRoot().getData();
    }

    public void expandTree() {
        for(int i = 0; i < getRoot().getChildCount(); i++) {
            int[] path = new int[]{i};
            if( !isPathOpened(path) ) {
                addOpenPath(path);
            }
        }
    }
}
