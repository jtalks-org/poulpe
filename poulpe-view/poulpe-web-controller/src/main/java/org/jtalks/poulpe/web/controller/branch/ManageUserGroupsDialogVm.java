package org.jtalks.poulpe.web.controller.branch;

import org.jtalks.poulpe.model.entity.Group;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author stanislav bashkirtsev
 */
public class ManageUserGroupsDialogVm {
    private final List<Group> availableGroups = new ArrayList<Group>();
    private final List<Group> addedGroups = new ArrayList<Group>();

    public ManageUserGroupsDialogVm setAvailableGroups(List<Group> availableGroups) {
        this.availableGroups.clear();
        this.availableGroups.addAll(availableGroups);
        return this;
    }

    public ManageUserGroupsDialogVm setAddedGroups(List<Group> addedGroups) {
        this.addedGroups.clear();
        this.addedGroups.addAll(addedGroups);
        return this;
    }

    public ListModel getAvailableGroups() {
        return new BindingListModelList(availableGroups, true);
    }

    public ListModel getAddedGroups() {
        return new BindingListModelList(addedGroups, true);
    }
}
