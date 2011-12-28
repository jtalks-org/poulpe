package org.jtalks.poulpe.service.security;

import org.jtalks.common.security.acl.JtalksPermission;

import javax.annotation.Nonnull;

/**
 * These are the restrictions that relate only to branches and sections.
 *
 * @author stanislav bashkirtsev
 */
public class BranchPermission extends JtalksPermission {
    /**
     * The ability of user group or user to create new topics in the branch.
     */
    public static final JtalksPermission CREATE_TOPICS = new BranchPermission("11", "CREATE_TOPICS");
    /**
     * The ability of user group or user to view the branch (to see its topics).
     */
    public static final JtalksPermission VIEW_TOPICS = new BranchPermission("110", "VIEW_TOPICS");

    protected BranchPermission(int mask, @Nonnull String name) {
        super(mask, name);
    }

    /**
     * {@inheritDoc}
     */
    protected BranchPermission(@Nonnull String mask, @Nonnull String name)  {
        super(mask, name);
    }
}
