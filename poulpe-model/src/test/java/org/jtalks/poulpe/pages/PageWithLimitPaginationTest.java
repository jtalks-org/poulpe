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
import static org.testng.Assert.assertEquals;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Alexey Grigorev
 */
public class PageWithLimitPaginationTest {
    
    @Mock Query query;
    @Mock Criteria criteria;
    
    private PageWithLimitPagination pagination;
    
    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);
        int page = 1, limit = 10;
        pagination = new PageWithLimitPagination(page, limit);
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
    
    @Test
    public void pagination0thPage() {
        int page = 0, limit = 10;
        
        PageWithLimitPagination pagination = new PageWithLimitPagination(page, limit);

        assertEquals(0, pagination.getFrom());
        assertEquals(limit, pagination.getCount());
    }

    @Test
    public void pagination1stPage() {
        int page = 1, limit = 10;
        PageWithLimitPagination pagination = new PageWithLimitPagination(page, limit);
        assertEquals(10, pagination.getFrom());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void paginationNegativePage() {
        new PageWithLimitPagination(-1, 10);
    }
}
