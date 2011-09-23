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

import java.util.List;

import org.jtalks.poulpe.model.entity.Section;

/**
 * @author Bekrenev Dmitry
 * 
 *         This interface for representation view delete section dialog
 * */
public interface DeleteSectionDialogView {

    /**
     * Get victim section
     * 
     * @return Section which will be deleted
     * */
    Section getDeleteSection();

    /**
     * Get selected section in combobox
     * 
     * @return Section which will be recipient
     * */
    Section getSelectedSection();

    /**
     * Get delete mode
     * 
     * @return String deleting mode. Now available only 2 modes: "deleteAll" -
     *         which mean delete section with she branches. "deleteAndMove" -
     *         which mean delete section and move branches in recipient section
     * */
    String getDeleteMode();

    /**
     * Cause showing dialog
     * 
     * */
    void showDialog();

    /**
     * Cause close dialog
     * */
    void closeDialog();

    /**
     * Initialize combobox available section
     * 
     * @param selectableSections
     *            list available sections
     * */
    void initSectionList(List<Section> selectableSections);
}
