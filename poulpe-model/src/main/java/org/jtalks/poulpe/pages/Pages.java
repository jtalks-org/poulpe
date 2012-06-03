package org.jtalks.poulpe.pages;

/**
 * Class for building {@link Pagination} objects
 * 
 * @author Alexey Grigorev
 */
public class Pages {

    /**
     * Hides for preventing instantiation of utility-class
     */
    private Pages() {
    }

    /**
     * No pagination needed - {@link Pagination} object with
     * {@link Pagination#isNeeded()} returning {@code false}
     */
    public static final Pagination NONE = new NoPagination();

    /**
     * Creates pagination for given page and amount of items per page
     * 
     * @param page number of page
     * @param limit amount of items per page
     * @return {@link Pagination} object with {@link Pagination#isNeeded()}
     * returning {@code true}
     * @exception IllegalArgumentException if page not greater then zero
     */
    public static Pagination paginate(int page, int limit) {
        return new PageWithLimitPagination(page, limit);
    }

}
