package org.jtalks.poulpe.web.controller.users;

import java.util.List;

import org.jtalks.common.model.entity.User;

/**
 * View to display a list of
 * {@link org.jtalks.common.model.entity.User}
 * 
 * @author Vytautas Kashchuk 
 */
public interface UsersListView {

    /**
     * Show users list.
     * <ul>
     * <li>
     * Each row should show username, email, first name, last name and role of User .</li>
     * <li>
     * If user double click on the existing user event should be directed to
     * the presenter</li>
     * <li>
     * Multiple selections should be prohibited.</li>
     * </ul>
     * 
     * @param list of #{@link org.jtalks.common.model.entity.User}
     */
    void showUsersList(List<User> list);
    
    void showSearchString(String searchString);

    String getSearchString();
}