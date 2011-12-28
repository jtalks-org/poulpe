package org.jtalks.common.security;


import com.google.common.annotations.VisibleForTesting;
import org.jtalks.common.model.dao.UserDao;
import org.jtalks.common.model.entity.Entity;
import org.jtalks.common.model.entity.User;
import org.jtalks.common.security.acl.AclManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


/**
 * Abstract layer for Spring Security. Contains methods for authentication and authorization. This service
 *
 * @author Kirill Afonin
 * @author Max Malakhov
 * @author Dmitry Sokolov
 */
public class SecurityService implements UserDetailsService {
    private final UserDao userDao;
    private final AclManager aclManager;
    private SecurityContextFacade securityContextFacade = new SecurityContextFacade();

    /**
     * Constructor creates an instance of service.
     *
     * @param userDao    {@link UserDao} to be injected
     * @param aclManager manager for actions with ACLs
     */
    public SecurityService(UserDao userDao, AclManager aclManager) {
        this.userDao = userDao;
        this.aclManager = aclManager;
    }

    /**
     * Get current authenticated {@link User}.
     *
     * @return current authenticated {@link User} or {@code null} if there is no authenticated {@link User}.
     * @see User
     */
    public User getCurrentUser() {
        return userDao.getByUsername(getCurrentUserUsername());
    }

    /**
     * Get current authenticated {@link User} username.
     *
     * @return current authenticated {@link User} username or {@code null} if there is no authenticated {@link User}.
     */
    public String getCurrentUserUsername() {
        Authentication auth = securityContextFacade.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        Object principal = auth.getPrincipal();
        String username = extractUsername(principal);

        if (isAnonymous(username)) {
            return null;
        }
        return username;
    }

    /**
     * Get username from principal.
     *
     * @param principal principal
     * @return username
     */
    private String extractUsername(Object principal) {
        // if principal is spring security user, cast it and get username
        // else it is javax.security principal with toString() that return username
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
    }

    /**
     * @param username username
     * @return {@code true} if user is anonymous
     */
    private boolean isAnonymous(String username) {
        return username.equals(SecurityConstants.ANONYMOUS_USERNAME);
    }


    /**
     * Delete object from acl. All permissions will be removed.
     *
     * @param securedObject a removed secured object.
     */
    public void deleteFromAcl(Entity securedObject) {
        deleteFromAcl(securedObject.getClass(), securedObject.getId());
    }

    /**
     * Delete object from acl. All permissions will be removed.
     *
     * @param clazz object {@code Class}
     * @param id    object id
     */
    public void deleteFromAcl(Class clazz, long id) {
        aclManager.deleteFromAcl(clazz, id);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userDao.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return user;
    }

    @VisibleForTesting
    void setSecurityContextFacade(SecurityContextFacade securityContextFacade) {
        this.securityContextFacade = securityContextFacade;
    }
}


