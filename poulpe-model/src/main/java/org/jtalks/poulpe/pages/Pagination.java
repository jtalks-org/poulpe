package org.jtalks.poulpe.pages;

import java.util.List;

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
     * @return {@code true} if pagination is needed, {@code false} otherwise
     */
    boolean isNeeded();

    /**
     * @return item from which the pagination should start
     * @exception UnsupportedOperationException if pagination is not needed
     */
    int getFrom();

    /**
     * @return amount of items after the 'from' index
     * @exception UnsupportedOperationException if pagination is not needed
     */
    int getCount();

    /**
     * Applies this pagination for given list. Note: changes applied to result
     * may change the original list
     * 
     * @param source list to be paginated
     * @return sublist of the given list
     * @exception IllegalArgumentException if this pagination is not applicable
     * (say, when trying to paginate list with not enough pages)
     */
    <E> List<E> paginate(List<E> source);
    
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
