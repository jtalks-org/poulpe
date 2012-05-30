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
        return groupDao.getAll();
    }

    public Group getModeratingGroup() {
        Group moderatorsGroup = editedBranch.getBranchItem().getModeratorsGroup();
        if (moderatorsGroup == null) {
            moderatorsGroup = new Group("Moderators of " + editedBranch.getBranchItem().getName());
        }
        return moderatorsGroup;
    }

    public void setModeratingGroup(Group moderatingGroup) {
        editedBranch.getBranchItem().setModeratorsGroup(moderatingGroup);
    }

    public String getGroupToCreate() {
        return "Moderators of " + editedBranch.getBranchItem().getName();
    }

    public void setGroupToCreate(String groupNameToCreate) {
        setModeratingGroup(new Group(groupNameToCreate));
    }
}
