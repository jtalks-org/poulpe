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

import java.util.ArrayList;
import java.util.List;

import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.BranchSectionVisitable;
import org.jtalks.poulpe.model.entity.BranchSectionVisitor;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

/**
 * Class for rendering tree items in {@link ZkSectionView}. Renders sections and
 * branches
 * 
 * @author unascribed
 * @author Alexey Grigorev
 */
class SectionBranchTreeitemRenderer implements TreeitemRenderer<ExtendedTreeNode<BranchSectionVisitable>> {

    private final SectionPresenter presenter;

    /**
     * @param presenter which will be used for handling drag-n-drop events
     */
    public SectionBranchTreeitemRenderer(SectionPresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Renders sections and branches
     */
    @Override
    public void render(Treeitem treeItem, ExtendedTreeNode<BranchSectionVisitable> node, int index) throws Exception {
        BranchSectionVisitable data = node.getData();

        treeItem.setOpen(node.isExpanded());
        treeItem.setValue(data);

        List<Treecell> treecells = findTreeCells(treeItem);

        if (treecells.isEmpty()) {
            Treerow treeRow = new Treerow();
            data.apply(new TreeRowAppenderBranchSectionVisitor(treeRow, presenter));
            treeItem.appendChild(treeRow);
        } else {
            for (Treecell treecell : treecells) {
                data.apply(new TreeSellTitleSetterVisitor(treecell));
            }
        }        
    }
    
    /**
     * For each {@link Treerow} in the tree it looks for {@link Treecell}s - and returns
     * all of them, if any found 
     *  
     * @param treeItem in which {@link Treecell}s are looked
     * @return list of found {@link Treecell}s 
     */
    private List<Treecell> findTreeCells(final Treeitem treeItem) {
        List<Treecell> treecells = new ArrayList<Treecell>();

        for (Component cmp : treeItem.getChildren()) {
            if (cmp instanceof Treerow) {
                for (Object oCell : cmp.getChildren()) {
                    if (oCell instanceof Treecell) {
                        treecells.add((Treecell) oCell);
                    }
                }
            }
        }
        return treecells;
    }

    /**
     * Sets label to {@link Treecell} from an entity's title
     * 
     * @author Alexey Grigorev
     */
    private class TreeSellTitleSetterVisitor implements BranchSectionVisitor {
        private final Treecell treecell;

        public TreeSellTitleSetterVisitor(Treecell treecell) {
            this.treecell = treecell;
        }

        @Override
        public void visitSection(PoulpeSection section) {
            treecell.setLabel(section.getName());
        }

        @Override
        public void visitBranch(PoulpeBranch branch) {
            treecell.setLabel(branch.getName());
        }
    }

}
