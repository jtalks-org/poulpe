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
import org.jtalks.poulpe.web.controller.zkutils.ZkTreeModel;
import org.jtalks.poulpe.web.controller.zkutils.ZkTreeNode;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.TreeNode;

import javax.annotation.Nonnull;
import java.util.ArrayList;

/**
 * Is used to contain data for the view and do operations on view. It doesn't do any business logic, only logic that is
 * connected to presenting the data. In MVP it would be a View.
 *
 * @author stanislav bashkirtsev
 * @author Guram Savinov
 */
public class ForumStructureData {
    private final BranchEditingDialog branchDialog = new BranchEditingDialog();
    private ForumStructureItem selectedItem = new ForumStructureItem();
    private ZkTreeModel<ForumStructureItem> sectionTree;
    private boolean showSectionDialog;

    /**
     * Since the {@link Jcommune} object is the root of the Forum Structure tree, this method can fetch this object from
     * the tree and return it.
     *
     * @return the {@link Jcommune} object that is the root of the Forum Structure tree
     */
    public Jcommune getRootAsJcommune() {
        return (Jcommune) (Object) sectionTree.getRoot().getData();
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
     * Shows the branch dialog and decides whether it should be a dialog for the new branch creation or it will be an
     * editing of the existing item.
     *
     * @param createNew a flag to decide whether we're going to create a new branch or will be editing the existing
     *                  one
     * @return this
     */
    public ForumStructureData showBranchDialog(boolean createNew) {
        selectedItem = selectedItem.prepareBranchItemForEditing(createNew);
        ForumStructureItem containingSection = sectionTree.getChildData(sectionTree.getSelectionPath()[0]);
        sectionTree.clearSelection();
        branchDialog.selectSection(containingSection).showDialog();
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
        return sectionTree.removeSelected().getData();
    }

    /**
     * Gets the section that was chosen in the list of available sections while moving the branch to another section (or
     * creating new).
     *
     * @return the section that was chosen in the list of available sections
     */
    public ForumStructureItem getSectionSelectedInDropdown() {
        return getSectionList().getSelection().iterator().next();
    }

    /**
     * The list of available sections (in the branch dialog where Admin can choose to what section this branch should
     * go).
     *
     * @return the list of available sections
     */
    public ListModelList<ForumStructureItem> getSectionList() {
        return branchDialog.getSectionList();
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
     * Figures out whether the Branch Editing Dialog should be shown right now. It also changes the flag to {@code
     * false} each time because we don't send anything to the server when closing window, so during next change
     * notification ZK will think that we need to show that dialog again which is wrong.
     *
     * @return {@code true} if the Branch Editing dialog should be shown
     * @see #showBranchDialog
     */
    public boolean isShowBranchDialog() {
        boolean show = branchDialog.isShowDialog() && selectedItem.isBranch();
        branchDialog.closeDialog();
        return show;
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
     * The whole tree of sections and branches as a ZK tree model.
     *
     * @return the tree of sections and branches
     */
    public ZkTreeModel<ForumStructureItem> getSectionTree() {
        return sectionTree;
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
     * If a new section was created (and thus was put into {@link #setSelectedItem(ForumStructureItem)}, then we need to
     * add that section to the Forum Structure tree to the last position. This method does exactly this and it results
     * in no-op if the selected section is already persistent.
     *
     * @return this
     */
    public ForumStructureData addSelectedSectionToTreeIfNew() {
        if (!getSelectedItem().isPersisted()) {
            TreeNode<ForumStructureItem> sectionNode = new DefaultTreeNode<ForumStructureItem>(
                    getSelectedItem(), new ArrayList<TreeNode<ForumStructureItem>>());
            getSectionTree().getRoot().add(sectionNode);
            getSectionTree().addToSelection(sectionNode);
            getSectionList().add(sectionNode.getData());
        }
        return this;
    }

    /**
     * Finds the branch that is currently set as selected and if this is a new branch (was just added via branch
     * creation dialog), adds it to the list of branches of the section. The section that is chosen - the one that was
     * selected in the dropdown list of sections (and it can be accessed via {@link #getSectionSelectedInDropdown()}. It
     * results in the branch been moved to another section if the currently selected section is not persisted yet. When
     * you trigger this method, conditions should be met:
     * <pre>
     *   <li>{@link #getSelectedItem()} should return a non null branch (no matter whether it's already persistent or
     *         not)
     *   </li>
     *   <li>{@link #getSectionSelectedInDropdown()} should return a section chosen in the Branch Editing dialog</li>
     * </pre>
     *
     * @return this
     */
    public ForumStructureData putSelectedBranchToSectionInDropdown() {
        ForumStructureItem branchToPut = getSelectedItem();
        TreeNode<ForumStructureItem> destinationSectionNode = getSectionTree().find(getSectionSelectedInDropdown());
        ZkTreeNode<ForumStructureItem> branchNodeToPut = (ZkTreeNode<ForumStructureItem>) getSectionTree().find(branchToPut);
        if (branchNodeToPut == null) {
            branchNodeToPut = new ZkTreeNode<ForumStructureItem>(branchToPut);
        }
        branchNodeToPut.moveTo(destinationSectionNode);
        getSectionTree().addToSelection(branchNodeToPut);
        getSectionTree().addOpenObject(destinationSectionNode);
        return this;
    }

    /**
     * Sets and rebuilds the Forum Structure tree and also updates the branch dialog with the list of available sections
     * so that in combo box we have only sections that are present in the section tree.
     *
     * @param sectionTree the new tree of sections and branches to be shown on the Forum Structure page
     */

    public void setSectionTree(@Nonnull ZkTreeModel<ForumStructureItem> sectionTree) {
        this.sectionTree = sectionTree;
        branchDialog.renewSectionsFromTree(sectionTree);
    }

}
