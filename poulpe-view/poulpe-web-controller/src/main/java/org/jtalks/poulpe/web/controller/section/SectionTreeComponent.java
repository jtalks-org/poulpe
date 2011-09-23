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
package org.jtalks.poulpe.web.controller.section;

import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Section;

/**
 * @author Konstantin Akimov
 * */
public interface SectionTreeComponent {

    /**
     * add branch to view
     * 
     * @param branch
     *            instance branch witch will added in view
     * */
    void addBranchToView(Branch branch);

    /**
     * remove branch from view
     * 
     * @param branch
     *            instance branch witch will deleted from view
     * */
    void removeBranchFromView(Branch branch);

    /**
     * update branch in view
     * 
     * @param branch
     *            witch will updated in view
     * */
    void updateBranchInView(Branch branch);

    /**
     * update section in view
     * 
     * @param section
     *            witch will updated in view
     * */
    void updateSectionInView(Section section);

    /**
     * get selected object
     * 
     * @return branch or section
     * */
    Object getSelectedObject();
}
