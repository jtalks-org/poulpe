package org.jtalks.common.security;

/**
 * Contains constants specific to security.
 *
 * @author Kirill Afonin
 */
public final class SecurityConstants {
    /**
     * Username of anonymous user (not logged in user).
     * If you want to change it you should change it here and in security-context.xml
     */
    public static final String ANONYMOUS_USERNAME = "anonymousUser";
    /**
     * Role name of administrators.
     */
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    /**
     * Role name of user. Every registered user have this role by default.
     */
    public static final String ROLE_USER = "ROLE_USER";

    /**
     * You can't create instance of this class.
     */
    private SecurityConstants() {
    }
}

