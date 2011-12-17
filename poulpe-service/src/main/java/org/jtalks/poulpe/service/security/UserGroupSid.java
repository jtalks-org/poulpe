package org.jtalks.poulpe.service.security;

import org.jtalks.poulpe.model.entity.Group;
import org.springframework.security.acls.model.Sid;

import javax.annotation.Nonnull;

/**
 * @author stanislav bashkirstev
 */
public class UserGroupSid implements Sid {
    private final String groupId;

    public UserGroupSid(@Nonnull Long groupId) {
        this.groupId = String.valueOf(groupId);
    }

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
