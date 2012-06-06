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

import javax.annotation.Nonnull;

import org.apache.commons.lang3.Validate;
import org.hibernate.Criteria;
import org.hibernate.Query;

/**
 * Used when pagination is needed. Typically invoked from {@link Pages#from(int)} static factory method
 * 
 * @author Alexey Grigorev
 */
class PageWithLimitPagination implements Pagination {

    private final int from;
    private final int limit;

    /**
     * For instantiation, use {@link Pages#paginate(int, int)}
     * @param page page number
     * @param limit maximal amount of items per page
     */
    PageWithLimitPagination(int page, int limit) {
        checkParams(page, limit);

        this.from = limit * page;
        this.limit = limit;
    }

    private static void checkParams(int page, int limit) {
        Validate.isTrue(page >= 0, "page must not be negative, got %d", page);
        Validate.isTrue(limit > 0, "limit expected to be greater then zero, got %d", limit);
    }

    @Override
    public Criteria addPagination(@Nonnull Criteria criteria) {
        Validate.notNull(criteria);
        criteria.setFirstResult(from);
        return criteria.setMaxResults(limit);
    }

    @Override
    public Query addPagination(@Nonnull Query query) {
        Validate.notNull(query);
        query.setFirstResult(from);
        return query.setMaxResults(limit);
    }

    public int getFrom() {
        return from;
    }

    public int getCount() {
        return limit;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PageWithLimitPagination) {
            PageWithLimitPagination other = (PageWithLimitPagination) obj;
            return other.from == from && other.limit == limit;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return from + limit;
    }

    @Override
    public String toString() {
        return "PageWithLimitPagination [from=" + from + ", limit=" + limit + "]";
    }

}
