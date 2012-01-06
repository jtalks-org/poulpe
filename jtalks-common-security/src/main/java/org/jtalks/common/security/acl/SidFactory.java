/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jtalks.common.security.acl;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.Sid;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Decides what implementation of {@link Sid} should be created by the string representation of the sid name (or sid
 * id). There are might be either standard {@link Sid}s or custom sids like {@link UserGroupSid}. If you want to add
 * another possible implementation, take a look at the method {@link #getCustomSids()}.
 *
 * @author stanislav bashkirtsev
 * @see Sid
 * @see IdentifiableSid
 */
public final class SidFactory {
    private final static Map<String, Class<? extends IdentifiableSid>> CUSTOM_SIDS = getCustomSids();

    /**
     * This is a static factory, it shouldn't be instantiated.
     */
    private SidFactory() {
    }

    /**
     * Gets the mapping of the sid implementations to the sid name formats. All the custom sids should have the prefix
     * like {@link UserGroupSid#SID_PREFIX} by which we can find out what type of sid it is.
     *
     * @return the mapping of the sid implementations to the string format of the sids
     */
    private static Map<String, Class<? extends IdentifiableSid>> getCustomSids() {
        Map<String, Class<? extends IdentifiableSid>> customSids = Maps.newHashMap();
        customSids.put(UserGroupSid.SID_PREFIX, UserGroupSid.class);
        return customSids;
    }

    /**
     * Looks at the format of the {@code sidName} and finds out what sid implementation should be created. If the
     * specified name doesn't comply with the format of custom sids (prefix + {@link IdentifiableSid#SID_NAME_SEPARATOR}
     * + entity id), then ordinary Spring Security implementations are used (either {@link PrincipalSid} or {@link
     * GrantedAuthoritySid} which is defined by the second parameter {@code principal}.
     *
     * @param sidName   the name of the sid (its id) to look at its format and decide what implementation of sid should
     *                  be created
     * @param principal pass {@code true} if it's some kind of entity ID (like user or group), or {@code false} if it's
     *                  some standard role ({@link GrantedAuthoritySid}
     * @return created instance of sid that has the {@code sidName} as the sid id inside
     */
    public static Sid create(@Nonnull String sidName, boolean principal) {
        Sid toReturn = parseCustomSid(sidName);
        if (toReturn == null) {
            if (principal) {
                toReturn = new PrincipalSid(sidName);
            } else {
                toReturn = new GrantedAuthoritySid(sidName);
            }
        }
        return toReturn;
    }

    /**
     * Iterates through all the mapping from {@link #getCustomSids()} and if it finds some custom sid class that should
     * be instantiated (it looks at the sid name that should start with sid prefix), then it instantiates it via
     * Reflection.
     *
     * @param sidName the name of the sid to find the respective sid implementation
     * @return the instantiated sid implementation that complies with the pattern of specified sid name or {@code null}
     *         if no mapping for that name was found and there are no appropriate custom implementations of sid
     */
    private static Sid parseCustomSid(String sidName) {
        for (Map.Entry<String, Class<? extends IdentifiableSid>> customSidEntry : CUSTOM_SIDS.entrySet()) {
            if (sidName.startsWith(customSidEntry.getKey())) {
                try {
                    return customSidEntry.getValue().getDeclaredConstructor(String.class).newInstance(sidName);
                } catch (InstantiationException e) {
                    throw new SidClassIsNotConcreteException(customSidEntry.getValue(), e);
                } catch (IllegalAccessException e) {
                    throw new SidWithoutRequiredConstructorException(customSidEntry.getValue(), e);
                } catch (InvocationTargetException e) {
                    throw new SidConstructorThrewException(customSidEntry.getValue(), e);
                } catch (NoSuchMethodException e) {
                    throw new SidWithoutRequiredConstructorException(customSidEntry.getValue(), e);
                }
            }
        }
        return null;
    }

    @VisibleForTesting
    static void addMapping(String sidPrefix, Class<? extends IdentifiableSid> clazz) {
        CUSTOM_SIDS.put(sidPrefix, clazz);
    }

    /**
     * Can be thrown if the sid implementation is not a concrete class.
     *
     * @author stanislav bashkirtsev
     */
    public static class SidClassIsNotConcreteException extends RuntimeException {
        /**
         * @param sidClass the class of the sid implementation that caused the problem
         * @param ex       reflection exception that originally caused the error when factory tried to instantiate the
         *                 class
         */
        public SidClassIsNotConcreteException(Class<? extends IdentifiableSid> sidClass, Throwable ex) {
            super(sidClass + " class is not a concrete class: its either an interface or abstract.", ex);
        }
    }

    /**
     * All the custom sid implementations should have at least one public constructor that accepts a string parameter -
     * the sid id. If there is no such constructor, the factory cannot instantiate the class correctly and throws this
     * exception.
     *
     * @author stanislav bashkirtsev
     */
    public static class SidWithoutRequiredConstructorException extends RuntimeException {
        /**
         * @param sidClass the class of the sid implementation that caused the problem
         * @param ex       reflection exception that originally caused the error when factory tried to instantiate the
         *                 class
         */
        public SidWithoutRequiredConstructorException(Class<? extends IdentifiableSid> sidClass, Throwable ex) {
            super(sidClass + " doesnt have a public constructor with single String argument.", ex);
        }
    }

    /**
     * Is thrown when the custom sid implementation threw an exception inside of its constructor while the factory tried
     * to instantiate it.
     */
    public static class SidConstructorThrewException extends RuntimeException {
        /**
         * @param sidClass the class that has the public constructor with one string parameter which was tried to be
         *                 instantiated
         * @param ex       the root exception that was thrown when the constructor was invoked
         */
        public SidConstructorThrewException(Class<? extends IdentifiableSid> sidClass, Throwable ex) {
            super(sidClass + ". While initiating the class, it threw an exception.", ex);
        }
    }
}
