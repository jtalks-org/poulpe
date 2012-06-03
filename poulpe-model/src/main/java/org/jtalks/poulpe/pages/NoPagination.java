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

import java.util.List;

import org.apache.commons.lang3.Validate;
import org.hibernate.Criteria;
import org.hibernate.Query;

/**
 * Used when no pagination is needed. Typically should be accessed via
 * {@link Pages#NONE}
 * 
 * @author Alexey Grigorev
 */
class NoPagination implements Pagination {

    @Override
    public boolean isNeeded() {
        return false;
    }

    @Override
    public int getFrom() {
        throw new UnsupportedOperationException("getFrom() cannot be invoked on NoPagination object. "
                + "Make sure to make a prior call to 'isNeeded()' method");
    }

    @Override
    public int getCount() {
        throw new UnsupportedOperationException("getCount() cannot be invoked on NoPagination object. "
                + "Make sure to make a prior call to 'isNeeded()' method");
    }

    @Override
    public <E> List<E> paginate(List<E> source) {
        return Validate.notEmpty(source);
    }

    @Override
    public Criteria addPagination(Criteria criteria) {
        return criteria;
    }

    @Override
    public Query addPagination(Query query) {
        return query;
    }

}