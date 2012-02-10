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

import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.BranchSectionVisitor;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treerow;

/**
 * For a {@link Treerow} appends an element based on visitable type. If it's a
 * {@link PoulpeBranch}, then it adds it making it draggable and droppable. If it's a
 * {@link PoulpeSection}, it just adds it.
 * 
 * @author Alexey Grigorev
 */
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
        treeRow.setDraggable("section");
        treeRow.setDroppable("section");
        
        treeRow.addEventListener("onDrop", new SectionDragAndDropEventListener(presenter));
    }

    @Override
    public void visitBranch(PoulpeBranch branch) {
        treeRow.appendChild(new Treecell(branch.getName()));
        treeRow.setDraggable("branch");
        treeRow.setDroppable("branch");
        
        treeRow.addEventListener("onDrop", new BranchDragAndDropEventListener(presenter));
    }
}
