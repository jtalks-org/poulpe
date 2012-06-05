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

import org.hibernate.Criteria;
import org.hibernate.Query;

/**
 * Class for paginating a result. If is not needed, {@link #getFrom()} and
 * {@link #getCount()} may throw {@link UnsupportedOperationException}
 * 
 * @author Alexey Grigorev
 */
public interface Pagination {

    /**
     * Adds pagination to hibernate's ctiteria
     * 
     * @param criteria to paginate
     * @return paginated criteria
     */
    Criteria addPagination(Criteria criteria);
    
    /**
     * Adds pagination to hibernate's query
     * 
     * @param query to paginate
     * @return paginated query
     */
    Query addPagination(Query query);

}
