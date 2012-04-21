package org.jtalks.poulpe.web.controller.section;

import org.jtalks.common.model.entity.Entity;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.service.SectionService;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author stanislav bashkirtsev
 */
public class ForumStructureData {
    private ListModelList<ForumStructureItem> sectionList = new ListModelList<ForumStructureItem>();
    private ForumStructureItem selectedItem = new ForumStructureItem();
    private DefaultTreeModel<ForumStructureItem> sectionTree;
    private boolean showSectionDialog;
    private boolean showBranchDialog;

    public Jcommune getRootAsJcommune() {
        return (Jcommune) (Object) sectionTree.getRoot().getData();
    }

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

    public ForumStructureData removeSelectedItem(){
        if(!getSelectedItem().isBranch()){
            int[] sectionIndex = sectionTree.getSelectionPath();
            sectionTree.getRoot().remove(sectionIndex[0]);
        }
        setSelectedItem(new ForumStructureItem());
        return this;
    }

    public ListModelList<ForumStructureItem> getSectionList() {
        return sectionList;
    }

    public ForumStructureItem getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(ForumStructureItem selectedItem) {
        this.selectedItem = selectedItem;
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

    public ForumStructureData addSectionIfNew(PoulpeSection section){
        if(section.getId() == 0){
            sectionTree.getRoot().add(new DefaultTreeNode<ForumStructureItem>(new ForumStructureItem(section)));
        }
        return this;
    }

    public ForumStructureData addBranchIfNew(ForumStructureItem section, PoulpeBranch branch){
        if(branch.getId() == 0){
            List<TreeNode<ForumStructureItem>> sectionNodes = sectionTree.getRoot().getChildren();
            for(TreeNode<ForumStructureItem> node: sectionNodes){
                if(section == node.getData()){
                    node.add(new DefaultTreeNode<ForumStructureItem>(new ForumStructureItem(branch)));
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
