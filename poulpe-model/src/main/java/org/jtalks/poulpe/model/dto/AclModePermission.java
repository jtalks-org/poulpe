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

import javax.annotation.concurrent.Immutable;

import org.jtalks.common.model.entity.Entity;
import org.jtalks.common.model.permissions.JtalksPermission;

/**
 * @author Vyacheslav Zhivaev
 * 
 */
@Immutable
public class AclModePermission {
    private final Entity target;
    private final AclMode mode;
    private final JtalksPermission permission;

    public AclModePermission(Entity target, AclMode mode, JtalksPermission permission) {
        this.target = target;
        this.mode = mode;
        this.permission = permission;
    }

    /**
     * @return the target
     */
    public Entity getTarget() {
        return target;
    }

    /**
     * @return the mode
     */
    public AclMode getMode() {
        return mode;
    }

    /**
     * @return the permission
     */
    public JtalksPermission getPermission() {
        return permission;
    }

}
