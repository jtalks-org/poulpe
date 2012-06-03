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