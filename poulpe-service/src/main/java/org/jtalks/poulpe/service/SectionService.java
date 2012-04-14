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

import org.jtalks.common.service.EntityService;
import org.jtalks.poulpe.model.entity.PoulpeSection;

import java.util.List;

/**
 * Service for dealing with {@link PoulpeSection} objects
 * 
 * @author unascribed
 */
public interface SectionService extends EntityService<PoulpeSection> {

    /**
     * Get list of all Sections.
     * 
     * @return - list of all {@link PoulpeSection} objects.
     */
    List<PoulpeSection> getAll();

    /**
     * Save or update section.
     * 
     * @param section instance to save
     */
    void saveSection(PoulpeSection section);

    /**
     * Removes the section and all its branches.
     * 
     * @param victim the removed section
     * @return {@code true} if section was removed, {@code false} otherwise
     */
    boolean deleteRecursively(PoulpeSection victim);

    /**
     * Removes the section and move all its branches to another section.
     * 
     * @param victim the removed section
     * @param recipient the section that will take orphan branches
     * @return {@code true} if the section was removed, {@code false} otherwise
     */
    boolean deleteAndMoveBranchesTo(PoulpeSection victim, PoulpeSection recipient);

}
