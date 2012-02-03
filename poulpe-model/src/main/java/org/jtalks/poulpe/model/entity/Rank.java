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

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.jtalks.common.model.entity.Entity;
import org.jtalks.poulpe.validation.annotations.UniqueConstraint;
import org.jtalks.poulpe.validation.annotations.UniqueField;

/**
 * Forum rank.
 * If autoAssigned is set then rank is assigned by moderator.
 * In other case rank applies to the user when he has more then posts than postCount.
 * 
 * @author Pavel Vervenko
 */
@UniqueConstraint
public class Rank extends Entity {
    /**
     * Error message if rank already exists
     */
    public static final String RANK_ALREADY_EXISTS = "rank.validation.not_unique_rank_name";
    
    /**
     * Error message if rank name is void
     */
    public static final String RANK_CANT_BE_VOID = "rank.error.rank_name_cant_be_void";
    
    @UniqueField(message = RANK_ALREADY_EXISTS)
    @NotNull(message = RANK_CANT_BE_VOID)
    @NotEmpty(message = RANK_CANT_BE_VOID)
    private String rankName;
    
    private boolean autoAssigned;
    private int postCount;

    /**
     * Default constructor with postCount = 0 and autoAssigned = false
     */
    public Rank() {
    }

    /**
     * Create auto-assigned rank with specified name, sets postCount = 0 and
     * autoAssigned = false
     * 
     * @param rankName new rank's name
     */
    public Rank(String rankName) {
        this(rankName, 0, false);
    }

    /**
     * Create auto-assigned rank with specified posts limit and name
     * 
     * @param rankName rank name
     * @param postCount posts limit
     */
    public Rank(String rankName, int postCount) {
        this(rankName, postCount, true);
    }
    
    /**
     * Create rank with specified posts limit, name and autoAssigned flag
     * 
     * @param rankName rank name
     * @param postCount posts limit
     * @param autoAssigned {@code true} if the Rank should be assigned to users
     * automatically, {@code false} otherwise
     */
    public Rank(String rankName, int postCount, boolean autoAssigned) {
        this.rankName = rankName;
        this.autoAssigned = autoAssigned;
        this.postCount = postCount;
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

    @Override
    public String toString() {
        return "Rank [id=" + getId() + ", rankName=" + rankName + ", autoAssigned=" + autoAssigned + ", postCount="
                + postCount + "]";
    }

}
