package org.jtalks.poulpe.web.controller.group;

import java.util.List;

import org.jtalks.poulpe.model.entity.Group;

public interface GroupView {
    public void updateView(List<Group> groups);
    public void openNewDialog();
    public void openEditDialog(Group groupToEdit);
    public void showConfirmDialog();
}
