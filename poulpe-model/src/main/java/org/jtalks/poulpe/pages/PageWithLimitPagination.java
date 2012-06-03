package org.jtalks.poulpe.pages;

import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.Validate;
import org.hibernate.Criteria;
import org.hibernate.Query;

/**
 * Used when pagination is needed. Typically invoked from
 * {@link Pages#from(int)} static factory method
 * 
 * @author Alexey Grigorev
 */
class PageWithLimitPagination implements Pagination {

    private final int from;
    private final int limit;

    PageWithLimitPagination(int page, int limit) {
        checkParams(page, limit);
        
        this.from = limit * (page - 1);
        this.limit = limit;
    }

    private static void checkParams(int page, int limit) {
        Validate.isTrue(page > 0, "page expected to be greater then zero, got %d", page);
        Validate.isTrue(limit > 0, "limit expected to be greater then zero, got %d", limit);
    }

    @Override
    public boolean isNeeded() {
        return true;
    }

    @Override
    public int getFrom() {
        return from;
    }

    @Override
    public int getCount() {
        return limit;
    }

    @Override
    public <E> List<E> paginate(List<E> source) {
        Validate.notEmpty(source);
        
        int size = source.size();
        int to = from + limit;

        if (size < to) {
            to = size;
        }

        Validate.isTrue(from <= to,
                "Cannot paginate given list, asked page cannot be reached. List of size %d, pagination %s", size, this);
        return source.subList(from, to);
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
    
    @Override
    public String toString() {
        return "PageWithLimitPagination [from=" + from + ", limit=" + limit + "]";
    }

}
