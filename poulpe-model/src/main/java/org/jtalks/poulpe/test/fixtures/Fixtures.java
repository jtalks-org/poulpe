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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.entity.Property;
import org.jtalks.common.model.entity.Rank;
import org.jtalks.poulpe.model.entity.*;

import com.google.common.collect.Lists;

/**
 * @author Kirill Afonin
 * @author Alexey Grigorev
 * 
 */
public final class Fixtures {

    private static final Random RANDOM = new Random();

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
        BaseComponent base = new BaseComponent(type); 
        Component c = base.newComponent(random(), random());
        c.addProperty("prop.name", "prop.value");
        return c;
    }
    
    public static Component randomComponent() {
        ComponentType[] types = ComponentType.values();
        return createComponent(types[randomInt(types.length)]);
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

    public static PoulpeUser createUser(String username) {
        String email = username + "@" + random() + "." + RandomStringUtils.randomAlphabetic(3);
        return new PoulpeUser(username, email, random(), "");
    }

    public static PoulpeUser createUser() {
        return createUser(random());
    }

    public static List<PoulpeUser> createBannedUsers(String... usernames) {
        List<PoulpeUser> result = Lists.newArrayList();

        for (String username : usernames) {
            PoulpeUser user = createUser(username);
            user.setBanReason("any reason");
            result.add(user);
        }

        return result;
    }

    public static List<PoulpeUser> bannedUsersListOf(int n) {
        List<PoulpeUser> users = usersListOf(n);

        for (PoulpeUser user : users) {
            user.setBanReason("banReason");
        }

        return users;
    }

    public static List<PoulpeUser> createUsers(String... usernames) {
        List<PoulpeUser> result = Lists.newArrayList();

        for (String username : usernames) {
            result.add(createUser(username));
        }

        return result;
    }

    public static List<PoulpeUser> usersListOf(int n) {
        List<PoulpeUser> result = Lists.newArrayListWithCapacity(n);

        while (n > 0) {
            result.add(createUser());
            n--;
        }

        return result;
    }

    public static Group createGroup() {
        return new Group(random(), "desc");
    }

    private static String random() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private static int randomInt() {
        return RANDOM.nextInt(10) + 1;
    }
    
    private static int randomInt(int max) {
        return RANDOM.nextInt(max);
    }

    /**
     * Create rank with random name and postCount.
     * 
     * @return new rank
     */
    public static Rank createRank() {
        int randNum = RANDOM.nextInt(1000);
        return new Rank("Rank" + randNum, randNum);
    }

    public static Jcommune createJcommune(int sectionsAmount) {
        Jcommune jcommune = new Jcommune("name", "description", new ArrayList<Property>());

        for (int i = 0; i < sectionsAmount; i++) {
            PoulpeSection section = createSectionWithBranches();
            jcommune.addSection(section);
        }

        return jcommune;
    }

}
