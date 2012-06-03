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
