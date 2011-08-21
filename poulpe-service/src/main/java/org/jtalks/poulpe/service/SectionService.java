/**
 * Copyright (C) 2011  jtalks.org Team
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
 * Also add information on how to contact you by electronic and paper mail.
 * Creation date: Apr 12, 2011 / 8:05:19 PM
 * The jtalks.org Project
 */
package org.jtalks.poulpe.service;

import java.util.List;

import org.jtalks.poulpe.model.entity.Section;
import org.jtalks.poulpe.service.exceptions.NotUniqueException;

public interface SectionService extends EntityService<Section> {

    /**
     * Get list of all Sections.
     * @return - list of Sections.
     */
    List<Section> getAll();

    // TODO WTF It doesn't work when has child branches, so why is it here? Remove it or describe
    // why it is necessary.
    // Marked as deprecated meanwhile. (sukharevd, 2011/08/21 02:49)
    /**
     * Mark the section as deleted.
     * @param section section to delete
     * @deprecated use other two delete methods instead
     */
    boolean deleteSection(Section section);

    /**
     * Save or update section.
     * @param section instance to save
     * @throws NotUniqueException if section with the same name already exists
     */
    void saveSection(Section section) throws NotUniqueException;

    /**
     * Check if section with given name exists.
     * @param section
     * @return true if exists
     */
    boolean isSectionExists(String section);

    /**
     * Removes the section and all its branches.
     * @param victim the removed section
     * @return {@code true} if section was removed, {@code false} otherwise
     */
    public boolean deleteRecursively(Section victim);

    /**
     * Removes the section and move all its branches to another section.
     * @param victim the removed section
     * @param recipient the section that will take orphan branches
     * @return {@code true} if the section was removed, {@code false} otherwise
     * @throws IllegalArgumentException when {@code victim}'s ID equals to {@code recipient}'s one
     *             (victim and recipient can't be the same section)
     */
    public boolean deleteAndMoveBranchesTo(Section victim, Section recipient);

}
