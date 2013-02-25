package org.jtalks.poulpe.model.sorting;

import org.jtalks.poulpe.model.pages.Pages;
import org.jtalks.poulpe.model.pages.Pagination;

/**
 * Request with parameters of sorting for users.
 */
public class UserSortingRequest extends AbstractSortingRequest{

    /**
     * Constructor
     */
    public UserSortingRequest() {
    }

    /**
     * Constructor
     * @param ascending sorting by ascending or not
     * @param pagination pagination of request sorting
     * @param column column that will be sorted
     * @param searchString string to search
     */
    public UserSortingRequest(boolean ascending, Pagination pagination,
                              String column, String searchString) {
        setAscending(ascending);
        setPagination(pagination);
        setColumn(column);
        setSearchString(searchString);
    }

    /**
     * Constructor
     * @param ascending sorting by ascending or not
     * @param page number of page
     * @param itemsPerPage items per page
     * @param column column that will be sorted
     * @param searchString string to search
     */
    public UserSortingRequest(boolean ascending, int page, int itemsPerPage,
                              String column, String searchString){
        setAscending(ascending);
        setPagination(Pages.paginate(page, itemsPerPage));
        setColumn(column);
        setSearchString(searchString);

    }


}
