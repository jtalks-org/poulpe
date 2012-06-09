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
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.entity.Property;
import org.jtalks.poulpe.model.entity.*;
import org.jtalks.poulpe.test.fixtures.Fixtures;
import org.jtalks.poulpe.web.controller.SelectedEntity;

import com.google.common.collect.Lists;

/**
 * @author unascribed
 * @author Vyacheslav Zhivaev
 * @deprecated use {@link Fixtures}
 */
@Deprecated
public class ObjectsFactory {
    public static Component createComponent(long id, String name, String description, ComponentType type) {
        BaseComponent base = new BaseComponent(type); 
        Component comp = base.newComponent(name, description);
        comp.setId(id);
        return comp;
    }

    public static List<Component> createComponents() {
        List<Component> list = new ArrayList<Component>();

        for (ComponentType type : ComponentType.values()) {
            String random = random();
            list.add(createComponent(0, random, random, type));
        }

        return list;
    }

    public static List<PoulpeSection> getFakeSections(int sizeOfCollection) {
        return Lists.newArrayList(Collections.nCopies(sizeOfCollection, new PoulpeSection(random(), "desc")));
    }

    public static List<PoulpeSection> fakeSections() {
        return getFakeSections(10);
    }

    public static PoulpeSection fakeSection() {
        return new PoulpeSection(random(), random());
    }

    public static PoulpeSection sectionWithBranches(int n) {
        PoulpeSection section = new PoulpeSection(random(), random());
        while (n > 0) {
            PoulpeBranch branch = fakeBranch();
            section.addOrUpdateBranch(branch);
            branch.setSection(section);
            n--;
        }
        return section;
    }

    public static PoulpeSection sectionWithBranches() {
        return sectionWithBranches(10);
    }

    public static PoulpeBranch fakeBranch() {
        return new PoulpeBranch(random(), random());
    }

    public static List<PoulpeUser> getFakeUsers(int size) {
        List<PoulpeUser> users = new ArrayList<PoulpeUser>();
        for (int i = 0; i < size; i++) {
            String random = random();
            users.add(new PoulpeUser(random, random, random, random));
        }
        return users;
    }

    public static TopicType getFakeTopicType(long id, String title, String description) {
        TopicType topicType = new TopicType(title, description);
        topicType.setId(id);
        return topicType;
    }

    public static Group fakeGroup() {
        return new Group(random(), random());
    }

    public static List<Group> fakeGroupList(int listSize) {
        List<Group> result = Lists.newArrayList();

        if (listSize > 0) {
            for (int i = 0; i < listSize; i++) {
                result.add(fakeGroup());
            }
        }

        return result;
    }

    private static String random() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    public static Jcommune fakeForum() {
        Jcommune forum = (Jcommune) Fixtures.createComponent(ComponentType.FORUM);
        forum.setSections(fakeSections());
        return forum;
    }

    /**
     * Create {@link SelectedEntity} instance with predefined internal {@code SelectedEntity.entity}.
     * 
     * @param entity the entity to set
     * @return new instance of {@link SelectedEntity}
     */
    public static <T> SelectedEntity<T> createSelectedEntity(T entity) {
        SelectedEntity<T> result = new SelectedEntity<T>();
        result.setEntity(entity);
        return result;
    }
}
