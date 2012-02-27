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
package org.jtalks.poulpe.model.dao;

import java.util.List;
import org.jtalks.common.model.dao.ParentRepository;
import org.jtalks.common.model.entity.Rank;

/**
 * Dao for the forum rank {@link Rank}.
 * 
 * @author Pavel Vervenko
 */
public interface RankDao extends ParentRepository<Rank> {

    /**
     * Get the list of all ranks.
     * 
     * @return ranks list
     */
    List<Rank> getAll();

    /**
     * Check if the name is already taken.
     * 
     * @param name name to check
     * @return true if exists
     * @deprecated use validator for that
     */
    @Deprecated
    boolean isRankNameExists(String name);
}
