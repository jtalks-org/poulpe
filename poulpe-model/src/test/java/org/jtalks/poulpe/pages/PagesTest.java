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
package org.jtalks.poulpe.pages;

import static org.testng.Assert.*;
import java.util.Collections;
import java.util.List;

import org.testng.annotations.Test;

public class PagesTest {

    @Test
    public void none() {
        Pagination none = Pages.NONE;
        assertFalse(none.isNeeded());
    }

    @Test
    public void paginationIsNeeded() {
        int page = 1, limit = 10;
        Pagination pagination = Pages.paginate(page, limit);
        assertTrue(pagination.isNeeded());
    }

    @Test
    public void pagination1stPage() {
        int page = 1, limit = 10;
        Pagination pagination = Pages.paginate(page, limit);

        assertEquals(0, pagination.getFrom());
        assertEquals(limit, pagination.getCount());
    }

    @Test
    public void pagination2ndPage() {
        int page = 2, limit = 10;
        Pagination pagination = Pages.paginate(page, limit);
        assertEquals(10, pagination.getFrom());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void paginationZeroPage() {
        Pages.paginate(0, 10);
    }

    @Test
    public void nonePaginate() {
        int n = 5;
        Pagination none = Pages.NONE;

        List<Object> result = none.paginate(listOf(n));
        assertEquals(n, result.size());
    }

    private static List<Object> listOf(int n) {
        return Collections.nCopies(n, new Object());
    }

    @Test
    public void paginateFirstPage() {
        int limit = 2;
        List<Object> source = listOf(5);
        Pagination paginator = Pages.paginate(1, limit);

        List<Object> result = paginator.paginate(source);
        assertEquals(limit, result.size());
    }

    @Test
    public void paginateLastPage() {
        int limit = 2;
        List<Object> source = listOf(5);
        Pagination paginator = Pages.paginate(3, limit);

        List<Object> result = paginator.paginate(source);
        assertEquals(1, result.size());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void paginateGoneTooMuch() {
        List<Object> source = listOf(1);
        Pagination paginator = Pages.paginate(3, 2);
        paginator.paginate(source);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void paginateCantPaginateEmpty() {
        List<Object> source = Collections.emptyList();
        Pagination paginator = Pages.paginate(3, 2);
        paginator.paginate(source);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void nonePaginateCantPaginateEmpty() {
        Pages.NONE.paginate(Collections.emptyList());
    }

}
