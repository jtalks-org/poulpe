package org.jtalks.common.security.acl;

import org.springframework.security.acls.model.Sid;

/**
 * This interface is dedicated to join all the custom {@link Sid}s into one group that can be accessed with unified
 * method to obtain their identifier (it will be saved into the ACL table).
 *
 * @author stanislav bashkirtsev
 */
public interface IdentifiableSid extends Sid {
    /**
     * Gets the unique identifier of the SID (usually a database ID of the entity). It is string since the ACL tables
     * require this.
     *
     * @return the unique identifier of the SID (usually a database ID of the entity)
     */
    String getSidId();
}
