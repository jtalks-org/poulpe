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
import org.jtalks.poulpe.model.entity.PoulpeSection;

/**
 * @author tanya birina
 * @author Dmitriy Sukharev
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
     * Method to check if section name is already exists.
     * 
     * @param section
     *            name for check
     * @return true if section with such name already exists
     */
    boolean isSectionNameExists(PoulpeSection section);

    /**
     * Removes the section and all its branches.
     * 
     * @param id
     *            the identifier of the removed section
     * @return {@code true} if section was removed, {@code false} otherwise
     */
    boolean deleteRecursively(PoulpeSection section);

    /**
     * Removes the section and move all its branches to another section.
     * 
     * @param victimId
     *            the identifier of the removed section
     * @param recipientId
     *            the identifier of the section that will take branches
     * @return {@code true} if section was removed, {@code false} otherwise
     */
    boolean deleteAndMoveBranchesTo(PoulpeSection victim, PoulpeSection recipient);

}
