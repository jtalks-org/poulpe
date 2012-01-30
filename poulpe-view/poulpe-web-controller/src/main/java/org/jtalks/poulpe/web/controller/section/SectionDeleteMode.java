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

    public static SectionDeleteMode fromString(String mode) {
        if (DELETE_ALL.mode.equals(mode)) {
            return DELETE_ALL;
        } else {
            return DELETE_AND_MOVE;
        }
    }
}
