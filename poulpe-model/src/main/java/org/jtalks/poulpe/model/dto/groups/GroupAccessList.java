package org.jtalks.poulpe.model.dto.groups;

import org.jtalks.poulpe.model.entity.Group;

import java.util.List;

/**
 * @author stanislav bashkirtsev
 */
public class GroupAccessList {
    private List<Group> allowed;
    private List<Group> restricted;

    public List<Group> getAllowed() {
        return allowed;
    }

    public GroupAccessList setAllowed(List<Group> allowed) {
        this.allowed = allowed;
        return this;
    }

    public List<Group> getRestricted() {
        return restricted;
    }

    public GroupAccessList setRestricted(List<Group> restricted) {
        this.restricted = restricted;
        return this;
    }
}
