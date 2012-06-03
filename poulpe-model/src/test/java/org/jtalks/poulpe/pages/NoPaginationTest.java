package org.jtalks.poulpe.pages;

import static org.mockito.Mockito.*;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

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
