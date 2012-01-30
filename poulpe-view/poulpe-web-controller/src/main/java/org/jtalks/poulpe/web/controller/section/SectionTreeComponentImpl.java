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

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.BranchSectionVisitable;
import org.jtalks.poulpe.model.entity.BranchSectionVisitor;
import org.jtalks.poulpe.model.entity.Section;
import org.zkoss.zk.ui.Components;
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
 * @author Konstantin Akimov
 * @author Guram Savinov
 * @author Alexey Grigorev
 */
public class SectionTreeComponentImpl extends Div implements IdSpace {
    private static final long serialVersionUID = -1083425488934932487L;

    /**
     * Reference to a corresponding ZUL file
     */
    private static final String ZUL_REF = "WEB-INF/pages/sectionTree.zul";

    private Tree sectionTree;
    private final SectionPresenter presenter;
    
    private DefaultTreeNode<Section> treeNode;
    /**
     * @param section for which will be build tree
     * @param presenter instance section presenter
     */
    public SectionTreeComponentImpl(Section section, SectionPresenter presenter) {
        this.presenter = presenter;
        
        Executions.createComponents(ZUL_REF, this, null);
        Components.wireVariables(this, this);
        Components.addForwards(this, this);
        
        treeNode = TreeNodeFactory.getTreeNode(section);
        DefaultTreeModel<Section> model = prepareTreeModel(treeNode);
        
        sectionTree.setModel(model);
        sectionTree.setItemRenderer(new SectionBranchTreeitemRenderer(presenter));
    }

    private DefaultTreeModel<Section> prepareTreeModel(DefaultTreeNode<Section> treeNode) {
        List<DefaultTreeNode<Section>> defaultTreeNodes = Collections.singletonList(treeNode);
        DefaultTreeNode<Section> root = new DefaultTreeNode<Section>(null, defaultTreeNodes);
        return new DefaultTreeModel<Section>(root);
    }

    /**
     * {@inheritDoc}
     */
    public BranchSectionVisitable getSelectedObject() {
        if (sectionTree.getSelectedCount() != 0) {
            BranchSectionVisitable data = sectionTree.getSelectedItem().getValue();
            return data;
        }
        
        return null;
    }

    /**
     * event which happen when click on add branch button
     */
    public void onClick$addBranchButton() {
        presenter.openNewBranchDialog(this);
    }

    /**
     * event which happen when double click on section or branch
     */
    public void onDoubleClick$sectionTree() {
        presenter.openEditDialog(this);
    }

    /** {@inheritDoc} */
    public void addBranchToView(Branch branch) {
        // TODO
    }

    /** {@inheritDoc} */
    public void updateBranchInView(Branch branch) {
    }

    /** {@inheritDoc} */
    public void removeBranchFromView(Branch branch) {
    }

    /**
     * {@inheritDoc}
     */
    public void updateSectionInView(Section section) {
        treeNode.setData(section);
    }

    /**
     * Click on '*' button
     */
    public void onClick$moderatorButton() {
        BranchSectionVisitable selectedObject = getSelectedObject();
        selectedObject.apply(openModeratorDialogVisitor);
    }

    private BranchSectionVisitor openModeratorDialogVisitor = new BranchSectionVisitor() {
        @Override
        public void visitSection(Section section) {
            // do nothing because moderators windows is not applicable for sections
        }

        @Override
        public void visitBranch(Branch branch) {
            presenter.openModeratorDialog(branch);
        }
    };

    public void onClick$permissionsButton() throws IOException {
        BranchSectionVisitable selectedObject = getSelectedObject();
        selectedObject.apply(showPermissions);
    }

    private static BranchSectionVisitor showPermissions = new BranchSectionVisitor() {
        @Override
        public void visitSection(Section section) {
            Messagebox.show("This action not provided for section, please select a branch");
        }

        @Override
        public void visitBranch(Branch branch) {
            Executions.sendRedirect("/sections/BranchPermissionManagement.zul?branchId=" + branch.getId());
        }
    };

    

    /**
     * Event which happen when user click on '-' button after it selected
     * section is going to be deleted
     */
    public void onClick$delButton() {
        BranchSectionVisitable selectedObject = getSelectedObject();

        if (selectedObject == null) {
            selectedObject = getFirstElement();
        }

        presenter.openDeleteDialog(selectedObject);
    }

    private BranchSectionVisitable getFirstElement() {
        // if the there no selected object
        // we should consider this event as a try to remove section
        TreeModel<DefaultTreeNode<BranchSectionVisitable>> model = sectionTree.getModel();
        DefaultTreeNode<BranchSectionVisitable> root = model.getRoot();
        TreeNode<BranchSectionVisitable> child = root.getChildAt(0);
        return child.getData();
    }

    /**
     * @param tree
     */
    public void setSectionTree(Tree tree) {
        this.sectionTree = tree;
        
        sectionTree.addEventListener(Events.ON_SELECT, new EventListener<Event>() {
            @Override
            public void onEvent(Event event) throws Exception {
                getSelectedObject().apply(new BranchSectionVisitor() {
                    @Override
                    public void visitSection(Section section) {
                        getBranchPermissionsButton().setDisabled(true);
                    }

                    @Override
                    public void visitBranch(Branch branch) {
                        getBranchPermissionsButton().setDisabled(false);
                    }
                });
            }
        });
        
    }

    private Button getBranchPermissionsButton() {
        return (Button) sectionTree.getFellow("permissionsButton");
    }
}
