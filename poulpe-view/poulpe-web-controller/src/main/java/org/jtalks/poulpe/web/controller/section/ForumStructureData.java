package org.jtalks.poulpe.web.controller.section;

import org.jtalks.common.model.entity.Entity;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.TreeNode;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Is used to contain data for the view and do operations on view. It doesn't do any business logic, only logic that is
 * connected to presenting the data. In MVP it would be a View.
 *
 * @author stanislav bashkirtsev
 */
public class ForumStructureData {
    private ListModelList<ForumStructureItem> sectionList = new ListModelList<ForumStructureItem>();
    private ForumStructureItem selectedItem = new ForumStructureItem();
    private DefaultTreeModel<ForumStructureItem> sectionTree;
    private boolean showSectionDialog;
    private boolean showBranchDialog;

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
     * If the branch editing/creating dialog was open and there was a dropdown with the list of sections and user had to
     * choose to what section this branch had to go, this method would returned the selected value.
     *
     * @return the section that was selected during editing/creating of the branch
     */
    public ForumStructureItem getSectionSelectedInDropDown() {
        Set<ForumStructureItem> selection = sectionList.getSelection();
        if (selection.isEmpty()) {
            return null;
        }
        return selection.iterator().next();
    }

    public ForumStructureData closeDialog() {
        showSectionDialog = false;
        showBranchDialog = false;
        return this;
    }

    public ForumStructureData showBranchDialog(boolean createNew) {
        if (createNew) {
            selectedItem = new ForumStructureItem(new PoulpeBranch());
        }
        TreeNode<ForumStructureItem> section = sectionTree.getRoot().getChildAt(sectionTree.getSelectionPath()[0]);
        sectionTree.clearSelection();
        sectionList.addToSelection(section.getData());
        showBranchDialog = true;
        return this;
    }

    public ForumStructureData showSectionDialog(boolean createNew) {
        if (createNew) {
            selectedItem = new ForumStructureItem(new PoulpeSection());
        }
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
        int[] selectedPath = sectionTree.getSelectionPath();
        if (getSelectedItem().isBranch()) {
            sectionTree.getRoot().getChildAt(selectedPath[0]).remove(selectedPath[1]);
        } else {
            sectionTree.getRoot().remove(selectedPath[0]);
        }
        return setSelectedItem(new ForumStructureItem());
    }

    public ListModelList<ForumStructureItem> getSectionList() {
        return sectionList;
    }

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

    public boolean isShowBranchDialog() {
        boolean show = showBranchDialog && selectedItem.isBranch();
        this.showBranchDialog = false;
        return show;
    }

    public boolean isShowSectionDialog() {
        boolean show = showSectionDialog && !selectedItem.isBranch();
        this.showSectionDialog = false;
        return show;
    }

    public DefaultTreeModel<ForumStructureItem> getSectionTree() {
        return sectionTree;
    }

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
        if (getSelectedItem().isPersisted()) {
            TreeNode<ForumStructureItem> sectionNode = new DefaultTreeNode<ForumStructureItem>(
                    getSelectedItem(), new ArrayList<TreeNode<ForumStructureItem>>());
            getSectionTree().getRoot().add(sectionNode);
            getSectionTree().addToSelection(sectionNode);
            getSectionList().add(sectionNode.getData());
        }
        return this;
    }

    public ForumStructureData addBranchIfNew(ForumStructureItem section, ForumStructureItem branchItem) {
        if (branchItem.isPersisted()) {
            List<TreeNode<ForumStructureItem>> sectionNodes = sectionTree.getRoot().getChildren();
            for (TreeNode<ForumStructureItem> node : sectionNodes) {
                if (section == node.getData()) {
                    TreeNode<ForumStructureItem> branchNode = new DefaultTreeNode<ForumStructureItem>(branchItem);
                    node.add(branchNode);
                    sectionTree.addToSelection(branchNode);
                    sectionTree.addOpenObject(node);
                }
            }
        }
        return this;
    }

    public void setSectionTree(DefaultTreeModel<ForumStructureItem> sectionTree) {
        this.sectionTree = sectionTree;
        this.sectionList.clear();
        List<ForumStructureItem> sections = unwrap(sectionTree.getRoot().getChildren());
        this.sectionList.addAll(sections);
    }

    private List<ForumStructureItem> unwrap(List<TreeNode<ForumStructureItem>> sectionNodes) {
        List<ForumStructureItem> sections = new ArrayList<ForumStructureItem>();
        for (TreeNode<ForumStructureItem> sectionNode : sectionNodes) {
            sections.add(sectionNode.getData());
        }
        return sections;
    }
}
