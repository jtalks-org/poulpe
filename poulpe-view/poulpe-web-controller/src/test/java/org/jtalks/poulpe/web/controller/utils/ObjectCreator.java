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
package org.jtalks.poulpe.web.controller.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.jtalks.poulpe.model.entity.User;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.model.entity.ComponentType;
import org.jtalks.poulpe.model.entity.Section;
import org.jtalks.poulpe.model.entity.TopicType;

public class ObjectCreator {
    public static Component createComponent(long id, String name, String description, ComponentType type) {
        Component comp = new Component(name, description, type);
        comp.setId(id);
        return comp;
    }

    public static List<Component> createComponents() {
        List<Component> list = new ArrayList<Component>();
        
        for (ComponentType type : ComponentType.values()) {
            String random = random();
            list.add(new Component(random, random, type));
        }
        
        return list;
    }

    public static List<Section> getFakeSections(int sizeOfCollection) {
        List<Section> sections = new ArrayList<Section>();
        for (int i = 0; i < sizeOfCollection; i++) {
            sections.add(new Section(random(), "desc"));
        }
        return sections;
    }

    /**
     * @deprecated use {@link Section#Section(String, String)} constructor
     * instead
     */
    @Deprecated
    public static Section getFakeSection(String name, String description) {
        Section section = new Section();
        section.setName(name);
        section.setDescription(description);
        return section;
    }

    /**
     * @deprecated use {@link Branch#Branch(String, String)} constructor instead
     */
    @Deprecated
    public static Branch getFakeBranch(String name, String description) {
        Branch branch = new Branch();
        branch.setName(name);
        branch.setDescription(description);
        return branch;
    }

    public static List<User> getFakeUsers(int size) {
        List<User> users = new ArrayList<User>();
        for (int i = 0; i < size; i++) {
            String random = random();
            users.add(new User(random, random, random, random));
        }
        return users;
    }

    public static TopicType getFakeTopicType(long id, String title, String description) {
        TopicType topicType = new TopicType(title, description);
        topicType.setId(id);
        return topicType;
    }

    private static String random() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

}
