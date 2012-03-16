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