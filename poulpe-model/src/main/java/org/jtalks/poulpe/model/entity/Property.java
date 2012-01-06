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
package org.jtalks.poulpe.model.entity;

import org.jtalks.common.model.entity.Entity;

/**
 * The Property entity which contains configuration
 * for components
 *
 * @author Vahluev Vyacheslav
 *
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
     * Default constructor, sets nothing
     */
    public Property() {
    }

    /**
     * Constructor which sets name and value of the property
     * @param name
     * @param value
     */
    public Property(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Gets the name of the property
     * @return name of the property
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the property
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the value of the property
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the property
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Returns String representation of the property
     */
    public String toString() {
        return this.getName() + ": " + this.getValue();
    }

}
