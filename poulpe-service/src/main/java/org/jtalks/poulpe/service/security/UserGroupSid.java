package org.jtalks.poulpe.service.security;

import org.jtalks.poulpe.model.entity.Group;
import org.springframework.security.acls.model.Sid;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * This class does the same as {@link org.springframework.security.acls.domain.PrincipalSid} does for users. More
 * precisely it contains some security ID that is used by Spring ACL to associate the owner to the permissions it owns.
 * Thus this class contains an ID that will be saved into database ({@code acl_sids#sid, acl_object_identity#owner_sid,
 * acl_entry#sid}).
 *
 * @author stanislav bashkirstev
 */
public class UserGroupSid implements Sid {
    private final String groupId;

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
