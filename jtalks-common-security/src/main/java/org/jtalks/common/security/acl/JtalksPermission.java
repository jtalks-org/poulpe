package org.jtalks.common.security.acl;

import org.springframework.security.acls.domain.BasePermission;
import ru.javatalks.utils.general.Assert;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

/**
 * This is an extension of basic Spring Security ACL permissions, it adds more available permission constants. Also it
 * gives an opportunity to get the reverted permissions (restrictions). The idea is that since we have 4 bytes for
 * integer (32 bits), we can use 31 of them for usual permissions, but if we want to get a <i>restriction</i> for some
 * action, we can simply revert all the bits and save that value into database (thus will have a negative integer in the
 * database since the 32nd bit was changed). This allows us to have 3 states: Allowed (usual permission), Restricted
 * (with reverted bit mask) and Neutral (no record in the ACL table). Also restriction has the same name as permission,
 * but with additional prefix 'RESTRICTION_' at the beginning. <br/> So an example:<br/> {@code Permission
 * (01111000001111111111111111110010, CREATE_TOPIC)}<br/> <code> Restriction(10000111110000000000000000001101,
 * RESTRICT_CREATE_TOPIC)</code>.</br>
 *
 * @author stanislav bashkirtsev
 * @see <a href=http://jtalks.org/display/jtalks/Permission+Management>JTalks Permission Management Architecture</a>
 * @see <a href=http://jtalks.org/display/jtalks/Managing+Permissions>JTalks Permission Management Vision</a>
 */
@ThreadSafe
public class JtalksPermission extends BasePermission {
    /**
     * This prefix is added to the permission names when the are to be reverted.
     */
    private final static String RESTRICTED_PREFIX = "RESTRICTED_";
    private final String name;

    /**
     * Constructs the whole object without symbol.
     *
     * @param mask a bit mask that represents the permission, can be negative only for restrictions (look at the class
     *             description). The integer representation of it is saved to the ACL tables of Spring Security.
     * @param name a textual representation of the permission (usually the same as the constant name), though the
     *             restriction usually starts with the 'RESTRICTION_' word
     */
    protected JtalksPermission(int mask, @Nonnull String name) {
        super(mask);
        throwIfNameNotValid(name);
        this.name = name;
    }

    /**
     * Takes a string bit mask.
     *
     * @param mask a bit mask that represents the permission. It's parsed into integer and saved into the ACL tables of
     *             Spring Security.
     * @param name a textual representation of the permission (usually the same as the constant name), though the
     *             restriction usually starts with the 'RESTRICTION_' word
     * @throws NumberFormatException look at {@link Integer#parseInt(String, int)} for details on this as this method is
     *                               used underneath
     * @see JtalksPermission#JtalksPermission(int, String)
     * @see BasePermission
     */
    protected JtalksPermission(@Nonnull String mask, @Nonnull String name) {
        super(Integer.parseInt(mask, 2));
        throwIfNameNotValid(name);
        this.name = name;
    }

    /**
     * Creates an opposite permission to the current one (which is actually a Restriction if it was a Permission and
     * vice versa). There is rather straightforward algorithm how you can convert between each other: we take a usual
     * {@link JtalksPermission} and invert its bit mask so that all the {@code 0} are replaced with {@code 1} and vice
     * versa. This leads to all Permissions to be positive numbers and Restrictions - negative ones, if we transform the
     * bit mask into the integer, the permission will be N and restriction will be -N-1, e.g. 10 and -11 respectively.
     * <br/> The name of the Restriction is the same as the originating Permission, but it has 'RESTRICTION_' as a
     * prefix (if it is a Restriction and already has this prefix, then it will be removed).
     *
     * @return a permission that is opposite to the current one, it has its bit mask inverted and also its name starts
     *         with 'RESTRICTED_' prefix if it was a permission without this prefix
     */
    public JtalksPermission getInverted() {
        return new JtalksPermission(~super.getMask(), getInvertedName(getName()));
    }

    /**
     * Gets the human readable textual representation of the restriction (usually the same as the constant name).
     *
     * @return the human readable textual representation of the restriction (usually the same as the constant name)
     */
    public String getName() {
        return name;
    }

    private void throwIfNameNotValid(String name) {
        Assert.throwIfNull(name, "The name can't be null");
    }

    /**
     * If the permission name was specified, then {@link #RESTRICTED_PREFIX} will be added to the start of the string,
     * if restriction name was specified (and it already has the prefix), then prefix will be removed.
     *
     * @param name a name of permission or restriction to invert it
     * @return a new name with {@link #RESTRICTED_PREFIX} to be added or removed depending on whether it was in the
     *         specified parameter
     */
    private String getInvertedName(@Nonnull String name) {
        if (name.startsWith(RESTRICTED_PREFIX)) {
            return name.replaceFirst(RESTRICTED_PREFIX, "");
        }
        return RESTRICTED_PREFIX + name;
    }
}
