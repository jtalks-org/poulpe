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
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.service.ForumStructureService;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.branch.BranchPermissionManagementVm;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Treeitem;

import javax.annotation.Nonnull;
import java.util.List;

import static org.jtalks.poulpe.web.controller.section.TreeNodeFactory.buildForumStructure;

/**
 * Is used in order to work with page that allows admin to manage sections and branches (moving them, reordering,
 * removing, editing, etc.). Note, that this class is responsible for back-end of the operations (presenter,
 * controller), so it stores all the changes to the database using {@link ComponentService}. In order to control the
 * view and what it should show/change, it uses {@link ForumStructureData}.
 *
 * @author stanislav bashkirtsev
 * @author Guram Savinov
 */
public class ForumStructureVm {
    private static final String SELECTED_ITEM_PROP = "selectedItemInTree", VIEW_DATA_PROP = "viewData", TREE_MODEL = "treeModel";
    private final ForumStructureService forumStructureService;
    private final WindowManager windowManager;
    private ForumStructureItem selectedItemInTree = new ForumStructureItem();
    private SelectedEntity<PoulpeBranch> selectedBranchForPermissions;
    private ForumStructureData viewData;
    private ForumStructureTreeModel treeModel;

    public ForumStructureVm(@Nonnull ForumStructureService forumStructureService, @Nonnull WindowManager windowManager,
                            @Nonnull SelectedEntity<PoulpeBranch> selectedBranchForPermissions,
                            ForumStructureData viewData) {
        this.forumStructureService = forumStructureService;
        this.windowManager = windowManager;
        this.selectedBranchForPermissions = selectedBranchForPermissions;
        this.viewData = viewData;
    }

    /**
     * Creates the whole sections and branches structure. Always hits database. Is executed each time a page is
     * opening.
     */
    @Init
    public void init() {
        treeModel = new ForumStructureTreeModel(buildForumStructure(loadJcommune()));
    }

    /**
     * Shows the dialog either for creating or for editing existing section.
     *
     * @param createNew whether or not it's a creating of new section or just editing existing one
     */
    @Command
    @NotifyChange({SELECTED_ITEM_PROP})
    public void showNewSectionDialog(@BindingParam("createNew") boolean createNew) {
        viewData.showSectionDialog(createNew);
    }

    @GlobalCommand
    @NotifyChange(TREE_MODEL)
    public void refreshTree() {
    }

    /**
     * Shows the confirmation message before deleting the branch.
     *
     * @see ForumStructureData#getSelectedItem()
     */
    @Command
    @NotifyChange({VIEW_DATA_PROP})
    public void deleteSelected() {
        if (viewData.getSelectedItem().isBranch()) {
            viewData.getConfirmBranchDeletionDialogVm().showDialog();
        } else {
            viewData.getConfirmSectionDeletionDialogVm().showDialog();
        }
    }

    /**
     * Deletes the selected branch. It does both: back-end removal from DB and ask the {@link ForumStructureData} to
     * remove the item from the tree.
     *
     * @see ForumStructureData#getSelectedItem()
     */
    @Command
    @NotifyChange({VIEW_DATA_PROP, SELECTED_ITEM_PROP})
    public void confirmBranchDeletion() {
        ForumStructureItem selectedItem = viewData.removeSelectedItem();
        deleteSelectedBranch(selectedItem.getBranchItem());
    }

    /**
     * Deletes the selected section. It does both: back-end removal from DB and ask the {@link ForumStructureData} to
     * remove the item from the tree.
     *
     * @see ForumStructureData#getSelectedItem()
     */
    @Command
    @NotifyChange({VIEW_DATA_PROP, SELECTED_ITEM_PROP})
    public void confirmSectionDeletion() {
        ForumStructureItem selectedItem = viewData.removeSelectedItem();
        deleteSelectedSection(selectedItem.getSectionItem());

    }

    void deleteSelectedSection(PoulpeSection selectedSection) {
        Jcommune jcommune = forumStructureService.deleteSectionWithBranches(selectedSection);
//        viewData.setStructureTree(new ForumStructureTreeModel(buildForumStructure(jcommune)));
    }

    void deleteSelectedBranch(PoulpeBranch branchItem) {
        forumStructureService.removeBranch(branchItem);
    }

    /**
     * Opens a separate page - Branch Permissions where admin can edit what Groups have wha Permissions on the selected
     * branch.
     */
    @Command
    public void openBranchPermissions() {
        selectedBranchForPermissions.setEntity(getSelectedItemInTree().getBranchItem());
        BranchPermissionManagementVm.showPage(windowManager);
    }

    public void updateSectionInTree(PoulpeSection section){
        treeModel.addIfAbsent(section);
        selectedItemInTree.setItem(section);
    }

    @Command
    @NotifyChange({TREE_MODEL, SELECTED_ITEM_PROP})
    public void updateBranchInTree(PoulpeBranch branch) {
        treeModel.moveBranchIfSectionChanged(branch);
        selectedItemInTree.setItem(branch);
    }

    public ForumStructureData getViewData() {
        return viewData;
    }

    public ForumStructureItem getSelectedItemInTree() {
        return selectedItemInTree;
    }

    public void setSelectedItemInTree(ForumStructureItem selectedItemInTree) {
        this.selectedItemInTree = selectedItemInTree;
    }

    /**
     * Is used by ZK binder to inject the section that is currently selected.
     *
     * @param selectedNode the section that is currently selected
     */
    @NotifyChange(SELECTED_ITEM_PROP)
    public void setSelectedNode(DefaultTreeNode<ForumStructureItem> selectedNode) {
        this.selectedItemInTree = selectedNode.getData();
    }

    /**
     * Loads instance of JCommune from database.
     *
     * @return instance of JCommune from database
     */
    private Jcommune loadJcommune() {
        Jcommune jcommune = forumStructureService.getJcommune();
        if (jcommune == null) {
            throw new IllegalStateException("Please, create a Forum Component first.");
        }
        return jcommune;
    }

    /**
     * Handler of event when one item was dragged and dropped to another.
     *
     * @param event contains all needed info about event
     */
    @Command
    @NotifyChange(VIEW_DATA_PROP)
    public void onDropItem(@BindingParam("event") DropEvent event) {
        DefaultTreeNode<ForumStructureItem> draggedNode = ((Treeitem) event.getDragged()).getValue();
        DefaultTreeNode<ForumStructureItem> targetNode = ((Treeitem) event.getTarget()).getValue();
        ForumStructureItem draggedItem = draggedNode.getData();
        ForumStructureItem targetItem = targetNode.getData();
        if (draggedItem.isBranch()) {
            PoulpeBranch draggedBranch = draggedItem.getBranchItem();
            if (noEffectAfterDrop(draggedBranch, targetItem)) {
                return;
            }
            if (targetItem.isBranch()) {
                forumStructureService.moveBranch(draggedBranch, targetItem.getBranchItem());
                viewData.dropBeforeAndSelect(draggedNode, targetNode);
            } else {
                forumStructureService.moveBranch(draggedBranch, targetItem.getSectionItem());
                viewData.dropInAndSelect(draggedNode, targetNode);
            }
        }
    }

    /**
     * Checks that dropping branch haven't effect.
     *
     * @param draggedBranch the branch to move
     * @param targetItem    the target item, may be branch as well as section
     * @return {@code true} if dropping have no effect, otherwise return {@code false}
     */
    private boolean noEffectAfterDrop(PoulpeBranch draggedBranch,
                                      ForumStructureItem targetItem) {
        PoulpeSection draggedSection = draggedBranch.getPoulpeSection();
        if (targetItem.isSection()) {
            if (draggedSection.equals(targetItem.getSectionItem())) {
                return true;
            }
        }

        PoulpeBranch targetBranch = targetItem.getBranchItem();
        PoulpeSection targetSection = targetBranch.getPoulpeSection();
        if (draggedSection.equals(targetSection)) {
            List<PoulpeBranch> branches = draggedSection.getPoulpeBranches();
            int draggedIndex = branches.indexOf(draggedBranch);
            int targetIndex = branches.indexOf(targetBranch);
            if (targetIndex - 1 == draggedIndex) {
                return true;
            }
        }

        return false;
    }

    public ForumStructureTreeModel getTreeModel() {
        return treeModel;
    }
}
