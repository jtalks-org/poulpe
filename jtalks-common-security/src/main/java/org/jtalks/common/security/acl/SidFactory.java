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

import com.google.common.collect.Maps;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.Sid;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author stanislav bashkirtsev
 */
public class SidFactory {
    private final static Map<String, Class<? extends IdentifiableSid>> CUSTOM_SIDS = getCustomSids();

    private static Map<String, Class<? extends IdentifiableSid>> getCustomSids() {
        Map<String, Class<? extends IdentifiableSid>> customSids = Maps.newHashMap();
        customSids.put(UserGroupSid.SID_PREFIX, UserGroupSid.class);
        return customSids;
    }

    public static Sid create(String sidName, boolean principal) {
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

    private static Sid parseCustomSid(String sidName) {
        for (Map.Entry<String, Class<? extends IdentifiableSid>> customSidEntry : CUSTOM_SIDS.entrySet()) {
            if (sidName.startsWith(customSidEntry.getKey())) {
                try {
                    return customSidEntry.getValue().getConstructor(String.class).newInstance(sidName);
                } catch (InstantiationException e) {
                    throw new SidWithoutRequiredConstructorException(customSidEntry.getValue(), e);
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

    public static class SidWithoutRequiredConstructorException extends RuntimeException {
        public SidWithoutRequiredConstructorException(Class<? extends IdentifiableSid> sidClass, Throwable ex) {
            super(sidClass + " doesnt have a public constructor with single String argument.", ex);
        }
    }

    public static class SidConstructorThrewException extends RuntimeException {
        public SidConstructorThrewException(Class<? extends IdentifiableSid> sidClass, Throwable ex) {
            super(sidClass + ". While initiating the class, it threw an exception.", ex);
        }
    }
}
