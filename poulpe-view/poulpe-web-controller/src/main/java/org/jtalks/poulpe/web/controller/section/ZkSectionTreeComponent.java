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

import org.jtalks.poulpe.model.entity.BranchSectionVisitable;
import org.jtalks.poulpe.model.entity.BranchSectionVisitor;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.web.controller.ZkHelper;
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

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @author Konstantin Akimov
 * @author Guram Savinov
 * @author Alexey Grigorev
 */
public class ZkSectionTreeComponent extends Div implements IdSpace {
    private static final long serialVersionUID = -1083425488934932487L;

    /**
     * Reference to the corresponding ZUL file
     */
    public static final String ZUL_REF = "WEB-INF/pages/sectionTree.zul";

    private ZkHelper zkInitializer;

    private SectionPresenter presenter;

    private Tree sectionTree;
    private DefaultTreeNode<PoulpeSection> treeNode;

    ZkSectionTreeComponent(ZkHelper zkInitializer) {
        this.zkInitializer = zkInitializer;
    }

    /**
     * @param section   for which will be build tree
     * @param presenter instance section presenter
     */
    public ZkSectionTreeComponent(PoulpeSection section, SectionPresenter presenter) {
        this.zkInitializer = new ZkHelper(this);
        init(section, presenter);
    }

    public void init(PoulpeSection section, SectionPresenter presenter) {
        zkInitializer.wireToZul(ZUL_REF);
        zkInitializer.wireByConvention();

        this.presenter = presenter;

        treeNode = TreeNodeFactory.getTreeNode(section);
        DefaultTreeModel<PoulpeSection> model = prepareTreeModel(treeNode);

        sectionTree.setModel(model);
        sectionTree.setItemRenderer(new SectionBranchTreeitemRenderer(presenter));
    }

    public void newBranchDialog() {
        presenter.openNewBranchDialog(this);
    }

    public void editDialog() {
        presenter.openEditDialog(this);
    }

    public void moderationDialog() {
        presenter.openModerationWindow();
    }

    /**
     * {@inheritDoc}
     */
    public void updateSectionInView(PoulpeSection section) {
        treeNode.setData(section);
    }

    private static DefaultTreeModel<PoulpeSection> prepareTreeModel(DefaultTreeNode<PoulpeSection> treeNode) {
        List<DefaultTreeNode<PoulpeSection>> defaultTreeNodes = Collections.singletonList(treeNode);
        DefaultTreeNode<PoulpeSection> root = new DefaultTreeNode<PoulpeSection>(null, defaultTreeNodes);
        return new DefaultTreeModel<PoulpeSection>(root);
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

    public void showPermissionsWindow() {
        BranchSectionVisitable selectedObject = getSelectedObject();
        selectedObject.apply(showPermissionsVisitor);
    }

    private static BranchSectionVisitor showPermissionsVisitor = new BranchSectionVisitor() {
        @Override
        public void visitSection(PoulpeSection section) {
            Messagebox.show("This action not provided for section, please select a branch");
        }

        @Override
        public void visitBranch(PoulpeBranch branch) {
            Executions.sendRedirect("/sections/BranchPermissionManagement.zul?branchId=" + branch.getId());
        }
    };

    public void deleteDialog() {
        BranchSectionVisitable selectedObject = getSelectedOrFirstElement();
        presenter.openDeleteDialog(selectedObject);
    }

    /**
     * (Open for spying)
     * @return
     */
    public BranchSectionVisitable getSelectedOrFirstElement() {
        BranchSectionVisitable selectedObject = getSelectedObject();

        if (selectedObject == null) {
            selectedObject = getFirstElement();
        }

        return selectedObject;
    }

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

    private void addDisablingForSections() {
        sectionTree.addEventListener(Events.ON_SELECT, new EventListener<Event>() {
            @Override
            public void onEvent(Event event) throws Exception {
                disablePermissionsButtonIfNeeded(getSelectedObject());
            }
        });
    }

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

    Button getBranchPermissionsButton() {
        return (Button) sectionTree.getFellow("permissionsButton");
    }

    /**
     * event which happen when click on add branch button
     */
    public void onClick$addBranchButton() {
        newBranchDialog();
    }

    /**
     * event which happen when double click on section or branch
     */
    public void onDoubleClick$sectionTree() {
        editDialog();
    }

    /**
     * Click on '*' button
     */
    public void onClick$moderatorButton() {
        moderationDialog();
    }

    /**
     * Event which happen when user click on '-' button after it selected
     * section is going to be deleted
     */
    public void onClick$delButton() {
        deleteDialog();
    }

    public void onClick$permissionsButton() throws IOException {
        showPermissionsWindow();
    }

}
