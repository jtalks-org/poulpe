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
 * Wires {@link Group} and {@link Boolean} by enable selected status for group, allows to make interface with
 * list of groups wired with checkboxes.
 * @author Leonid Kazancev
 */
public class GroupBooleanPair implements Comparable<GroupBooleanPair> {
    public static final String LIST_ITEM_CLASS = "listitemClass";
    public static final String CHANGED_LIST_ITEM_CLASS = "changedListItem";
    public static final String NOT_CHANGED_LIST_ITEM_CLASS = "";
    private final Group group;
    private boolean enable;
    private boolean changed;

    /**
     * Construction method.
     * @param group to hold
     * @param enable status of group
     */
    public GroupBooleanPair(Group group, boolean enable) {
        this.enable = enable;
        this.group = group;
    }

    /**
     * @return enable status
     */
    public boolean isEnable() {
        return enable;
    }

    /**
     * Sets group enable status, also keep actual state of changed status by changing it on enable change.
     * @param enable status to set
     */
    @NotifyChange(LIST_ITEM_CLASS)
    public void setEnable(boolean enable) {
        if (this.enable != enable) {
            changed = !changed;
        }
        this.enable = enable;
    }

    /**
     * @return changed status(holds group color)
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * @return {@link Group}
     */
    public Group getGroup() {
        return group;
    }

    /**
     * @return current style class of listitem, marked by color when changed
     */
    public String getListitemClass() {
        if(changed){
            return CHANGED_LIST_ITEM_CLASS;
        }
        return NOT_CHANGED_LIST_ITEM_CLASS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(GroupBooleanPair pair) {
        if (enable != pair.isEnable()) {
            return ((Boolean)pair.isEnable()).compareTo(enable);
        } else {
            Collator russianCollator = Collator.getInstance(new Locale("ru", "RU"));
            return russianCollator.compare(group.getName(), pair.getGroup().getName());
        }
    }

    /**
     * {@inheritDoc}
     */
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
        //TODO Implement multilang solution of equals.
        Collator russianCollator = Collator.getInstance(new Locale("ru", "RU"));
        if ((russianCollator.compare(group.getName(), that.getGroup().getName())) != 0) {
            return false;
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = (enable ? 1 : 0);
        int constant = 31;
        result = constant * result + (changed ? 1 : 0);
        result = constant * result + (group != null ? group.hashCode() : 0);
        return result;
    }
}
