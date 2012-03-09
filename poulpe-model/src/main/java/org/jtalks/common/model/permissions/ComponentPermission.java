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
package org.jtalks.common.model.permissions;

import javax.annotation.Nonnull;

import ru.javatalks.utils.general.Assert;

/**
 * Permissions related to component.
 * 
 * @author Vyacheslav Zhivaev
 * 
 */
public enum ComponentPermission implements JtalksPermission {
    /**
     * The ability of user group or user to view the component.
     */
    VIEW_COMPONENT("1000", "VIEW_COMPONENT");

    private final String name;
    private final int mask;

    /**
     * Constructs the whole object without symbol.
     * 
     * @param mask a bit mask that represents the permission, can be negative only for restrictions (look at the class
     * description). The integer representation of it is saved to the ACL tables of Spring Security
     * @param name a textual representation of the permission (usually the same as the constant name), though the
     * restriction usually starts with the 'RESTRICTION_' word
     */
    ComponentPermission(int mask, @Nonnull String name) {
        Assert.throwIfNull(name, "name can't be null");
        this.mask = mask;
        this.name = name;
    }

    /**
     * Takes a string bit mask.
     * 
     * @param mask a bit mask that represents the permission. It's parsed into integer and saved into the ACL tables of
     * Spring Security
     * @param name a textual representation of the permission (usually the same as the constant name)
     * @throws NumberFormatException look at {@link Integer#parseInt(String, int)} for details on this as this method is
     * used underneath
     * @see BranchPermission#BranchPermission(int, String)
     * @see org.springframework.security.acls.domain.BasePermission
     */
    ComponentPermission(@Nonnull String mask, @Nonnull String name) {
        Assert.throwIfNull(name, "name can't be null");
        this.mask = Integer.parseInt(mask, 2);
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMask() {
        return mask;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPattern() {
        return null;
    }

    /**
     * Gets the human readable textual representation of the restriction (usually the same as the constant name).
     * 
     * @return the human readable textual representation of the restriction (usually the same as the constant name)
     */
    @Override
    public String getName() {
        return name;
    }

}
