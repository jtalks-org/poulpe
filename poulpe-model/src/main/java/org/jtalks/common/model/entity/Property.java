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
package org.jtalks.common.model.entity;

import org.jtalks.common.model.entity.Entity;

/**
 * The Property entity which contains configuration for components
 * 
 * @author Vahluev Vyacheslav
 */
public class Property extends Entity {
    /**
     * Property's name
     */
    private String name;
    /**
     * Property's value
     */
    private String value;

    /**
     * Default constructor, sets nothing, used only by hibernate
     */
    protected Property() {
    }

    /**
     * Constructor which sets name and value of the property
     * 
     * @param name of the property
     * @param value of the property
     */
    public Property(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Gets the name of the property
     *
     * @return name of the property
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the property
     *
     * @param name new name of the property
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the value of the property
     *
     * @return current value of the property
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the property
     *
     * @param value new value of the property
     */
    public void setValue(String value) {
        this.value = value;
    }
}
