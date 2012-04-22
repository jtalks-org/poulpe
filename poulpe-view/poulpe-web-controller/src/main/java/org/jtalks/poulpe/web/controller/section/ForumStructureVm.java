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

import com.google.common.annotations.VisibleForTesting;
import org.jtalks.common.model.entity.ComponentType;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.service.ComponentService;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.ListModel;

import javax.validation.constraints.NotNull;

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
    private static final String SELECTED_ITEM_PROP = "selectedItem", VIEW_DATA_PROP = "viewData";
    private final ComponentService componentService;
    private ForumStructureData viewData = new ForumStructureData();


    public ForumStructureVm(@NotNull ComponentService componentService) {
        this.componentService = componentService;
    }

    /**
     * Creates the whole sections and branches structure. Always hits database. Is executed each time a page is
     * opening.
     */
    @Init
    public void init() {
        viewData.setSectionTree(new DefaultTreeModel<ForumStructureItem>(buildForumStructure(loadJcommune())));
    }

    /**
     * Shows the dialog either for creating or for editing existing section.
     *
     * @param createNew whether or not it's a creating of new section or just editing existing one
     */
    @Command
    @NotifyChange({VIEW_DATA_PROP, SELECTED_ITEM_PROP})
    public void showNewSectionDialog(@BindingParam("createNew") boolean createNew) {
        viewData.showSectionDialog(createNew);
    }

    /**
     * Shows the dialog for creating or editing the branch. Whether it's a creating or editing is decided by the
     * specified parameter.
     *
     * @param createNew pass {@code true} if this is a window for creating a new branch
     */
    @Command
    @NotifyChange({VIEW_DATA_PROP, SELECTED_ITEM_PROP})
    public void showNewBranchDialog(@BindingParam("createNew") boolean createNew) {
        viewData.showBranchDialog(createNew);
    }

    /**
     * Deletes the selected entity no matter what it is - a branch or a section. It does both - back end removal from DB
     * and ask the {@link ForumStructureData} to remove the item from the tree.
     *
     * @see ForumStructureData#getSelectedItem()
     */
    @Command
    @NotifyChange({VIEW_DATA_PROP, SELECTED_ITEM_PROP})
    public void deleteSelected() {
        Jcommune jcommune = viewData.getRootAsJcommune();
        ForumStructureItem selectedItem = viewData.removeSelectedItem();
        if (selectedItem.isBranch()) {
            PoulpeBranch branch = selectedItem.getItem(PoulpeBranch.class);
            branch.getSection().deleteBranch(branch);
            branch.setSection(null);
        } else {
            jcommune.getSections().remove(selectedItem.getItem(PoulpeSection.class));
        }
        componentService.saveComponent(jcommune);
    }

    /**
     * Saves the {@link #getSelectedItem} to the database, adds it as the last one to the list of sections and cleans
     * the selected section. Also makes the create section dialog to be closed.
     */
    @Command
    @NotifyChange({VIEW_DATA_PROP})
    public void saveSection() {
        storeNewSection(viewData.getSelectedEntity(PoulpeSection.class));
        viewData.addSelectedSectionToTreeIfNew();
        viewData.closeDialog();
    }

    void storeNewSection(PoulpeSection section) {
        Jcommune jcommune = viewData.getRootAsJcommune();
        jcommune.addSection(section);
        componentService.saveComponent(jcommune);
    }

    @Command
    @NotifyChange({VIEW_DATA_PROP, SELECTED_ITEM_PROP})
    public void saveBranch() {
        viewData.putSelectedBranchToSectionInDropdown();
        storeSelectedBranch();
    }

    /**
     * Stores the branch that is selected in the {@link #viewData} to the database. Adds it to the list of branches of
     * the section selected in {@link ForumStructureData#getSelectedEntity(Class)} if the branch is new or was moved to
     * another section.
     */
    void storeSelectedBranch() {
        Jcommune jcommune = viewData.getRootAsJcommune();
        PoulpeBranch selectedBranch = viewData.getSelectedEntity(PoulpeBranch.class);
        PoulpeSection section = viewData.getSectionSelectedInDropDown();
        selectedBranch.getSection().deleteBranch(selectedBranch);
        section.addOrUpdateBranch(selectedBranch);
        selectedBranch.setSection(section);
        componentService.saveComponent(jcommune);
    }

    /**
     * Returns all the sections from our database in list model representation in order they are actually sorted.
     *
     * @return all the sections from our database in list model representation in order they are actually sorted or
     *         empty tree if there are no sections. Can't return {@code null}.
     */
    public ListModel<ForumStructureItem> getSectionList() {
        return viewData.getSectionList();
    }

    public ForumStructureData getViewData() {
        return viewData;
    }

    public ForumStructureItem getSelectedItem() {
        return viewData.getSelectedItem();
    }

    public void setSelectedItem(ForumStructureItem selectedItem) {
        this.viewData.setSelectedItem(selectedItem);
    }

    /**
     * Is used by ZK binder to inject the section that is currently selected.
     *
     * @param selectedNode the section that is currently selected
     */
    @NotifyChange(SELECTED_ITEM_PROP)
    public void setSelectedNode(DefaultTreeNode<ForumStructureItem> selectedNode) {
        this.viewData.setSelectedItem(selectedNode.getData());
    }

    private Jcommune loadJcommune() {
        return (Jcommune) componentService.getByType(ComponentType.FORUM);
    }

    @VisibleForTesting
    void setViewData(ForumStructureData viewData) {
        this.viewData = viewData;
    }
}
