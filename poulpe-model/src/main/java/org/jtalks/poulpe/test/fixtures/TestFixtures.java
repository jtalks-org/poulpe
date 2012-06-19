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
package org.jtalks.poulpe.test.fixtures;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.entity.Property;
import org.jtalks.common.model.entity.Rank;
import org.jtalks.common.model.entity.User;
import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.model.entity.ComponentBase;
import org.jtalks.poulpe.model.entity.ComponentType;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.jtalks.poulpe.model.entity.TopicType;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Provides unified way for creating text fixtures.<br>
 * <br>
 * This class is not under test source folder because it should be accessible for all components.
 *
 * @author Kirill Afonin
 * @author Alexey Grigorev
 */
public final class TestFixtures {

    private static final Random RANDOM = new Random();

    /**
     * @return branch with random name and description beloning to random section with moderators group
     */
    public static PoulpeBranch branch() {
        PoulpeBranch newBranch = new PoulpeBranch(random(), random());
        newBranch.setSection(section());
        newBranch.setModeratorsGroup(group());
        return newBranch;
    }

    /**
     * @return topic type with random name and description
     */
    public static TopicType topicType() {
        return new TopicType(random(), random());
    }

    /**
     * @param type of the component
     * @return component with random name and description and with given type
     */
    public static Component component(ComponentType type) {
        ComponentBase base = new ComponentBase(type);
        return base.newComponent(random(), random());
    }

    /**
     * @return list of components of all {@link ComponentType} values
     */
    public static List<Component> allComponents() {
        List<Component> result = Lists.newArrayList();

        for (ComponentType componentType : ComponentType.values()) {
            result.add(component(componentType));
        }

        return result;
    }

    /**
     * @return component with {@link ComponentType} = {@link ComponentType#FORUM}
     */
    public static Jcommune jcommune() {
        return (Jcommune) component(ComponentType.FORUM);
    }

    /**
     * @param sectionsAmount amount of sections to be add to result
     * @return component with {@link ComponentType} = {@link ComponentType#FORUM} and with sections and branches
     */
    public static Jcommune jcommuneWithSections(int sectionsAmount) {
        Jcommune jcommune = jcommune();

        for (int i = 0; i < sectionsAmount; i++) {
            PoulpeSection section = sectionWithBranches();
            jcommune.addOrUpdateSection(section);
        }

        return jcommune;
    }

    /**
     * @return jcommune component with 10 sections
     */
    public static Jcommune jcommuneWithSections() {
        return jcommuneWithSections(10);
    }

    /**
     * @return section with random amount of branches (from 5 to 15)
     */
    public static PoulpeSection sectionWithBranches() {
        return sectionWithBranches(5 + randomInt(10));
    }

    /**
     * @param branchesAmount amount of branches in section
     * @return section with random name and given amount of branches
     */
    public static PoulpeSection sectionWithBranches(int branchesAmount) {
        PoulpeSection section = new PoulpeSection(random());

        for (int i = 0; i < branchesAmount; i++) {
            PoulpeBranch branch = branch();
            branch.setSection(section);
            section.addOrUpdateBranch(branch);
        }

        return section;
    }

    /**
     * @return component of random {@link ComponentType}
     */
    public static Component randomComponent() {
        return component(randomComponentType());
    }

    /**
     * @return random {@link ComponentType} value
     */
    public static ComponentType randomComponentType() {
        ComponentType[] types = ComponentType.values();
        return types[randomInt(types.length)];
    }

    /**
     * @return {@link ComponentBase} with random {@link ComponentType}
     */
    public static ComponentBase baseComponent() {
        return new ComponentBase(randomComponentType());
    }

    /**
     * @return property with random name, description and validation rule
     */
    public static Property property() {
        Property property = new Property(random(), random());
        property.setValidationRule(random());
        return property;
    }

    /**
     * @return section with random name
     */
    public static PoulpeSection section() {
        return new PoulpeSection(random());
    }

    /**
     * @param username of the user
     * @return user with the given username, generated email and empty salt
     */
    public static PoulpeUser user(String username) {
        String email = username + "@" + random() + ".com";
        return new PoulpeUser(username, email, random(), "");
    }

    /**
     * @return user with random name, email, password and empty salt
     */
    public static PoulpeUser user() {
        return user(random());
    }

    /**
     * @return group with randoms users
     */
    public static Group groupWithUsers() {
        List<PoulpeUser> users = usersListOf(5);
        Group group = group();
        group.setUsers((List<User>) (Object) users);
        return group;
    }

    /**
     * @param n amount of users
     * @return list of users
     */
    public static List<PoulpeUser> usersListOf(int n) {
        List<PoulpeUser> result = Lists.newArrayListWithCapacity(n);

        while (n > 0) {
            result.add(user());
            n--;
        }

        return result;
    }

    /**
     * @return group with random name and description
     */
    public static Group group() {
        return new Group(random(), random());
    }

    /**
     * @return list with one group of banned users
     */
    public static List<Group> bannedGroups() {
        return Collections.singletonList(group());
    }

    /**
     * @return rank with random name and post limit
     */
    public static Rank rank() {
        return new Rank(random(), randomInt(1000));
    }

    /**
     * @return random string of 10 symbols
     */
    private static String random() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    /**
     * @param max upper bound
     * @return random int with upper bound
     */
    private static int randomInt(int max) {
        return RANDOM.nextInt(max);
    }

}
