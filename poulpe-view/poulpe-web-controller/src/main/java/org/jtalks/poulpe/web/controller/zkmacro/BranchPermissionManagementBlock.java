package org.jtalks.poulpe.web.controller.zkmacro;

import org.jtalks.poulpe.service.security.JtalksPermission;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * The page of editing branch permission consists of several blocks, each block represents a single permission and the
 * lists of groups that are granted to that permission (allowed) or restricted to it. The example of such block:
 * <pre>
 *  ____________________________________
 * |Create New Topics in the Branch     |
 * |------------------------------------|
 * |Allowed: Moderators, Activated Users|
 * |Restricted: Banned Users            |
 * |____________________________________|
 * </pre>
 * There can be plenty of such blocks, they all look the same, but the permission, allowed & restricted groups are
 * always different. This class represents a model (data) for such block. Note, that each row of groups is represented
 * with {@link BranchPermissionManagementRow}.
 *
 * @author stanislav bashkirtsev.
 * @see BranchPermissionManagementRow
 * @see <a href="http://jtalks.org/display/jtalks/Managing+Permissions">Permission Management Vision</a>
 * @see <a href="http://jtalks.org/display/jtalks/Permission+Management">Permission Management Architecture</a>
 */
@Immutable
public final class BranchPermissionManagementBlock {
    /**
     * The permission this block represents (the block title on the page). Read class description for detailed
     * information.
     */
    private final JtalksPermission permission;
    private final BranchPermissionManagementRow allowRow;
    private final BranchPermissionManagementRow restrictRow;

    /**
     * Creates a block with permission and without any restricted/allowed groups. Note, that since the class is
     * immutable, you won't be able to set the allowed and restricted rows in the very same instance, for these purposes
     * you'll need to use {@link #setAllowRow(BranchPermissionManagementRow)} & {@link
     * #setRestrictRow(BranchPermissionManagementRow)} which create and return new instances each time. By default,
     * empty rows will be created for both allowed and restricted groups, this is the perfect case for brand new branch
     * where no groups were specified neither as restricted, nor as allowed.
     *
     * @param permission the permission this block represents
     */
    public BranchPermissionManagementBlock(@Nonnull JtalksPermission permission) {
        this(permission, new BranchPermissionManagementRow(), new BranchPermissionManagementRow());
    }

    /**
     * Creates the whole instance with all the required information ready to feed the page.
     *
     * @param permission  the permission this block represents
     * @param allowRow    the set of groups that are granted to the specified {@code permission}
     * @param restrictRow the set of groups that are restricted (not allowed) to use the specified {@code permission}
     */
    public BranchPermissionManagementBlock(@Nonnull JtalksPermission permission,
                                           @Nonnull BranchPermissionManagementRow allowRow,
                                           @Nonnull BranchPermissionManagementRow restrictRow) {
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
    public BranchPermissionManagementBlock setPermission(@Nonnull JtalksPermission permission) {
        return new BranchPermissionManagementBlock(permission, allowRow, restrictRow);
    }

    /**
     * Gets the row that represents the granted groups (those that are allowed to do the action the {@link #permission}
     * is about).
     *
     * @return the row that represents the granted groups
     */
    public BranchPermissionManagementRow getAllowRow() {
        return allowRow;
    }

    /**
     * Sets the row of groups that are granted to the {@link #permission}.
     *
     * @param allowRow the row of groups that are granted to the {@link #permission}
     * @return new instance with new argument specified and others are kept previous
     */
    public BranchPermissionManagementBlock setAllowRow(@Nonnull BranchPermissionManagementRow allowRow) {
        return new BranchPermissionManagementBlock(permission, allowRow, restrictRow);
    }

    /**
     * Gets the row that represents the restricted groups (those that are not allowed to do the action the {@link
     * #permission} is about).
     *
     * @return the row that represents the restricted groups
     */
    public BranchPermissionManagementRow getRestrictRow() {
        return restrictRow;
    }

    /**
     * Sets the row of groups that are restricted to execute the {@link #permission}.
     *
     * @param restrictRow the row of groups that are not allowed to fulfill the {@link #permission}
     * @return new instance with new argument specified and others are kept previous
     */
    public BranchPermissionManagementBlock setRestrictRow(@Nonnull BranchPermissionManagementRow restrictRow) {
        return new BranchPermissionManagementBlock(permission, allowRow, restrictRow);
    }
}
