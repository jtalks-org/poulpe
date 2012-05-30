package org.jtalks.poulpe.web.controller.section.dialogs;

import org.jtalks.common.model.entity.Group;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;

/**
 * @author stanislav bashkirtsev
 */
public class GroupListTest {
    GroupList sut;

    @BeforeMethod
    public void setUp() throws Exception {
        sut = new GroupList();
    }

    @Test
    public void testGetEqual_withoutElements() throws Exception {
        assertNull(sut.getEqual(new Group()));
    }

    @Test
    public void getEqualShouldReturnNullIfNullSpecified() throws Exception {
        sut.setGroups(Arrays.asList(new Group(), new Group()));
        assertNull(sut.getEqual(null));
    }

    @Test
    public void testGetEqual() throws Exception {
        Group toGetAfterwards = new Group();
        sut.setGroups(Arrays.asList(new Group(), toGetAfterwards));
        Group toSearch = new Group();
        toSearch.setUuid(toGetAfterwards.getUuid());
        assertSame(sut.getEqual(toSearch), toGetAfterwards);
    }
}
