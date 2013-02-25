package org.jtalks.poulpe.model.sorting;

import org.jtalks.poulpe.model.pages.Pagination;

/**
 * Abstract class of request with parameters of sorting.
 */
public abstract class AbstractSortingRequest {

    /**
     * Soring by ascending
     */
    private boolean ascending = true;

    /**
     * Pagination of request
     */
    private Pagination pagination;

    /**
     * Column for sorting
     */
    private String column;

    /**
     * String to search
     */
    private String searchString;

    /**
     * @return sorting by ascending or not
     */
    public boolean isAscending() {
        return ascending;
    }

    /**
     * @param ascending sorting by ascending or not
     */
    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    /**
     * @return pagination of request sorting
     */
    public Pagination getPagination() {
        return pagination;
    }

    /**
     * @param pagination pagination of request sorting
     */
    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    /**
     * @return column that will be sorted
     */
    public String getColumn() {
        return column;
    }

    /**
     * @param column column that will be sorted
     */
    public void setColumn(String column) {
        this.column = column;
    }

    /**
     * @return string to search
     */
    public String getSearchString() {
        return searchString;
    }

    /**
     * @param searchString string to search
     */
    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
}
