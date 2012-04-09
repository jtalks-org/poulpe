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

/**
 * Interface for implementing visitor pattern for {@link PoulpeBranch} and
 * {@link PoulpeSection} classes. Mainly for using in PoulpeSection view where, depending on
 * selected element, needed actions should be performed.<br>
 * <br>
 * 
 * This interface should be implemented by all visitors, applicable to
 * {@link PoulpeBranch} and {@link PoulpeSection}
 * 
 * @author Alexey Grigorev
 * 
 */
public interface BranchSectionVisitor {

    /**
     * Performs an action when the passed element is {@link PoulpeSection}
     * 
     * @param section to be processed
     */
    void visitSection(PoulpeSection section);

    /**
     * Performs an action when the passed element is {@link PoulpeBranch}
     * 
     * @param branch to be processed
     */
    void visitBranch(PoulpeBranch branch);

}
