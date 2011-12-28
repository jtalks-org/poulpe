package org.jtalks.common.security.acl;

import com.google.common.annotations.VisibleForTesting;
import org.jtalks.poulpe.model.entity.Group;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.regex.Pattern;

/**
 * This class does the same as {@link org.springframework.security.acls.domain.PrincipalSid} does for users. More
 * precisely it contains some security ID that is used by Spring ACL to associate the owner to the permissions it owns.
 * Thus this class contains an ID that will be saved into database ({@code acl_sids#sid, acl_object_identity#owner_sid,
 * acl_entry#sid}).
 *
 * @author stanislav bashkirstev
 */
public class UserGroupSid implements IdentifiableSid {
    public final static String SID_PREFIX = "usergroup";
    private final String groupId;

    public UserGroupSid(String sidId) {
        this.groupId = parseGroupId(sidId);
    }

    /**
     * Constructs SID by the group id.
     *
     * @param groupId the id of the group that will own permissions for some actions on some objects
     * @see Group#getId()
     */
    public UserGroupSid(@Nonnegative long groupId) {
        this.groupId = String.valueOf(groupId);
    }

    /**
     * Constructs a SID by retrieving the group id from the {@link Group} object.
     *
     * @param group a group to take its database id
     * @see Group#getId()
     */
    public UserGroupSid(@Nonnull Group group) {
        this.groupId = String.valueOf(group.getId());
    }

    private String parseGroupId(String sidId) {
        String[] splitted = sidId.split(Pattern.quote(":"));
        if (splitted.length != 2) {
            throw new WrongFormatException(sidId);
        }
        return splitted[1];
    }

    /**
     * Gets the id of the {@link Group} which this SID is actually is.
     *
     * @return the id of the {@link Group} which this SID is actually is
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSidId() {
        return SID_PREFIX + IdentifiableSid.SID_NAME_SEPARATOR + groupId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserGroupSid that = (UserGroupSid) o;

        if (!groupId.equals(that.groupId)) return false;

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return groupId.hashCode();
    }
}
