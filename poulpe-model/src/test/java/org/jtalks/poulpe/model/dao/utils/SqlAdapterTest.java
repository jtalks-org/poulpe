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
package org.jtalks.poulpe.model.dao.utils;

import org.testng.AssertJUnit;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Anton Kolyaev
 * Date: 27.07.12
 * Time: 9:03
 * @author Anton Kolyaev
 */
public class SqlAdapterTest  {

    @Test
    public void test1EscapeCtrlCharacters() throws Exception {
            AssertJUnit.assertEquals("Query is Ok", "\\%", SqlAdapter.escapeCtrlCharacters("%"));
    }

    @Test
    public void test2EscapeCtrlCharacters() throws Exception {
            AssertJUnit.assertEquals("Query is Ok", "\\_", SqlAdapter.escapeCtrlCharacters("_"));
    }
    @Test
    public void test3EscapeCtrlCharacters() throws Exception {
            AssertJUnit.assertEquals("Query is Ok", "\\!", SqlAdapter.escapeCtrlCharacters("!"));
    }
    @Test
    public void test4EscapeCtrlCharacters() throws Exception {
            AssertJUnit.assertEquals("Query is Ok", "\\^", SqlAdapter.escapeCtrlCharacters("^"));
    }
    @Test
    public void test5EscapeCtrlCharacters() throws Exception {
            AssertJUnit.assertEquals("Query is Ok", "\\[", SqlAdapter.escapeCtrlCharacters("["));
    }
    @Test
    public void test6EscapeCtrlCharacters() throws Exception {
            AssertJUnit.assertEquals("Query is Ok", "\\]", SqlAdapter.escapeCtrlCharacters("]"));
    }
    @Test
    public void test7EscapeCtrlCharacters() throws Exception {
            AssertJUnit.assertEquals("Query is Ok", "abc", SqlAdapter.escapeCtrlCharacters("abc"));
    }
    @Test
    public void test8EscapeCtrlCharacters() throws Exception {
            AssertJUnit.assertEquals("Query is Ok", "\\^\\%\\!\\_\\[\\]abc\\^\\%\\!\\_\\[\\]", SqlAdapter.escapeCtrlCharacters("^%!_[]abc^%!_[]"));
    }

    @Test
    public void test9EscapeCtrlCharacters() throws Exception {
            AssertJUnit.assertEquals("Query is Ok", "abc\\^\\%\\!\\_\\[\\]abc", SqlAdapter.escapeCtrlCharacters("abc^%!_[]abc"));
    }
}
