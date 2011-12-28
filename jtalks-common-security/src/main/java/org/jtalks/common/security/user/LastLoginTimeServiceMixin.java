package org.jtalks.common.security.user;

import org.jtalks.common.model.entity.User;

/**
 * @author stanislav bashkirtsev
 */
public interface LastLoginTimeServiceMixin {
    /**
     * Updates user last login time to current time.
     *
     * @param user user which must be updated
     * @see org.jtalks.common.model.entity.User
     */
    void updateLastLoginTime(User user);
}
