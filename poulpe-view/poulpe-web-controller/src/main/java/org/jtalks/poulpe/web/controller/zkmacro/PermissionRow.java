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
package org.jtalks.poulpe.web.controller.zkmacro;

import org.jtalks.common.model.entity.Group;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents a list of permissions on such pages as Component Permissions and Branch Permissions. You can
 * combine several rows your own aims in order to show e.g. restricted permissions and granted ones for a given object.
 * Essentially this is just a list of groups, but since all our permission management is based on groups, this is all we
 * need in such case.
 *
 * @author stanislav bashkirtsev
 * @author Vyacheslav Zhivaev
 */
public class PermissionRow {
    public static final String ALLOW_DEFAULT_LABEL = "Allow";
    public static final String RESTRICT_DEFAULT_LABEL = "Restrict";
    private final String rowLabel;
    private final List<Group> groups;

    /**
     * Creates the new Permission Row object
     * @param rowLabel label for the row
     * @param groups list of groups in the row
     */
    protected PermissionRow(String rowLabel, List<Group> groups) {
        this.groups = groups;
        this.rowLabel = rowLabel;
    }

    /**
     * Gets the groups that relate to the permission (either granted or restricted or anything else).
     *
     * @return the groups that relate to the permission (either granted or restricted or anything else)
     */
    public List<Group> getGroups() {
        return groups;
    }

    /**
     * Gets the string label that should be shown on the page near the list of groups. E.g. this label can state:
     * <i>Granted: </i> or <i>Restricted:</i>.
     *
     * @return string label that should be shown on the page near the list of groups
     */
    public String getRowLabel() {
        return rowLabel;
    }

    /**
     * Adds a new group to the list of groups.
     *
     * @param group a new group to be added to the list of groups shown on the page
     * @return this
     */
    public PermissionRow addGroup(@Nonnull Group group) {
        groups.add(group);
        return this;
    }

    /**
     * Creates a new row with empty list of groups and with label equal to {@link #ALLOW_DEFAULT_LABEL}.
     *
     * @return a new row with empty list of groups and with label equal to {@link #ALLOW_DEFAULT_LABEL}
     */
    public static PermissionRow newAllowRow() {
        return newAllowRow(new ArrayList<Group>());
    }


    /**
     * Creates a new row with empty list of groups and with label equal to {@link #RESTRICT_DEFAULT_LABEL}.
     *
     * @return a new row with empty list of groups and with label equal to {@link #RESTRICT_DEFAULT_LABEL}
     */
    public static PermissionRow newRestrictRow() {
        return newRestrictRow(new ArrayList<Group>());
    }

    /**
     * Creates a new row with specified list of groups and with label equal to {@link #ALLOW_DEFAULT_LABEL}. This
     * factory method ideally shouldn't be used in preference to {@link #newAllowRow(String, java.util.List)} so that
     * you can specified the i18n label instead of hardcoded one.
     *
     * @param groups the groups that should be in the allow row
     * @return a new row with specified list of groups and with label equal to {@link #ALLOW_DEFAULT_LABEL}
     */
    public static PermissionRow newAllowRow(@Nonnull List<Group> groups) {
        return newAllowRow(ALLOW_DEFAULT_LABEL, groups);
    }

    /**
     * Creates a new row with specified list of groups and with label equal to {@link #RESTRICT_DEFAULT_LABEL}. This
     * factory method ideally shouldn't be used in preference to {@link #newRestrictRow(String, java.util.List)} so that
     * you can specified the i18n label instead of hardcoded one.
     *
     * @param groups the groups that should be in the 'restricted' row
     * @return a new row with specified list of groups and with label equal to {@link #RESTRICT_DEFAULT_LABEL}
     */
    public static PermissionRow newRestrictRow(@Nonnull List<Group> groups) {
        return newRestrictRow(RESTRICT_DEFAULT_LABEL, groups);
    }

    /**
     * Creates a new row with specified list of groups and specified label (which ideally should be a i18n label)
     *
     * @param rowLabel the label of the allow row, ideally should be an i18n label
     * @param groups   the groups that should be in the allow row
     * @return a new row with specified list of groups and with label equal to {@link #ALLOW_DEFAULT_LABEL}
     */
    public static PermissionRow newAllowRow(@Nonnull String rowLabel, @Nonnull List<Group> groups) {
        return new PermissionRow(rowLabel, groups);
    }

    /**
     * Creates a new row with specified list of groups and specified label (which ideally should be a i18n label)
     *
     * @param rowLabel the label of the restrict row, ideally should be an i18n label
     * @param groups   the groups that should be in the restrict row
     * @return a new row with specified list of groups and label
     */
    public static PermissionRow newRestrictRow(@Nonnull String rowLabel, @Nonnull List<Group> groups) {
        return new PermissionRow(rowLabel, groups);
    }

}
