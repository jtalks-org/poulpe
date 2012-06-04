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

import static org.mockito.Mockito.verify;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PageWithLimitPaginationTest {
    
    @Mock Query query;
    @Mock Criteria criteria;
    
    private Pagination pagination;
    private final int page = 1;
    private final int limit = 10;
    
    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);
        pagination = Pages.paginate(page, limit);
    }

    @Test
    public void addPaginationCriteria() {
        pagination.addPagination(criteria);
        
        verify(criteria).setFirstResult(pagination.getFrom());
        verify(criteria).setMaxResults(pagination.getCount());
    }

    @Test
    public void addPaginationQuery() {
        pagination.addPagination(query);
        
        verify(query).setFirstResult(pagination.getFrom());
        verify(query).setMaxResults(pagination.getCount());
    }
}
