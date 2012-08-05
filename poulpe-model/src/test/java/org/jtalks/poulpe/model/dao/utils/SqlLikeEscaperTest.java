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
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * @author Anton Kolyaev
 */
public class SqlLikeEscaperTest {

    @Test
    public void shouldEscapePercent() throws Exception {
        assertEquals("\\%", SqlLikeEscaper.escapeControlCharacters("%"));
    }

    @Test
    public void shouldEscapeUnderscore() throws Exception {
        assertEquals("\\_", SqlLikeEscaper.escapeControlCharacters("_"));
    }

    @Test
    public void shouldEscapeExclamation() throws Exception {
        assertEquals("\\!", SqlLikeEscaper.escapeControlCharacters("!"));
    }

    /**
     * Don't know how this sign is called..
     */
    @Test
    public void shouldEscapeRoof() {
        assertEquals("\\^", SqlLikeEscaper.escapeControlCharacters("^"));
    }

    @Test
    public void shouldEscapeLeftBrace() throws Exception {
        assertEquals("\\[", SqlLikeEscaper.escapeControlCharacters("["));
    }

    @Test
    public void shouldEscapeRightBrace() throws Exception {
        assertEquals("\\]", SqlLikeEscaper.escapeControlCharacters("]"));
    }

    @Test
    public void shouldNotEscapeEmptyString(){
        assertEquals("", SqlLikeEscaper.escapeControlCharacters(""));
    }

    @Test
    public void shouldDoNothingOnNull() throws Exception {
        assertNull(SqlLikeEscaper.escapeControlCharacters(null));
    }

    @Test
    public void shouldNotEscapeAnything() throws Exception {
        assertEquals("abc", SqlLikeEscaper.escapeControlCharacters("abc"));
    }

    @Test
    public void shouldEscapeControlCharactersWhileTextIsInside() throws Exception {
        assertEquals("\\^\\%\\!\\_\\[\\]abc\\^\\%\\!\\_\\[\\]", SqlLikeEscaper.escapeControlCharacters("^%!_[]abc^%!_[]"));
    }

    @Test
    public void shouldEscapeControlCharactersInsideText() throws Exception {
        assertEquals("abc\\^\\%\\!\\_\\[\\]abc", SqlLikeEscaper.escapeControlCharacters("abc^%!_[]abc"));
    }
}
