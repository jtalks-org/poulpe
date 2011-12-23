package org.jtalks.poulpe.web.controller.branch;

import org.jtalks.poulpe.model.entity.Group;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author stanislav bashkirtsev
 */
public class ManageUserGroupsDialogVm {
    private int[] selectedIndex;
    private final List<Group> availableGroups = new ArrayList<Group>();
    private final BindingListModelList availableListModel = new BindingListModelList(availableGroups, true);
    private final List<Group> addedGroups = new ArrayList<Group>();
    private final BindingListModelList addedListModel = new BindingListModelList(addedGroups, true);
    private final List<Group> movedToAddedGroups = new ArrayList<Group>();
    private final List<Group> movedFromAddedGroups = new ArrayList<Group>();

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

    public int[] getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int[] selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public ListModel getAvailableGroups() {
        return availableListModel;
    }

    public ListModel getAddedGroups() {
        return addedListModel;
    }

    public void moveFromAvailableToAdded(int... indexes) {
        List<Group> moved = new LinkedList<Group>();
        for(int i = 0; i < indexes.length; i++) {
             moved.add((Group) availableListModel.get(indexes[i] - i));
            availableListModel.remove(indexes[i] - i);
        }
        addedListModel.addAll(moved);
    }

}
