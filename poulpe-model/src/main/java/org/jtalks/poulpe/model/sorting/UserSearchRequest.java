package org.jtalks.poulpe.model.sorting;

import org.jtalks.poulpe.model.pages.Pages;
import org.jtalks.poulpe.model.pages.Pagination;

/**
 * Request with parameters of sorting for users.
 */
public class UserSearchRequest extends AbstractSortingRequest{

    /**
     * Searching with sorting by username
     */
    public static final String BY_USERNAME = "username";

    /**
     * Searching with sorting by email
     */
    public static final String BY_EMAIL = "email";

    /**
     * Searching with sorting by first name
     */
    public static final String BY_FIRST_NAME = "firstName";

    /**
     * Searching with sorting by last name
     */
    public static final String BY_LAST_NAME = "lastName";

    /**
     * Searching with sorting by user's role
     */
    public static final String BY_ROLE = "role";



    /**
     * Constructor
     */
    public UserSearchRequest() {
    }

    /**
     * Constructor
     * @param ascending sorting by ascending or not
     * @param pagination pagination of request sorting
     * @param column column that will be sorted
     * @param searchString string to search
     */
    public UserSearchRequest(boolean ascending, Pagination pagination,
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
    public UserSearchRequest(boolean ascending, int page, int itemsPerPage,
                             String column, String searchString){
        setAscending(ascending);
        setPagination(Pages.paginate(page, itemsPerPage));
        setColumn(column);
        setSearchString(searchString);

    }


}
