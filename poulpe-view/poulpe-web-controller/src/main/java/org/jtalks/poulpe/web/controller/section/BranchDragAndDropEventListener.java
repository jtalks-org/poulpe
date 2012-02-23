package org.jtalks.poulpe.web.controller.section;

import java.util.List;

import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Section;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.GenericEventListener;
import org.zkoss.zul.Treeitem;

/**
 * Listener for Drag'n'Drop event.
 * 
 * @author Guram Savinov
 */
class BranchDragAndDropEventListener extends GenericEventListener<Event> {

    private static final long serialVersionUID = 1L;
    private final SectionPresenter presenter;

    public BranchDragAndDropEventListener(SectionPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onEvent(Event evt) throws Exception {
        DropEvent dropevent = (DropEvent) evt;
        Treeitem dragged = (Treeitem) dropevent.getDragged().getParent();
        Treeitem target = (Treeitem) dropevent.getTarget().getParent();

        int draggedIndex = dragged.getIndex();
        int targetIndex = target.getIndex();
        if ((targetIndex - 1) == draggedIndex) {
            return;
        } else if (targetIndex > draggedIndex) {
            targetIndex--;
        }

        Section section = dragged.getParentItem().getValue();
        List<Branch> branches = section.getBranches();

        branches.remove(draggedIndex);
        Branch value = dragged.getValue();
        branches.add(targetIndex, value);
        presenter.saveSection(section);

        target.getParent().insertBefore(dragged, target);
    }
}