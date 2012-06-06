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

import static org.mockito.Mockito.*;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

/**
 * @author Alexey Grigorev
 */
public class NoPaginationTest {
    
    @Mock Query query;
    @Mock Criteria criteria;
    
    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addPaginationCriteria() {
        Pages.NONE.addPagination(criteria);
        
        verify(criteria, never()).setFirstResult(anyInt());
        verify(criteria, never()).setMaxResults(anyInt());
    }

    @Test
    public void addPaginationQuery() {
        Pages.NONE.addPagination(query);
        
        verify(query, never()).setFirstResult(anyInt());
        verify(query, never()).setMaxResults(anyInt());
    }
}
