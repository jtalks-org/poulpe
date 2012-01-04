/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jtalks.poulpe.model.dto.branches;

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
