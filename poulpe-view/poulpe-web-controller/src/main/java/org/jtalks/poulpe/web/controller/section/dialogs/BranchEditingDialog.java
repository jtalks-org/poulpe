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
package org.jtalks.poulpe.web.controller.section.dialogs;

import org.jtalks.common.model.entity.Group;
import org.jtalks.poulpe.model.dao.GroupDao;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.web.controller.section.ForumStructureItem;
import org.jtalks.poulpe.web.controller.zkutils.ZkTreeModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.TreeNode;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * View model that manages branch editing dialog and its data (like section list in dropdown).
 *
 * @author stanislav bashkirtsev
 */
public class BranchEditingDialog {
    private final GroupDao groupDao;
    private final ListModelList<ForumStructureItem> sectionList = new ListModelList<ForumStructureItem>();
    private final GroupList groupList = new GroupList();
    private ForumStructureItem editedBranch = new ForumStructureItem(new PoulpeBranch());
    private boolean showDialog;

    public BranchEditingDialog(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    /**
     * Closes the dialog without removing any underlying state.
     *
     * @return this
     */
    public BranchEditingDialog closeDialog() {
        showDialog = false;
        return this;
    }

    /**
     * Sets the showDialog flag to true so that next time {@link #isShowDialog()} will return true.
     *
     * @return decides to show the dialog
     */
    public BranchEditingDialog showDialog() {
        groupList.setGroups(groupDao.getAll());
        showDialog = true;
        return this;
    }

    /**
     * By this flag the ZUL decides whether to show the branch editing dialog or not.
     *
     * @return whether to show the branch editing dialog
     */
    public boolean isShowDialog() {
        boolean result = showDialog;
        showDialog = false;
        return result;
    }

    /**
     * Sets the specified section as the one that is selected in dropdown list.
     *
     * @param sectionItem an item to be selected in the dropdown at branch editing dialog
     * @return this
     */
    public BranchEditingDialog selectSection(@Nonnull ForumStructureItem sectionItem) {
        sectionList.addToSelection(sectionItem);
        return this;
    }

    /**
     * Gets the list of available sections so that it's possible to place the branch to some other section.
     *
     * @return the list of available sections so that it's possible to place the branch to some other section
     */
    public ListModelList<ForumStructureItem> getSectionList() {
        return sectionList;
    }

    /**
     * Clears the previous sections and gets the new ones from the specified tree.
     *
     * @param sectionTree a forum structure tree to get sections from it
     * @return this
     */
    public BranchEditingDialog renewSectionsFromTree(@Nonnull ZkTreeModel<ForumStructureItem> sectionTree) {
        this.sectionList.clear();
        List<ForumStructureItem> sections = unwrapSections(sectionTree.getRoot().getChildren());
        this.sectionList.addAll(sections);
        return this;
    }

    /**
     * Gets the {@link ForumStructureItem}s from the {@link TreeNode}s.
     *
     * @param sectionNodes to be converted to the {@link ForumStructureItem}
     * @return a list of {@link ForumStructureItem} unwrapped from specified nodes
     */
    private List<ForumStructureItem> unwrapSections(List<TreeNode<ForumStructureItem>> sectionNodes) {
        List<ForumStructureItem> sections = new ArrayList<ForumStructureItem>();
        for (TreeNode<ForumStructureItem> sectionNode : sectionNodes) {
            sections.add(sectionNode.getData());
        }
        return sections;
    }

    /**
     * Gets the section that was chosen in the list of available sections while moving the branch to another section (or
     * creating new).
     *
     * @return the section that was chosen in the list of available sections
     */
    public ForumStructureItem getSectionSelectedInDropdown() {
        return sectionList.getSelection().iterator().next();
    }

    public ForumStructureItem getEditedBranch() {
        return editedBranch;
    }

    public BranchEditingDialog setEditedBranch(ForumStructureItem editedBranch) {
        this.editedBranch = editedBranch;
        return this;
    }

    public List<Group> getCandidatesToModerate() {
        return groupList.getGroups();
    }

    /**
     * Gets the group that is equal to the one that is currently moderating the selected branch. A new group with the
     * empty fields will be created if there is no moderating group of the branch (it's a new branch). Note, that this
     * method will be used by ZK in order to identify currently selected item in Combo Box, which means that it doesn't
     * actually need a real object to be returned, but it will be enough if we return an equal object (in our case
     * equals means they have the same UUID). Due to this and due to the problem with ZK binding (Hibernate will return
     * a proxy here, but method {@link #getCandidatesToModerate()} returns non-proxies, and when ZK tries to set the
     * value, it throws a class cast exception because proxy != a usual class instance). That's why this method returns
     * an instance that is equal to the real moderating group, not the actual one.
     *
     * @return the group that is equal to the one that is currently moderating the selected branch
     */
    public Group getModeratingGroup() {
        Group currentModeratorsGroup = editedBranch.getBranchItem().getModeratorsGroup();
        return groupList.getEqual(currentModeratorsGroup);
    }

    public void setModeratingGroup(Group moderatingGroup) {
        editedBranch.getBranchItem().setModeratorsGroup(moderatingGroup);
    }

    /**
     * Used by ZK in order to set the field from the form if we want to create a moderating group along with the branch
     * instead of using some existing group. Does nothing if the specified name is empty.
     *
     * @param groupNameToCreate
     */
    public void setGroupToCreate(String groupNameToCreate) {
        if (!groupNameToCreate.isEmpty()) {
            setModeratingGroup(new Group(groupNameToCreate));
        }
    }
}
