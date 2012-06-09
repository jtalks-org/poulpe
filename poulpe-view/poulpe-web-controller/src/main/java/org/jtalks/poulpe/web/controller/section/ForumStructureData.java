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

import org.jtalks.common.model.entity.Entity;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.web.controller.section.dialogs.BranchEditingDialog;
import org.jtalks.poulpe.web.controller.section.dialogs.ConfirmBranchDeletionDialogVm;
import org.jtalks.poulpe.web.controller.section.dialogs.ConfirmSectionDeletionDialogVm;
import org.zkoss.zul.TreeNode;

import javax.annotation.Nonnull;

/**
 * Is used to contain data for the view and do operations on view. It doesn't do any business logic, only logic that is
 * connected to presenting the data. In MVP it would be a View.
 *
 * @author stanislav bashkirtsev
 * @author Guram Savinov
 */
public class ForumStructureData {
    /**
     * The level of the section in the tree, e.g. branch is the next level = 1.
     */
    private static final int SECTION_TREE_LEVEL = 0;
    private final BranchEditingDialog branchDialog = new BranchEditingDialog(null, null, null);
    private final ConfirmBranchDeletionDialogVm confirmBranchDeletionDialogVm = new ConfirmBranchDeletionDialogVm();
    private final ConfirmSectionDeletionDialogVm confirmSectionDeletionDialogVm = new ConfirmSectionDeletionDialogVm();
    private ForumStructureItem selectedItem = new ForumStructureItem();
    private ForumStructureTreeModel structureTree;
    private boolean showSectionDialog;


    /**
     * Since the {@link Jcommune} object is the root of the Forum Structure tree, this method can fetch this object from
     * the tree and return it.
     *
     * @return the {@link Jcommune} object that is the root of the Forum Structure tree
     */
    public Jcommune getRootAsJcommune() {
        return (Jcommune) (Object) structureTree.getRoot().getData();
    }

    /**
     * You can use it to close both section dialog and branch dialog.
     *
     * @return this
     */
    public ForumStructureData closeDialog() {
        showSectionDialog = false;
        branchDialog.closeDialog();
        return this;
    }

    /**
     * Shows the section dialog and decides whether it should be a dialog for the new section creation or it will be an
     * editing of the existing item.
     *
     * @param createNew a flag to decide whether we're going to create a new section or will be editing the existing
     *                  one
     * @return this
     */
    public ForumStructureData showSectionDialog(boolean createNew) {
        selectedItem = selectedItem.prepareSectionItemForEditing(createNew);
        showSectionDialog = true;
        return this;
    }

    /**
     * Sets an empty item into the {@link #selectedItem} field (should be treated as {@code null}, but Null Object
     * pattern is used to get rid of cumbersome conditions).
     *
     * @return the item that was previously set as selected (the one that is removed by this method)
     */
    public ForumStructureItem removeSelectedItem() {
        return structureTree.removeSelected().getData();
    }

    /**
     * The item being currently selected. It can be empty (without underlying entity) if nothing was actually selected.
     *
     * @return item being currently selected or an empty item if nothing is selected
     */
    public ForumStructureItem getSelectedItem() {
        return selectedItem;
    }

    /**
     * Replaces the old selected item with the new one specified.
     *
     * @param selectedItem a new selected item to be set
     * @return the previous selected item or empty item if nothing was previously set (since during the class creation,
     *         an empty item is created and set to the selected one)
     */
    public ForumStructureItem setSelectedItem(@Nonnull ForumStructureItem selectedItem) {
        ForumStructureItem previous = this.selectedItem;
        this.selectedItem = selectedItem;
        return previous;
    }

    /**
     * Figures out whether the Section Editing Dialog should be shown right now. It also changes the flag to {@code
     * false} each time because we don't send anything to the server when closing window, so during next change
     * notification ZK will think that we need to show that dialog again which is wrong.
     *
     * @return {@code true} if the Section Editing dialog should be shown
     * @see #showSectionDialog
     */
    public boolean isShowSectionDialog() {
        boolean show = showSectionDialog && selectedItem.isSection();
        this.showSectionDialog = false;
        return show;
    }

    /**
     * Gets the VM that is responsible for showing the section deletion confirmation.
     *
     * @return the VM that is responsible for showing the section deletion confirmation
     */
    public ConfirmSectionDeletionDialogVm getConfirmSectionDeletionDialogVm() {
        return confirmSectionDeletionDialogVm;
    }

    /**
     * Gets the VM that is responsible for showing the branch deletion confirmation.
     *
     * @return the VM that is responsible for showing the branch deletion confirmation
     */
    public ConfirmBranchDeletionDialogVm getConfirmBranchDeletionDialogVm() {
        return confirmBranchDeletionDialogVm;
    }

    /**
     * The whole tree of sections and branches as a ZK tree model.
     *
     * @return the tree of sections and branches
     */
    public ForumStructureTreeModel getStructureTree() {
        return structureTree;
    }

    /**
     * A shorthand method to get currently selected item.
     *
     * @param clazz the class (branch or section) the selected item should be casted to
     * @param <T>   {@link  PoulpeBranch} or {@link PoulpeSection}
     * @return currently selected entity or {@code null} if nothing is selected
     * @see ForumStructureItem#getItem(Class)
     */
    public <T extends Entity> T getSelectedEntity(Class<T> clazz) {
        return getSelectedItem().getItem(clazz);
    }

    /**
     * Gets the Branch Editing Dialog View Data which is used while creating a new branch or editing the existing one.
     *
     * @return the Branch Editing Dialog View Data which is used while creating a new branch or editing the existing
     *         one
     */
    public BranchEditingDialog getBranchDialog() {
        return branchDialog;
    }

    /**
     * Sets and rebuilds the Forum Structure tree and also updates the branch dialog with the list of available sections
     * so that in combo box we have only sections that are present in the section tree.
     *
     * @param sectionTree the new tree of sections and branches to be shown on the Forum Structure page
     */

    public void setStructureTree(@Nonnull ForumStructureTreeModel sectionTree) {
        this.structureTree = sectionTree;
        branchDialog.renewSectionsFromTree(sectionTree);
    }

    /**
     * Drops node before the target and selects it
     *
     * @param node   the node that will be dropped and selected
     * @param target the target to which place will be dropped node
     */
    public void dropBeforeAndSelect(TreeNode<ForumStructureItem> node,
                                    TreeNode<ForumStructureItem> target) {
        structureTree.dropNodeBefore(node, target);
        structureTree.setSelectedNode(node);
        setSelectedItem(node.getData());
    }

    /**
     * Drops node in the target and selects it
     *
     * @param node   the node that will be dropped and selected
     * @param target the target in which will be dropped node
     */
    public void dropInAndSelect(TreeNode<ForumStructureItem> node,
                                TreeNode<ForumStructureItem> target) {
        structureTree.dropNodeIn(node, target);
        structureTree.setSelectedNode(node);
        setSelectedItem(node.getData());
    }

}
