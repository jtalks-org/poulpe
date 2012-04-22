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

import org.jtalks.common.model.entity.Section;

import java.util.List;

/**
 * Forum section that contains branches.
 *
 * @author Tatiana Birina
 * @author Guram Savinov
 */
public class PoulpeSection extends Section {
    /**
     * Default constructor, creates a section with empty list of branches
     */
    public PoulpeSection() {
        super();
    }

    /**
     * Creates a section with empty list of branches setting section a name
     *
     * @param name for new section
     */
    public PoulpeSection(String name) {
        super(name);
    }

    /**
     * Should be used if preference of {@link #getBranches()}
     *
     * @return list of {@link PoulpeBranch} objects
     */
    @SuppressWarnings("unchecked")
    public List<PoulpeBranch> getPoulpeBranches() {
        List<?> branches = getBranches();
        return (List<PoulpeBranch>) branches;
    }

    /**
     * Constructor with name and description, creates a section with empty list of branches
     *
     * @param name        - name for new section
     * @param description - description for new section
     */
    public PoulpeSection(String name, String description) {
        super(name, description);
    }

    /**
     * Unlike usual situation, in our case this method is used by presentation layer to depict forum structure, so this
     * method should be changed only if this presentation changed the way it shows branches.
     *
     * @return {@link #getName()}
     */
    @Override
    public String toString() {
        return this.getName();
    }
}
