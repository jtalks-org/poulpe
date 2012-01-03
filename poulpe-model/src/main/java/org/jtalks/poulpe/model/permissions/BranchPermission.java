package org.jtalks.poulpe.model.permissions;

import com.google.common.collect.Lists;
import ru.javatalks.utils.general.Assert;

import javax.annotation.Nonnull;
import java.util.List;


/**
 * These are the restrictions that relate only to branches and sections.
 *
 * @author stanislav bashkirtsev
 */
public enum BranchPermission implements JtalksPermission {
    /**
     * The ability of user group or user to create new topics in the branch.
     */
    CREATE_TOPICS("11", "CREATE_TOPICS"),
    /**
     * The ability of user group or user to view the branch (to see its topics).
     */
    VIEW_TOPICS("110", "VIEW_TOPICS"),
    /**
     * The ability of users to remove their own posts. Some forums prefer to restrict this functionality to avoid
     * misunderstanding between users.
     */
    DELETE_POSTS("111", "DELETE_POSTS");

    private final String name;
    private final int mask;

    /**
     * Constructs the whole object without symbol.
     *
     * @param mask a bit mask that represents the permission, can be negative only for restrictions (look at the class
     *             description). The integer representation of it is saved to the ACL tables of Spring Security.
     * @param name a textual representation of the permission (usually the same as the constant name), though the
     *             restriction usually starts with the 'RESTRICTION_' word
     */
    BranchPermission(int mask, @Nonnull String name) {
        this.mask = mask;
        throwIfNameNotValid(name);
        this.name = name;
    }

    /**
     * Takes a string bit mask.
     *
     * @param mask a bit mask that represents the permission. It's parsed into integer and saved into the ACL tables of
     *             Spring Security.
     * @param name a textual representation of the permission (usually the same as the constant name)
     * @throws NumberFormatException look at {@link Integer#parseInt(String, int)} for details on this as this method is
     *                               used underneath
     * @see BranchPermission#BranchPermission(int, String)
     * @see org.springframework.security.acls.domain.BasePermission
     */
    BranchPermission(@Nonnull String mask, @Nonnull String name) {
        throwIfNameNotValid(name);
        this.mask = Integer.parseInt(mask, 2);
        this.name = name;
    }

    /**
     * Gets the human readable textual representation of the restriction (usually the same as the constant name).
     *
     * @return the human readable textual representation of the restriction (usually the same as the constant name)
     */
    @Override
    public String getName() {
        return name;
    }

    private void throwIfNameNotValid(String name) {
        Assert.throwIfNull(name, "The name can't be null");
    }

    @Override
    public int getMask() {
        return mask;
    }

    @Override
    public String getPattern() {
        return null;
    }

    public static List<BranchPermission> getAllAsList() {
        return Lists.newArrayList(values());
    }

}
