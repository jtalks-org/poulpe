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
package org.jtalks.poulpe.model.dto;

import org.apache.commons.lang.Validate;
import org.jtalks.common.model.entity.Entity;
import org.jtalks.common.model.permissions.JtalksPermission;

import javax.annotation.concurrent.Immutable;

/**
 * DTO container for {@link JtalksPermission} and {@link Entity} for which permission is.
 *
 * @author Vyacheslav Zhivaev
 */
@Immutable
public class PermissionForEntity {
    private final Entity target;
    private final boolean allowed;
    private final JtalksPermission permission;

    /**
     * Constructs container.
     *
     * @param target     the entity for which permission is
     * @param allowed    the allowed (when {@code true}) or restricted flag
     * @param permission the permission
     */
    public PermissionForEntity(Entity target, boolean allowed, JtalksPermission permission) {
        this.target = target;
        this.allowed = allowed;
        this.permission = permission;
    }

    /**
     * Constructs container.
     *
     * @param target     the entity for which permission is
     * @param mode       the mode of permission, can be only {@code "allow"} or {@code "restrict"} (case ignored)
     * @param permission the permission
     */
    public PermissionForEntity(Entity target, String mode, JtalksPermission permission) {
        boolean allowedLocal = "allow".equalsIgnoreCase(mode);
        Validate.isTrue(allowedLocal || "restrict".equalsIgnoreCase(mode),
                "Illegal format of parameter 'mode', it can be only 'allow' or 'restrict' (case ignored)");

        this.target = target;
        this.allowed = allowedLocal;
        this.permission = permission;
    }

    /**
     * Gets target entity.
     *
     * @return the target
     */
    public Entity getTarget() {
        return target;
    }

    /**
     * Is this container builded to allow permission.
     *
     * @return the allowedNotRestricted
     */
    public boolean isAllowed() {
        return allowed;
    }

    /**
     * Gets permission.
     *
     * @return the permission
     */
    public JtalksPermission getPermission() {
        return permission;
    }

    /**
     * Returns a permission restricted to the specified entity.
     *
     * @param target     an entity (Object Identity) to restrict access to
     * @param permission a permission that won't be allowed to do with specified entity
     * @return permission that will be restricted to the specified entity
     */
    public static PermissionForEntity restricted(Entity target, JtalksPermission permission) {
        return new PermissionForEntity(target, false, permission);
    }

    /**
     * Returns a permission allowed to the specified entity.
     *
     * @param target     an entity (Object Identity) to allow access to
     * @param permission a permission that will be allowed to do with specified entity
     * @return permission that will be allowed to the specified entity
     */
    public static PermissionForEntity allowed(Entity target, JtalksPermission permission) {
        return new PermissionForEntity(target, true, permission);
    }

}
