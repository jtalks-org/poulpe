package org.jtalks.poulpe.web.controller.section;

import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.BranchSectionVisitor;
import org.jtalks.poulpe.model.entity.PoulpeSection;
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
    public void visitSection(PoulpeSection section) {
        treeRow.appendChild(new Treecell(section.getName()));
    }

    @Override
    public void visitBranch(PoulpeBranch branch) {
        treeRow.appendChild(new Treecell(branch.getName()));
        treeRow.setDraggable("true");
        treeRow.setDroppable("true");
        
        treeRow.addEventListener("onDrop", new DragAndDropEventListener(presenter));
    }
}