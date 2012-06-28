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

import org.jtalks.common.model.entity.Branch;
import org.jtalks.common.model.entity.Group;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;


/**
 * Forum branch that contains topics.
 *
 * @author Pavel Vervenko
 */
public class PoulpeBranch extends Branch {
    /**
     * Creates an empty branch, all fields are set to null,
     */
    public PoulpeBranch() {
    }

    /**
     * Create PoulpeBranch with name and empty description
     *
     * @param name - name for new PoulpeBranch
     */
    public PoulpeBranch(String name) {
        super(name, "");
    }

    public PoulpeBranch(String name, String description) {
        super(name, description);
    }

    /**
     * Moves the branch from one section to another, which effectively means removing it from the old one and adding to
     * another. If the target section is the same as the old one, then nothing happens.
     *
     * @param target a section to add branch to
     * @return the old section where branch was in or {@code null} if it's a new branch and wasn't added anywhere yet
     */
    public PoulpeSection moveTo(@Nonnull PoulpeSection target) {
        PoulpeSection removeFrom = (PoulpeSection) getSection();
        if (!target.equals(removeFrom)) {
            removeFrom.deleteBranch(this);
            target.addOrUpdateBranch(this);
            this.setSection(target);
        }
        return removeFrom;
    }

    /**
     * {@inheritDoc}<br/> Overriden in Poulpe in order to put Bean Validation annotation onto field.
     */
    @NotNull(message = "{sections.moderating_group.not_null_constraint}")
    @Override
    public Group getModeratorsGroup() {
        return super.getModeratorsGroup();
    }

    /**
     * Should be used in preference of {@link #getSection()}
     *
     * @return {@link PoulpeSection}
     */
    public PoulpeSection getPoulpeSection() {
        return (PoulpeSection) getSection();
    }

    /**
     * Unlike usual situation, in our case this method is used by presentation layer to depict forum structure, so this
     * method should be changed only if this presentation changed the way it shows branches.
     *
     * @return {@link #getName()}
     */
    @Override
    public String toString() {
        return getName();
    }

    /**
     * Removes the branch from its current section and sets the current section to null. Does nothing if there is no
     * section associated with the branch.
     *
     * @return the section branch was in or null if there was no section
     */
    public PoulpeSection removeFromSection() {
        PoulpeSection section = getPoulpeSection();
        if (section != null) {
            section.deleteBranch(this);
            setSection(null);
        }
        return section;
    }
}
