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

import java.util.ArrayList;
import java.util.List;

/**
 * The central JCommune entity that contains all the configuration for
 * respective component.
 * 
 * @author Guram Savinov
 * @author Vahluev Vyacheslav
 */
public class Jcommune extends Component {

    private List<Section> sections = new ArrayList<Section>();

    /**
     * Creates Component with {@link ComponentType#FORUM}
     * and empty section list
     */
    public Jcommune() {
        setComponentType(ComponentType.FORUM);
    }
    
    /**
     * Converts given {@link Component} to {@link Jcommune}, keeping the
     * identity of the first (i.e. id and uuid are copied as well)
     * 
     * @param component to be converted
     * @return converted {@link Jcommune}
     * 
     * @exception IllegalArgumentException when passed component is not Forum
     * (its componentType != {@link ComponentType#FORUM})
     */
    public static Jcommune fromComponent(Component component) {
        checkComponentType(component.getComponentType());

        Jcommune jcommune = new Jcommune();
        jcommune.setId(component.getId());
        jcommune.setUuid(component.getUuid());
        jcommune.setName(component.getName());
        jcommune.setDescription(component.getDescription());
        jcommune.setProperties(component.getProperties());

        return jcommune;
    }

    private static void checkComponentType(ComponentType componentType) {
        if (componentType != ComponentType.FORUM) {
            throw new IllegalArgumentException("Can't convert component to Forum because componentType == FORUM " +
            		"is expected, got " + componentType);
        }
    }

    /**
     * Gets the sections.
     * 
     * @return the sections
     */
    public List<Section> getSections() {
        return sections;
    }

    /**
     * Sets the sections.
     * 
     * @param sections the sections to set
     */
    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    /**
     * Adds a section to the list.
     * 
     * @param section the section to add
     */
    public void addSection(Section section) {
        sections.add(section);
    }
}
