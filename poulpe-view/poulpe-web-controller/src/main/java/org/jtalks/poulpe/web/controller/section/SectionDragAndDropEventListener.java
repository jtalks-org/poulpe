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

import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.GenericEventListener;
import org.zkoss.zul.Treeitem;

/**
 * Listener for Drag'n'Drop event.
 * 
 * @author Guram Savinov
 */
class SectionDragAndDropEventListener extends GenericEventListener<Event> {

    private static final long serialVersionUID = 1L;
    private final SectionPresenter presenter;

    public SectionDragAndDropEventListener(SectionPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onEvent(Event evt) throws Exception {
        DropEvent dropevent = (DropEvent) evt;
        Treeitem draggedItem = (Treeitem) dropevent.getDragged().getParent();
        Treeitem targetItem = (Treeitem) dropevent.getTarget().getParent();
        PoulpeSection draggedSection = (PoulpeSection) draggedItem.getValue();
        PoulpeSection targetSection = (PoulpeSection) targetItem.getValue();
        int draggedPosition = draggedSection.getPosition();
        int targetPosition = targetSection.getPosition();

        draggedSection.setPosition(targetPosition);
        targetSection.setPosition(draggedPosition);
        
        presenter.saveSection(draggedSection);
        presenter.saveSection(targetSection);
        presenter.updateView();
    }
}