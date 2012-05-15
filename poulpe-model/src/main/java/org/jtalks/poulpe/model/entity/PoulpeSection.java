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
import org.jtalks.common.model.entity.Section;

import javax.annotation.Nullable;
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
     * Constructor with name and description, creates a section with empty list of branches
     *
     * @param name        - name for new section
     * @param description - description for new section
     */
    public PoulpeSection(String name, String description) {
        super(name, description);
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
     * Unlike usual situation, in our case this method is used by presentation layer to depict forum structure, so this
     * method should be changed only if this presentation changed the way it shows branches.
     *
     * @return {@link #getName()}
     */
    @Override
    public String toString() {
        return this.getName();
    }

    /**
     * Removes all the branches from the section.
     */
    public void clearBranches() {
        this.getBranches().clear();
    }

    /**
     * Returns the amount of branches inside of the section.
     *
     * @return the amount of branches inside of the section
     */
    public int getAmountOfBranches() {
        return getBranches().size();
    }

    /**
     * Returns the branch that is located at the specified index in the list of branches.
     *
     * @param index the index of branch to return
     * @return a branch that is at the specified position in the list of branches
     * @throws IndexOutOfBoundsException if the specified index is {@code branches.size() < index < 0}
     */
    public PoulpeBranch getBranch(int index) {
        return (PoulpeBranch) getBranches().get(index);
    }

    /**
     * Defines whether such branch is already in the list of branches of the section.
     *
     * @param branch a branch to check whether it's already in the list
     * @return {@code true} if the branch is already in the list, {@code false} if it's not. If specified branch is
     *         {@code null}, then the result is <i>always</i> {@code false}.
     */
    public boolean containsBranch(@Nullable Branch branch) {
        return getBranches().contains(branch);
    }

    /**
     * Gets the last branch in the list of branches of the section.
     *
     * @return the last branch in the section or {@code null} if there are no branches
     */
    public PoulpeBranch getLastBranch() {
        if (getBranches().size() == 0) {
            return null;
        }
        return (PoulpeBranch) getBranches().get(getBranches().size() - 1);
    }

    /**
     * Adds a new branch to the list of branches and sets its section to current one. Results in no-op if the branch is
     * {@code null} or it's already in the list.
     *
     * @param branch a branch to be added to the list of branches in section
     */
    public void addBranchIfAbsent(@Nullable Branch branch) {
        List<Branch> branches = getBranches();
        if (branch != null && !branches.contains(branch)) {
            branches.add(branch);
            branch.setSection(this);
        }
    }
}
