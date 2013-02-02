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
package org.jtalks.poulpe.web.controller.users;

import org.jtalks.common.model.entity.Group;
import org.zkoss.bind.annotation.NotifyChange;

import java.text.Collator;
import java.util.Locale;

/**
 * Wires {@link Group} and {@link Boolean} value to show whether user is inside of the group or not. If it's the boolean
 * is false, then user is not in the group.
 *
 * @author Leonid Kazancev
 */
public class GroupBooleanPair implements Comparable<GroupBooleanPair> {
    private static final String LIST_ITEM_CLASS = "listitemClass";
    private static final String CHANGED_LIST_ITEM_CLASS = "changedListItem";
    private static final String NOT_CHANGED_LIST_ITEM_CLASS = "";
    private boolean enable;
    private boolean changed;
    private Group group;

    /**
     * @param group  the group to show whether user is part of it
     * @param enable whether the user is inside of the group or not
     */
    public GroupBooleanPair(Group group, boolean enable) {
        this.enable = enable;
        this.group = group;
    }

    /** @return true if user is in the group */
    public boolean isEnable() {
        return enable;
    }

    /**
     * Says to UI that user is a part of that group or vice versa. Besides this, it keeps track whether this row was
     * changed or not (on UI we mark the row in different color if the row was changed).
     *
     * @param enable false if user now shouldn't be the part of the group
     */
    @NotifyChange(LIST_ITEM_CLASS)
    public void setEnable(boolean enable) {
        if (this.enable != enable) {
            changed = !changed;
        }
        this.enable = enable;
    }

    /**
     * @return whether the state actually has been changed (user was added to the group while it wasn't on the start, or
     *         vice versa - user was removed)
     */
    public boolean isChanged() {
        return changed;
    }

    public Group getGroup() {
        return group;
    }

    /** @return current style class of listitem, marked by color when changed */
    public String getListitemClass() {
        if (changed) {
            return CHANGED_LIST_ITEM_CLASS;
        }
        return NOT_CHANGED_LIST_ITEM_CLASS;
    }

    @Override
    public int compareTo(GroupBooleanPair pair) {
        if (enable != pair.isEnable()) {
            return ((Boolean) pair.isEnable()).compareTo(enable);
        } else {
            Collator russianCollator = Collator.getInstance(new Locale("ru", "RU"));
            return russianCollator.compare(group.getName(), pair.getGroup().getName());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GroupBooleanPair that = (GroupBooleanPair) o;

        if (enable != that.enable) {
            return false;
        }
        Collator russianCollator = Collator.getInstance(new Locale("ru", "RU"));
        if ((russianCollator.compare(group.getName(), that.getGroup().getName())) != 0) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = (enable ? 1 : 0);
        result = 31 * result + (changed ? 1 : 0);
        result = 31 * result + (group != null ? group.hashCode() : 0);
        return result;
    }
}
