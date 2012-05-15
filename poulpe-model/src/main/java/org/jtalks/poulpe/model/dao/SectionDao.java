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

import org.jtalks.common.model.dao.ParentRepository;
import org.jtalks.poulpe.model.entity.PoulpeSection;

import java.util.List;

/**
 * Dao interface for accessing {@link PoulpeSection} objects
 *
 * @author tanya birina
 * @author Vahluev Vyacheslav
 */
public interface SectionDao extends ParentRepository<PoulpeSection> {

    /**
     * Get the list of all sections.
     *
     * @return list of sections
     */
    List<PoulpeSection> getAll();

    /**
     * Removes the section and all its branches.
     *
     * @param section a section to be removed with its branches
     * @return {@code true} if section was removed, {@code false} otherwise
     */
    boolean deleteRecursively(PoulpeSection section);

    /**
     * Removes the section and move all its branches to another section.
     *
     * @param victim    the section to be removed
     * @param recipient the section to move all the victim's branches to
     * @return {@code true} if section was removed, {@code false} otherwise
     */
    boolean deleteAndMoveBranchesTo(PoulpeSection victim, PoulpeSection recipient);

}
