/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jtalks.poulpe.web.controller;

import java.util.List;

import org.jtalks.poulpe.model.entity.PoulpeUser;

/**
 * The interface represents the manager for showing different types of dialog
 * messages.
 * 
 * @author Dmitriy Sukharev
 */
public interface DialogManager {

    /**
     * Notifies user using {@code localeMessage} message.
     * 
     * @param localeMessage i18n key whose value should be shown as a message
     */
    void notify(String localeMessage);

    /**
     * Asks user if they want to delete item.
     * 
     * @param victim the item to be deleted
     * @param performable the action to be performed when user confirms item
     * deletion
     */
    void confirmDeletion(String victim, Performable performable);

    /**
     * Asks user if they want to create item
     * 
     * @param target the item to be created
     * @param performable the action to be performed when user confirms item
     * deletion
     */
    void confirmCreation(String target, Performable performable);

    /**
     * Asks user if they want to edit item
     * 
     * @param target the item to be edit
     * @param performable the action to be performed when user confirms item
     * deletion
     */
    void confirmEdition(String target, Performable performable);

    /**
     * Asks user if they want to delete list items.
     * 
     * @param victimList the list items to be deleted
     * @param performable the action to be performed when user confirms item
     * deletion
     */
    void confirmDeletion(List<String> victimList, Performable performable);

    /**
     * Asks to ban specified users
     * 
     * @param usersToBan list of users to ban
     * @param reason of banning
     * @param performable action after confirming
     */
    void confirmBan(List<PoulpeUser> usersToBan, String reason, Performable performable);

    /**
     * The interface for storing some actions that ought to be performed when
     * user confirms them.
     * 
     * @author Dmitriy Sukharev
     * 
     */
    interface Performable {
        /**
         * The actions to be executed after the user confirms that he/she wants
         * them to be executed.
         */
        void execute();
    }
}
