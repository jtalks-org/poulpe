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

/**
 * Enum for representing types of section deleting. Now available only 2 modes:<br>
 * 
 * - <b>DELETE_ALL</b> - which mean delete section and all its branches.<br>
 * - <b>DELETE_AND_MOVE</b> - which mean delete section and move its branches to
 * a recipient section.<br>
 * 
 * @author Alexey Grigorev
 */
public enum SectionDeleteMode {
    /**
     * Delete section with its branches
     */
    DELETE_ALL("deleteAll"),

    /**
     * Delete section and move its branches to a recipient section
     */
    DELETE_AND_MOVE("deleteAndMove");

    private final String mode;

    private SectionDeleteMode(String mode) {
        this.mode = mode;
    }

    /**
     * Available modes: "deleteAll" and "deleteAndMove" 
     * 
     * @param mode string to be converted to enum
     * @return proper enum value
     */
    public static SectionDeleteMode fromString(String mode) {
        if (DELETE_ALL.mode.equals(mode)) {
            return DELETE_ALL;
        } else {
            return DELETE_AND_MOVE;
        }
    }
}
