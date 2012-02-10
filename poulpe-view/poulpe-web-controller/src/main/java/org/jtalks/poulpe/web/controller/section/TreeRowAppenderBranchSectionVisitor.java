package org.jtalks.poulpe.web.controller.section;

import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.BranchSectionVisitor;
import org.jtalks.poulpe.model.entity.Section;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treerow;

class TreeRowAppenderBranchSectionVisitor implements BranchSectionVisitor {
    
    private final Treerow treeRow;
    private final SectionPresenter presenter;

    public TreeRowAppenderBranchSectionVisitor(Treerow treeRow, SectionPresenter presenter) {
        this.treeRow = treeRow;
        this.presenter = presenter;
    }
    
    @Override
    public void visitSection(Section section) {
        treeRow.appendChild(new Treecell(section.getName()));
        treeRow.setDraggable("section");
        treeRow.setDroppable("section");
        
        treeRow.addEventListener("onDrop", new SectionDragAndDropEventListener(presenter));
    }

    @Override
    public void visitBranch(Branch branch) {
        treeRow.appendChild(new Treecell(branch.getName()));
        treeRow.setDraggable("branch");
        treeRow.setDroppable("branch");
        
        treeRow.addEventListener("onDrop", new BranchDragAndDropEventListener(presenter));
    }
}