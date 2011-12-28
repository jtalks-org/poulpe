package org.jtalks.poulpe.model.permissions;

import org.springframework.security.acls.model.Permission;

import javax.annotation.concurrent.ThreadSafe;

/**
 * @author stanislav bashkirtsev
 * @see <a href=http://jtalks.org/display/jtalks/Permission+Management>JTalks Permission Management Architecture</a>
 * @see <a href=http://jtalks.org/display/jtalks/Managing+Permissions>JTalks Permission Management Vision</a>
 */
public interface JtalksPermission extends Permission {
    String getName();
}
