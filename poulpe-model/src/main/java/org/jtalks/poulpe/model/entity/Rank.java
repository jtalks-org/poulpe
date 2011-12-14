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
 * Forum rank.
 * If autoAssigned is set then rank is assigned by moderator.
 * In other case rank applies to the user when he has more then posts than postCount.
 * 
 * @author Pavel Vervenko
 */
public class Rank extends Entity {
    private String rankName;
    private boolean autoAssigned;
    private int postCount;

    /**
     * Default constructor.
     */
    public Rank() {
    }

    /**
     * Create auto-assigned rank with specified name.
     * @param rankName new rank's name
     */
    public Rank(String rankName) {
        this.rankName = rankName;
        this.postCount = 0;
        this.autoAssigned = true;
    }

    /**
     * Create rank with specified posts limit and name.
     * @param rankName rank name
     * @param postCount posts limit
     */
    public Rank(String rankName, int postCount) {
        this.rankName = rankName;
        this.postCount = postCount;
        this.autoAssigned = false;
    }

    /**
     * Check if ranks is auto-assigned.
     * @return value
     */
    public boolean isAutoAssigned() {
        return autoAssigned;
    }

    /**
     * Mark rank as auto-assigned.
     * @param autoAssigned value
     */
    public void setAutoAssigned(boolean autoAssigned) {
        this.autoAssigned = autoAssigned;
    }

    /**
     * Get the posts count.
     * @return postCount
     */
    public int getPostCount() {
        return postCount;
    }

    /**
     * Set the posts count.
     * @param postCount value
     */
    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    /**
     * Get ranke name.
     * @return rank name
     */
    public String getRankName() {
        return rankName;
    }

    /**
     * Set rank name.
     * @param rankName value to set
     */
    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

}
