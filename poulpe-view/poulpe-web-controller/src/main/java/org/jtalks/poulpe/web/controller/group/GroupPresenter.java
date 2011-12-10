package org.jtalks.poulpe.web.controller.group;

import org.jtalks.poulpe.model.entity.Group;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;

/**
 * 
 * @author Konstantin Akimov
 * 
 */
public class GroupPresenter {

    private GroupViewImpl view;
    private GroupService groupService;
    private DialogManager dialogManager;
    private String searchRestrictions;

    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    public void setDialogManager(DialogManager dialogManager) {
        this.dialogManager = dialogManager;
    }

    public void initView(GroupViewImpl view) {
        this.view = view;        
        updateView(null);
    }

    public void updateView(String restrictions) {
        searchRestrictions = restrictions;
        updateView();

    }

    public void updateView() {
        view.updateView(groupService.getAllMatchedByName(searchRestrictions));
    }

    public void onAddGroup() {
        view.openNewDialog();
    }

    public void onEditGroup(Group groupToEdit) {
        view.openEditDialog(groupToEdit);
    }

    public void doSearch(String pattern) {
        updateView(pattern);
    }

    public void deleteGroup(final Group groupToDelete) {
        dialogManager.confirmDeletion(groupToDelete.getName(),
                new DialogManager.Performable() {

                    @Override
                    public void execute() {
                        groupService.deleteGroup(groupToDelete);
                        updateView();
                    }
                });
        
    }

}
