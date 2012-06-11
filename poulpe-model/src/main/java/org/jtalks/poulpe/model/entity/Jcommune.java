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

import org.jtalks.common.model.entity.Property;

import java.util.ArrayList;
import java.util.List;

/**
 * The central JCommune entity that contains all the configuration for respective component.
 *
 * @author Guram Savinov
 * @author Vahluev Vyacheslav
 */
public class Jcommune extends Component {

    private List<PoulpeSection> sections = new ArrayList<PoulpeSection>();

    /**
     * Creates Component with {@link ComponentType#FORUM} and empty section list.
     * Visible for hibernate.
     */
    protected Jcommune() {
        super(ComponentType.FORUM);
    }

    /**
     * @param name
     * @param description
     * @param defaultProperties
     */
    public Jcommune(String name, String description, List<Property> defaultProperties) {
        super(name, description, ComponentType.FORUM, defaultProperties);
    }

    /**
     * Gets the sections.
     *
     * @return the sections
     */
    public List<PoulpeSection> getSections() {
        return sections;
    }

    /**
     * Sets the sections.
     *
     * @param sections the sections to set
     */
    public void setSections(List<PoulpeSection> sections) {
        this.sections = sections;
    }

    /**
     * Removes the specified section from jcommune instance if it's there, does nothing if it's not there.
     *
     * @param section the section to remove it from the list
     * @return {@code true} if the specified section wasn't in the list
     */
    public boolean removeSection(PoulpeSection section) {
        return getSections().remove(section);
    }

    /**
     * Adds a section to the list.
     *
     * @param section the section to add
     */
    public void addSection(PoulpeSection section) {
        int position = sections.indexOf(section);
        if (position >= 0) {
            sections.set(position, section);
        } else {
            sections.add(section);
        }
    }
}
