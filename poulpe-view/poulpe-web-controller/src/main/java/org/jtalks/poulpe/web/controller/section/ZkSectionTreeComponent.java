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

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.ObjectUtils;
import org.jtalks.poulpe.model.entity.BranchSectionVisitable;
import org.jtalks.poulpe.model.entity.BranchSectionVisitor;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.web.controller.ZkHelper;
import org.springframework.util.TypeUtils;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.TreeNode;

/**
 * Tree component for rendering sections
 * 
 * @author Konstantin Akimov
 * @author Guram Savinov
 * @author Alexey Grigorev
 * @author Vyacheslav Zhivaev
 */
public class ZkSectionTreeComponent extends Div implements IdSpace {
    private static final long serialVersionUID = -1083425488934932487L;

    /**
     * Reference to the corresponding ZUL file
     */
    public static final String ZUL_REF = "WEB-INF/pages/sectionTree.zul";

    private ZkHelper zkHelper;

    private SectionPresenter presenter;

    private Tree sectionTree;
    private DefaultTreeNode<PoulpeSection> treeNode;

    /**
     * Package-private for initializing from tests
     * @param zkHelper zkHelper instance
     */
    ZkSectionTreeComponent(ZkHelper zkHelper) {
        this.zkHelper = zkHelper;
    }

    /**
     * @param section for which will be build tree
     * @param presenter instance section presenter
     */
    public ZkSectionTreeComponent(PoulpeSection section, SectionPresenter presenter) {
        this.zkHelper = new ZkHelper(this);
        init(section, presenter);
    }

    /**
     * Initializes the component, wires variables to corresponding ZUL file
     * 
     * @param section to be holded to component
     * @param presenter section presenter
     */
    public void init(PoulpeSection section, SectionPresenter presenter) {
        zkHelper.wireToZul(ZUL_REF);
        zkHelper.wireByConvention();

        this.presenter = presenter;

        treeNode = TreeNodeFactory.getTreeNode(section);
        DefaultTreeModel<PoulpeSection> model = prepareTreeModel(treeNode);

        sectionTree.setModel(model);
        sectionTree.setItemRenderer(new SectionBranchTreeitemRenderer(presenter));
    }

    /**
     * Prepares a model for given tree node with section
     * @param treeNode to be wrapped
     * @return tree model for given tree node
     */
    private static DefaultTreeModel<PoulpeSection> prepareTreeModel(DefaultTreeNode<PoulpeSection> treeNode) {
        List<DefaultTreeNode<PoulpeSection>> defaultTreeNodes = Collections.singletonList(treeNode);
        DefaultTreeNode<PoulpeSection> root = new DefaultTreeNode<PoulpeSection>(null, defaultTreeNodes);
        return new DefaultTreeModel<PoulpeSection>(root);
    }
    
    /**
     * Shows dialog for creating a new branch
     */
    public void newBranchDialog() {
        presenter.openNewBranchDialog(this);
    }

    /**
     * Shows a dialog for editing a branch
     */
    public void editDialog() {
        presenter.openEditDialog(this);
    }

    /**
     * Shows moderation dialog
     */
    public void moderationDialog() {
        BranchSectionVisitable selectedObject = getSelectedOrFirstElement();
        presenter.openModerationWindow(selectedObject);
    }

    /**
     * {@inheritDoc}
     */
    public void updateSectionInView(PoulpeSection section) {
        treeNode.setData(section);
    }

    /**
     * {@inheritDoc}
     */
    public BranchSectionVisitable getSelectedObject() {
        if (sectionTree.getSelectedCount() != 0) {
            return sectionTree.getSelectedItem().getValue();
        }

        return null;
    }

    /**
     * Show permission window for selected element. Selected element should be a
     * {@link PoulpeBranch}
     */
    public void showPermissionsWindow() {
        PoulpeBranch branch = (PoulpeBranch) getSelectedObject();
        presenter.openBranchPermissionsDialog(branch);
    }

    /**
     * Shows deletion dialog
     */
    public void deleteDialog() {
        BranchSectionVisitable selectedObject = getSelectedOrFirstElement();
        presenter.openDeleteDialog(selectedObject);
    }

    /**
     * (Open for spying)
     * 
     * @return selected element, or first one if none selected
     */
    public BranchSectionVisitable getSelectedOrFirstElement() {
        BranchSectionVisitable selectedObject = getSelectedObject();

        if (selectedObject == null) {
            selectedObject = getFirstElement();
        }

        return selectedObject;
    }

    /**
     * @return first element
     */
    private BranchSectionVisitable getFirstElement() {
        TreeModel<DefaultTreeNode<BranchSectionVisitable>> model = sectionTree.getModel();
        DefaultTreeNode<BranchSectionVisitable> root = model.getRoot();
        TreeNode<BranchSectionVisitable> child = root.getChildAt(0);
        return child.getData();
    }

    /**
     * @param sectionTree a tree of sections
     */
    public void setSectionTree(Tree sectionTree) {
        this.sectionTree = sectionTree;
        addDisablingForSections();
    }

    /**
     * For all components adds listened which disables/enables UI elements based
     * on their types
     */
    private void addDisablingForSections() {
        sectionTree.addEventListener(Events.ON_SELECT, new EventListener<Event>() {
            @Override
            public void onEvent(Event event) throws Exception {
                disableModeratorsButtonIfNeeded(getSelectedObject());
                disablePermissionsButtonIfNeeded(getSelectedObject());
            }
        });
    }

    /**
     * Decides on whether moderators button should be disabled or not.
     * For branches it should be enabled, for section - disabled.
     * 
     * @param visitable to be visited (section or branch)
     */
    public void disableModeratorsButtonIfNeeded(BranchSectionVisitable visitable) {
        visitable.apply(new BranchSectionVisitor() {
            @Override
            public void visitSection(PoulpeSection section) {
                getModeratorsButton().setDisabled(true);
            }

            @Override
            public void visitBranch(PoulpeBranch branch) {
                getModeratorsButton().setDisabled(false);
            }
        });
    }

    /**
     * Decides on whether branch permission button should be disabled or not.
     * For branches it should be enabled, for section - disabled.
     * 
     * @param visitable to be visited (section or branch)
     */
    public void disablePermissionsButtonIfNeeded(BranchSectionVisitable visitable) {
        visitable.apply(new BranchSectionVisitor() {
            @Override
            public void visitSection(PoulpeSection section) {
                getBranchPermissionsButton().setDisabled(true);
            }

            @Override
            public void visitBranch(PoulpeBranch branch) {
                getBranchPermissionsButton().setDisabled(false);
            }
        });
    }


    /**
     * @return moderators button
     */
    Button getModeratorsButton() {
        return (Button) sectionTree.getFellow("moderatorsButton");
    }

    /**
     * @return permission button
     */
    Button getBranchPermissionsButton() {
        return (Button) sectionTree.getFellow("permissionsButton");
    }

    /**
     * Event which happen when user click on add branch button
     */
    public void onClick$addBranchButton() {
        newBranchDialog();
    }

    /**
     * Event which happen when user double click on section or branch
     */
    public void onDoubleClick$sectionTree() {
        editDialog();
    }

    /**
     * Event which happen when user click on moderators button
     */
    public void onClick$moderatorsButton() {
        moderationDialog();
    }

    /**
     * Event which happen when user click on remove button
     */
    public void onClick$delButton() {
        deleteDialog();
    }

    /**
     * Event which happen when user click on permissions button
     */
    public void onClick$permissionsButton() {
        showPermissionsWindow();
    }

}
