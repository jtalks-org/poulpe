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

import org.jtalks.common.model.entity.ComponentType;
import org.jtalks.common.model.entity.Section;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.service.ComponentService;
import org.zkoss.bind.annotation.*;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.ListModel;

import javax.validation.constraints.NotNull;

/**
 * Is used in order to work with page that allows admin to manage sections and branches (moving them, reordering,
 * removing, editing, etc.).
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

    @Init
    public void init(){
        viewData.setSectionTree(new DefaultTreeModel<ForumStructureItem>(TreeNodeFactory.buildForumStructure(loadJcommune())));
    }

    @Command
    @NotifyChange({VIEW_DATA_PROP, SELECTED_ITEM_PROP})
    public void showNewSectionDialog(@BindingParam("createNew") boolean createNew) {
        viewData.showSectionDialog(createNew);
    }

    @Command
    @NotifyChange({VIEW_DATA_PROP, SELECTED_ITEM_PROP})
    public void showNewBranchDialog(@BindingParam("createNew") boolean createNew) {
        viewData.showBranchDialog(createNew);
    }

    @Command
    @NotifyChange({VIEW_DATA_PROP, SELECTED_ITEM_PROP})
    public void deleteSelectedSection() {
        Jcommune jcommune = viewData.getRootAsJcommune();
        jcommune.getSections().remove(viewData.getSelectedItem().getItem(PoulpeSection.class));
        componentService.saveComponent(jcommune);
        viewData.removeSelectedItem();
    }

    /**
     * Saves the {@link #getSelectedItem} to the database, adds it as the last one to the list of sections and cleans
     * the selected section. Also makes the create section dialog to be closed.
     */
    @Command
    @NotifyChange({VIEW_DATA_PROP})
    public void saveSection() {
        Jcommune jcommune = viewData.getRootAsJcommune();
        PoulpeSection section = viewData.getSelectedItem().getItem(PoulpeSection.class);
        viewData.addSectionIfNew(section);
        jcommune.addSection(section);
        componentService.saveComponent(jcommune);
        viewData.closeDialog();
    }

    @Command
    @NotifyChange(VIEW_DATA_PROP)
    public void saveBranch() {
        Jcommune jcommune = viewData.getRootAsJcommune();
        PoulpeBranch branch = viewData.getSelectedItem().getItem(PoulpeBranch.class);
        ForumStructureItem sectionTreeItem = viewData.getSectionSelectedInDropDown();
        viewData.addBranchIfNew(sectionTreeItem, branch);
        PoulpeSection section = sectionTreeItem.getItem(PoulpeSection.class);
        section.addOrUpdateBranch(branch);
        branch.setSection(section);
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
}
