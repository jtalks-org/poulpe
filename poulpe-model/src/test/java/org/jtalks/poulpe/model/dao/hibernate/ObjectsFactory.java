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
package org.jtalks.poulpe.model.dao.hibernate;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.jtalks.common.model.entity.Component;
import org.jtalks.common.model.entity.ComponentType;
import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.entity.Rank;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.model.entity.TopicType;
import org.jtalks.poulpe.model.entity.User;

import com.google.common.collect.Lists;

/**
 * @author Kirill Afonin
 * @author Alexey Grigorev
 * 
 */
public final class ObjectsFactory {

    public static PoulpeBranch createBranch() {
        PoulpeBranch newBranch = new PoulpeBranch(RandomStringUtils.random(15), "desc");
        newBranch.setSection(createSection());
        newBranch.setModeratorsGroup(createGroup());
        return newBranch;
    }

    public static TopicType topicType() {
        return new TopicType(random(), "desc");
    }

    public static Component createComponent(ComponentType type) {
        Component c = new Component(random(), "desc", type);
        c.addProperty("prop.name", "prop.value");
        return c;
    }

    public static PoulpeSection createSectionWithBranches() {
        return createSectionWithBranches(randomInt());
    }

    public static PoulpeSection createSectionWithBranches(int branchesAmount) {
        PoulpeSection section = new PoulpeSection(random());

        for (int i = 0; i < branchesAmount; i++) {
            PoulpeBranch branch = createBranch();
            branch.setSection(section);
            section.addOrUpdateBranch(branch);
        }

        return section;
    }

    public static PoulpeSection createSection() {
        return new PoulpeSection(RandomStringUtils.random(15));
    }

    public static User createUser(String username) {
        String email = username + "@" + RandomStringUtils.randomAlphanumeric(10) + "."
                + RandomStringUtils.randomAlphabetic(3);
        return new User(username, email, RandomStringUtils.randomAlphanumeric(8), "");
    }

    public static List<User> createBannedUsers(String... usernames) {
        List<User> result = Lists.newArrayList();

        for (String username : usernames) {
            User user = createUser(username);
            user.setBanReason("any reason");
            result.add(user);
        }

        return result;
    }

    public static List<User> createUsers(String... usernames) {
        List<User> result = Lists.newArrayList();

        for (String username : usernames) {
            result.add(createUser(username));
        }

        return result;
    }

    public static Group createGroup() {
        return new Group(random(), "desc");
    }

    private static String random() {
        return UUID.randomUUID().toString();
    }

    private static int randomInt() {
        return new Random().nextInt(10) + 1;
    }

    /**
     * Create rank with random name and postCount.
     * 
     * @return new rank
     */
    public static Rank createRank() {
        int randNum = new Random().nextInt(1000);
        return new Rank("Rank" + randNum, randNum);
    }

    public static Jcommune createJcommune(int sectionsAmount) {
        Jcommune jcommune = Jcommune.fromComponent(createComponent(ComponentType.FORUM));

        for (int i = 0; i < sectionsAmount; i++) {
            PoulpeSection section = createSectionWithBranches();
            jcommune.addSection(section);
        }

        return jcommune;
    }

}
