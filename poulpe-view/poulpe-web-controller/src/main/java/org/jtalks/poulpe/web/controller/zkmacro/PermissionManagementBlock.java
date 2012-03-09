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


import org.jtalks.common.model.permissions.JtalksPermission;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * The page of editing branch permission consists of several blocks, each block represents a single permission and the
 * lists of groups that are granted to that permission (allowed) or restricted to it. The example of such block:
 * <pre>
 *  _____________________________________
 * |Create New Topics in the PoulpeBranch|
 * |-------------------------------------|
 * |Allowed: Moderators, Activated Users |
 * |Restricted: Banned Users             |
 * |_____________________________________|
 * </pre>
 * There can be plenty of such blocks, they all look the same, but the permission, allowed & restricted groups are
 * always different. This class represents a model (data) for such block. Note, that each row of groups is represented
 * with {@link PermissionRow}.
 *
 * @author stanislav bashkirtsev.
 * @author Vyacheslav Zhivaev
 * @see PermissionRow
 * @see <a href="http://jtalks.org/display/jtalks/Managing+Permissions">Permission Management Vision</a>
 * @see <a href="http://jtalks.org/display/jtalks/Permission+Management">Permission Management Architecture</a>
 */
@Immutable
public final class PermissionManagementBlock {
    /**
     * The permission this block represents (the block title on the page). Read class description for detailed
     * information.
     */
    private final JtalksPermission permission;
    private final PermissionRow allowRow;
    private final PermissionRow restrictRow;

    /**
     * Creates a block with permission and without any restricted/allowed groups. Note, that since the class is
     * immutable, you won't be able to set the allowed and restricted rows in the very same instance, for these purposes
     * you'll need to use {@link #addAllowRow(PermissionRow)} & {@link
     * #addRestrictRow(PermissionRow)} which create and return new instances each time. By default,
     * empty rows will be created for both allowed and restricted groups, this is the perfect case for brand new branch
     * where no groups were specified neither as restricted, nor as allowed.
     *
     * @param permission the permission this block represents
     */
    public PermissionManagementBlock(@Nonnull JtalksPermission permission) {
        this(permission, PermissionRow.newAllowRow(), PermissionRow.newRestrictRow());
    }

    /**
     * Creates the whole instance with all the required information ready to feed the page.
     *
     * @param permission  the permission this block represents
     * @param allowRow    the set of groups that are granted to the specified {@code permission}
     * @param restrictRow the set of groups that are restricted (not allowed) to use the specified {@code permission}
     */
    public PermissionManagementBlock(@Nonnull JtalksPermission permission,
                                           @Nonnull PermissionRow allowRow,
                                           @Nonnull PermissionRow restrictRow) {
        this.permission = permission;
        this.allowRow = allowRow;
        this.restrictRow = restrictRow;
    }
    
    /**
     * Gets the permission this block is all about.
     *
     * @return the permission this block is all about
     */
    public JtalksPermission getPermission() {
        return permission;
    }

    /**
     * Replaces the originally specified permission with the new instance.
     *
     * @param permission the new permission this block should refer to
     * @return new instance with new permission specified and others are kept previous
     */
    public PermissionManagementBlock addPermission(@Nonnull JtalksPermission permission) {
        return new PermissionManagementBlock(permission, allowRow, restrictRow);
    }

    /**
     * Gets the row that represents the granted groups (those that are allowed to do the action the {@link #permission}
     * is about).
     *
     * @return the row that represents the granted groups
     */
    public PermissionRow getAllowRow() {
        return allowRow;
    }

    /**
     * Adds the row of groups that are granted to the {@link #permission}.
     *
     * @param allowRow the row of groups that are granted to the {@link #permission}
     * @return new instance with new argument specified and others are kept previous
     */
    public PermissionManagementBlock addAllowRow(@Nonnull PermissionRow allowRow) {
        return new PermissionManagementBlock(permission, allowRow, restrictRow);
    }

    /**
     * Gets the row that represents the restricted groups (those that are not allowed to do the action the {@link
     * #permission} is about).
     *
     * @return the row that represents the restricted groups
     */
    public PermissionRow getRestrictRow() {
        return restrictRow;
    }

    /**
     * Adds the row of groups that are restricted to execute the {@link #permission}.
     *
     * @param restrictRow the row of groups that are not allowed to fulfill the {@link #permission}
     * @return new instance with new argument specified and others are kept previous
     */
    public PermissionManagementBlock addRestrictRow(@Nonnull PermissionRow restrictRow) {
        return new PermissionManagementBlock(permission, allowRow, restrictRow);
    }
}
