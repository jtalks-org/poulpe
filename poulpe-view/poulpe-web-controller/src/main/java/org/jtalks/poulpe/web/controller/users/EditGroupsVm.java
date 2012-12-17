package org.jtalks.poulpe.web.controller.users;

import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.entity.User;
import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.web.controller.section.dialogs.AbstractDialogVm;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Leonid Kazancev
 */
public class EditGroupsVm extends AbstractDialogVm {
    private boolean chosen = true;
    private boolean notChosen = true;
    protected static final String GROUPS = "groupsToShow";

    private List<GroupBooleanPair> groupsToShow;
    private List<GroupBooleanPair> groups;

    /**
     * User to be edited
     */
    private PoulpeUser userToEdit;
    private final UserService userService;
    private final GroupService groupService;
    private final UsersVm usersVm;

    /**
     * @param usersVm
     * @param userService
     * @throws NotFoundException
     */
    public EditGroupsVm(@Nonnull UsersVm usersVm, @Nonnull UserService userService, @Nonnull GroupService groupService) {
        this.userService = userService;
        this.groupService = groupService;
        this.usersVm = usersVm;
    }


    /**
     * Dialog opening command.
     */
    @GlobalCommand("editGroups")
    @NotifyChange({SHOW_DIALOG, GROUPS})
    public void editGroups() throws NotFoundException {
        userToEdit = usersVm.getSelectedUser();
        init();
        showDialog();
    }

    @GlobalCommand("filterGroups")
    @NotifyChange(GROUPS)
    public void filterGroups() {
        doFilter();
    }

    private void init() {
        List<Group> allGroups = groupService.getAll();
        List<Group> userGroupList = userToEdit.getGroups();
        groups = new ArrayList<GroupBooleanPair>(allGroups.size());

        for (Group group : allGroups) {
            boolean isMember = userGroupList.contains(group);
            GroupBooleanPair pair = new GroupBooleanPair(group, isMember);
            groups.add(pair);
        }
        groupsToShow = new ArrayList<GroupBooleanPair>(groups);
        Collections.sort(groupsToShow);
    }

    private void doFilter() {
        groups.removeAll(groupsToShow);
        groups.addAll(groupsToShow);
        groupsToShow = new ArrayList<GroupBooleanPair>(groups.size());
        for (GroupBooleanPair group : groups) {
            boolean isMember = group.isEnable();
            if (chosen && isMember == chosen || notChosen && !isMember == notChosen) {
                groupsToShow.add(group);
            }
        }
        Collections.sort(groupsToShow);
    }

    @GlobalCommand
    @NotifyChange(GROUPS)
    public void colorChange() {
    }

    /**
     * Save changes command.
     */
    @GlobalCommand("saveUserGroupsChanges")
    @NotifyChange({SHOW_DIALOG})
    public void saveChanges() {
        List<Group> userGroups = userToEdit.getGroups();
        boolean changed = false;
        for (GroupBooleanPair group : groups) {
            Group currentGroup = group.getGroup();
            if (userGroups.contains(currentGroup) && !group.isEnable()) {
                //remove group from user
                userGroups.remove(group.getGroup());
                currentGroup.getUsers().remove(userToEdit);
                groupService.saveGroup(currentGroup);
                changed = true;
            }
            if (!userGroups.contains(currentGroup) && group.isEnable()) {
                //add group to user
                userGroups.add(group.getGroup());
                currentGroup.getUsers().add(userToEdit);
                groupService.saveGroup(currentGroup);
                changed = true;
            }

        }
        if (changed) {
            userService.updateUser(userToEdit);
        }
        closeDialog();
    }

    private void closeDialog() {
        isShowDialog();
    }


    public boolean isChosen() {
        return chosen;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }

    public boolean isNotChosen() {
        return notChosen;
    }

    public void setNotChosen(boolean notChosen) {
        this.notChosen = notChosen;
    }

    public List<GroupBooleanPair> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupBooleanPair> groups) {
        this.groups = groups;
    }

    public User getUserToEdit() {
        return userToEdit;
    }

    public List<GroupBooleanPair> getGroupsToShow() {
        return groupsToShow;
    }

    public void setGroupsToShow(List<GroupBooleanPair> groupsToShow) {
        this.groupsToShow = groupsToShow;
    }
}
