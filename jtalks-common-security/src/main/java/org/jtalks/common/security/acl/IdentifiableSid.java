package org.jtalks.common.security.acl;

import org.springframework.security.acls.model.Sid;

/**
 * This interface is dedicated to join all the custom {@link Sid}s into one group that can be accessed with unified
 * method to obtain their identifier (it will be saved into the ACL table).
 *
 * @author stanislav bashkirtsev
 */
public interface IdentifiableSid extends Sid {
    static final String SID_NAME_SEPARATOR = ":";

    /**
     * Gets the unique identifier of the SID (usually a database ID of the entity). It is string since the ACL tables
     * require this.
     *
     * @return the unique identifier of the SID (usually a database ID of the entity)
     */
    String getSidId();

    public static class WrongFormatException extends RuntimeException {
        private final String sidName;

        public WrongFormatException(String sidName) {
            super("Sid name is of incorrect format: " + sidName);
            this.sidName = sidName;
        }

        public String getSidName() {
            return sidName;
        }
    }
}
