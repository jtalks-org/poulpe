package org.jtalks.poulpe.web.controller.section;

import java.util.ArrayList;
import java.util.List;

import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.BranchSectionVisitable;
import org.jtalks.poulpe.model.entity.BranchSectionVisitor;
import org.jtalks.poulpe.model.entity.Section;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;


class SectionBranchTreeitemRenderer implements TreeitemRenderer<ExtendedTreeNode<BranchSectionVisitable>> {

    private final SectionPresenter presenter;

    public SectionBranchTreeitemRenderer(SectionPresenter presenter) {
        this.presenter = presenter;
    }
    
    @Override
    public void render(Treeitem treeItem, ExtendedTreeNode<BranchSectionVisitable> node) throws Exception {
        final BranchSectionVisitable data = node.getData();
        
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

    private List<Treecell> findTreeCells(final Treeitem treeItem) {
        List<Treecell> treecells = new ArrayList<Treecell>();
        
        for (Component cmp : treeItem.getChildren()) {
            if (cmp instanceof Treerow) {
                for (Object oCell : ((Treerow) cmp).getChildren()) {
                    if (oCell instanceof Treecell) {
                        treecells.add((Treecell) oCell);
                    }
                }
            }
        }
        return treecells;
    }
    
    private class TreeSellTitleSetterVisitor implements BranchSectionVisitor {
        private final Treecell treecell;

        public TreeSellTitleSetterVisitor(Treecell treecell) {
            this.treecell = treecell;
        }
        
        @Override
        public void visitSection(Section section) {
            treecell.setLabel(section.getName());
        }

        @Override
        public void visitBranch(Branch branch) {
            treecell.setLabel(branch.getName());
        }
    }
}
