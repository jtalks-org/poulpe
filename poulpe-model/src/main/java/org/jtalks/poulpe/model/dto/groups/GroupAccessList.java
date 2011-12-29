package org.jtalks.poulpe.model.dto.groups;

import org.jtalks.poulpe.model.entity.Group;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author stanislav bashkirtsev
 */
public class GroupAccessList {
    private final List<Group> allowed = new ArrayList<Group>();
    private final List<Group> restricted = new ArrayList<Group>();

    public List<Group> getAllowed() {
        return allowed;
    }

    public GroupAccessList addAllowed(@Nullable Group group) {
        if (group != null) {
            allowed.add(group);
        }
        return this;
    }

    public GroupAccessList addRestricted(Group group) {
        if (group != null) {
            restricted.add(group);
        }
        return this;
    }

    public GroupAccessList setAllowed(List<Group> allowed) {
        this.allowed.clear();
        this.allowed.addAll(allowed);
        return this;
    }

    public List<Group> getRestricted() {
        return restricted;
    }

    public GroupAccessList setRestricted(List<Group> restricted) {
        this.restricted.clear();
        this.restricted.addAll(restricted);
        return this;
    }

}
