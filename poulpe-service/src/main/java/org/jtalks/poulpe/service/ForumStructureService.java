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

import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;

/**
 * A coarse-grained service to work with forum structure (sections, branches).
 *
 * @author stanislav bashkirtsev
 * @author Guram Savinov
 */
public interface ForumStructureService {
    void saveJcommune(Jcommune jcommune);

    /**
     * Returns a JCommune instance by the specified ID (there can be several such instances).
     *
     * @return the instance of JCommune or {@code null} if no JCommune instance is in database
     */
    Jcommune getJcommune();

    void removeBranch(PoulpeBranch branch);

    void moveBranch(PoulpeBranch branch, PoulpeSection toSection);

    void moveBranch(PoulpeBranch branch, PoulpeBranch target);

    /**
     * Deletes the specified section from its JCommune instance and returns updated JCommune. Results in no-op if there
     * is no such section in DB.
     *
     * @param section a section to be removed from the database
     * @return the updated JCommune
     */
    Jcommune deleteSectionWithBranches(PoulpeSection section);

    void deleteSectionAndMoveBranches(PoulpeSection toRemove, PoulpeSection toReceiveBranches);

    PoulpeBranch saveBranch(PoulpeSection inSection, PoulpeBranch notYetSavedBranch);

    void deleteBranch(PoulpeBranch branch);
}
