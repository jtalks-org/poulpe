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
package org.jtalks.poulpe.service;

import java.util.List;

import org.jtalks.common.model.entity.Rank;
import org.jtalks.common.service.EntityService;

/**
 *
 * @author Temdegon
 */
public interface RankService extends EntityService<Rank> {

    /**
     * Get all ranks.
     * @return the list of the ranks
     */
    List<Rank> getAll();

    /**
     * Delete the specified rank.
     * @param rank rank to delete
     */
    void deleteRank(Rank rank);

    /**
     * Save new or update existent rank.
     * 
     * @param rank rank to save
     */
    void saveRank(Rank rank);
}
