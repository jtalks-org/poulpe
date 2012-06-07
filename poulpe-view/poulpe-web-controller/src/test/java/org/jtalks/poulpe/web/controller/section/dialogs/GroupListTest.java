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
