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

/**
 * Class for building {@link Pagination} objects which are used for paginating hql queries and criteria.<br>
 * <br>
 * 
 * Use {@link #NONE} when you don't need pagination, and {@link #paginate(int, int)} for specifying a page you need.
 * 
 * @author Alexey Grigorev
 * @see Pagination
 */
public class Pages {

    /**
     * Hidden for preventing instantiation of utility-class.
     */
    private Pages() {
    }

    /**
     * No pagination needed - {@link Pagination} which doesn't paginate queries and criteria.
     */
    public static final Pagination NONE = new NoPagination();

    /**
     * Creates pagination for given page and amount of items per page.
     * 
     * Provided with this data, it internally calculates the item from which to query the base
     * and the maximal amount of items returned per one query.
     * 
     * @param page number of page, starting from zero
     * @param limit amount of items per page
     * @return {@link Pagination} object
     * @exception IllegalArgumentException if page is negative
     */
    public static Pagination paginate(int page, int limit) {
        return new PageWithLimitPagination(page, limit);
    }

}
