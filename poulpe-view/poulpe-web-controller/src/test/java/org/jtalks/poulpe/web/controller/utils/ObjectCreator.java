package org.jtalks.poulpe.web.controller.utils;

import java.util.ArrayList;
import java.util.List;

import org.jtalks.common.model.entity.User;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Section;
import org.jtalks.poulpe.model.entity.TopicType;

public class ObjectCreator {
    public static List<Section> getFakeSections(int sizeOfCollection) {
        List<Section> sections = new ArrayList<Section>();
        for (int i = 0; i < sizeOfCollection; i++) {
            sections.add(getFakeSection("fake " + i, "description " + i));
        }
        return sections;
    }
    
    /**
     * @deprecated use {@link Section#Section(String, String)} constructor instead
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
        for(int i = 0 ; i < size ; i++){
            User user = new User("user " + i, "email " + i, "password " + i);
            users.add(user);
        }
        return users;
    }
    
    public static TopicType getFakeTopicType(long id, String title, String description) {
    	TopicType topicType = new TopicType();
    	topicType.setId(id);
    	topicType.setTitle(title);
    	topicType.setDescription(description);
    	
    	return topicType;
    }

}
