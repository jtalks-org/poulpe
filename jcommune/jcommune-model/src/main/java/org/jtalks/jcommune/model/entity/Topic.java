/* 
 * JTalks for uniting people
 * Copyright (C) 2011  JavaTalks Team
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * Also add information on how to contact you by electronic and paper mail.
 * 
 * This file creation date: Apr 12, 2011 / 8:05:19 PM
 * The JTalks Project
 * http://www.jtalks.org
 */
package org.jtalks.jcommune.model.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Represents the topic of the forum.
 * Contains the list of related {@link Post}
 * @author Temdegon
 */
public class Topic extends Persistent {

    private Date creationDate;
    private User userCreated;
    private String topicName;
    private List<Post> posts = new ArrayList();

    public void addPost(Post newPost) {
        posts.add(newPost);
    }

    public void removePost(Post postToRemove) {
        posts.remove(postToRemove);
    }

    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @return the userCreated
     */
    public User getUserCreated() {
        return userCreated;
    }

    /**
     * @param userCreated the userCreated to set
     */
    public void setUserCreated(User userCreated) {
        this.userCreated = userCreated;
    }

    /**
     * @return the topicName
     */
    public String getTopicName() {
        return topicName;
    }

    /**
     * @param topicName the topicName to set
     */
    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    /**
     * @return the posts
     */
    public Collection<Post> getPosts() {
        return posts;
    }

    /**
     * @param posts the posts to set
     */
    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Topic other = (Topic) obj;
        if (this.creationDate != other.creationDate && (this.creationDate == null || !this.creationDate.equals(other.creationDate))) {
            return false;
        }
        if ((this.topicName == null) ? (other.topicName != null) : !this.topicName.equals(other.topicName)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.creationDate != null ? this.creationDate.hashCode() : 0);
        hash = 67 * hash + (this.topicName != null ? this.topicName.hashCode() : 0);
        return hash;
    }
}