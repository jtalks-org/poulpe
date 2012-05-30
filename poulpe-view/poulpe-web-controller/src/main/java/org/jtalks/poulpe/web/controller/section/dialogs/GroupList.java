package org.jtalks.poulpe.web.controller.section.dialogs;

import org.jtalks.common.model.entity.Group;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * A list of groups with useful methods that are absent in usual list.
 *
 * @author stanislav bashkirtsev
 */
public class GroupList {
    private final List<Group> groups = new ArrayList<Group>();

    public GroupList setGroups(List<Group> groups) {
        this.groups.clear();
        this.groups.addAll(groups);
        return this;
    }

    public List<Group> getGroups() {
        return groups;
    }

    /**
     * Finds and returns a group that is equal or the same in the list.
     *
     * @param group a group to find its equal counterpart
     * @return a group in the list that is equal to the specified one or {@code null} if such group wasn't found
     */
    public Group getEqual(@Nullable Group group) {
        int groupIndex = groups.indexOf(group);
        if (groupIndex >= 0) {
            return groups.get(groupIndex);
        }
        return null;
    }
}
