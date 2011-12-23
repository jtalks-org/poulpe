package org.jtalks.poulpe.web.controller.branch;

import org.jtalks.poulpe.model.entity.Group;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listitem;

import java.util.*;

/**
 * @author stanislav bashkirtsev
 */
public class ManageUserGroupsDialogVm {
    private final BindingListModelList availableListModel = new BindingListModelList(new ArrayList(), false);
    private final BindingListModelList addedListModel = new BindingListModelList(new ArrayList(), false);
    private final List<Group> movedToAddedGroups = new ArrayList<Group>();
    private final List<Group> movedFromAddedGroups = new ArrayList<Group>();

    public ManageUserGroupsDialogVm setAvailableGroups(List<Group> availableGroups) {
        this.availableListModel.clear();
        this.availableListModel.addAll(availableGroups);
        return this;
    }

    public ManageUserGroupsDialogVm setAddedGroups(List<Group> addedGroups) {
        this.addedListModel.clear();
        this.addedListModel.addAll(addedGroups);
        return this;
    }

    public ListModel getAvailableGroups() {
        return availableListModel;
    }

    public ListModel getAddedGroups() {
        return addedListModel;
    }

    public Set<Group> moveFromAvailableToAdded() {
        Set<Group> selection = new HashSet<Group> (availableListModel.getSelection());
        for(Group nextSelectedItem: selection){
            addedListModel.add(nextSelectedItem);
            availableListModel.remove(nextSelectedItem);
        }
        return selection;
    }

}
