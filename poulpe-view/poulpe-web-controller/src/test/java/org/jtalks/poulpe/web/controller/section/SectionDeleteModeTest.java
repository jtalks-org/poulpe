package org.jtalks.poulpe.web.controller.section;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 * @author Alexey Grigorev
 */
public class SectionDeleteModeTest {

    @Test
    public void deleteAll() {
        SectionDeleteMode actual = SectionDeleteMode.fromString("deleteAll");
        assertEquals(actual, SectionDeleteMode.DELETE_ALL);
    }
    
    @Test
    public void deleteAndMove() {
        SectionDeleteMode actual = SectionDeleteMode.fromString("deleteAndMove");
        assertEquals(actual, SectionDeleteMode.DELETE_AND_MOVE);
    }
}
