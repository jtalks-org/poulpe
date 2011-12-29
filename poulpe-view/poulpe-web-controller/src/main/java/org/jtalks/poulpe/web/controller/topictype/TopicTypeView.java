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
package org.jtalks.poulpe.web.controller.topictype;

import org.jtalks.poulpe.validation.ValidationResult;

/**
 * View interface for TopicType
 * @author unascribed
 * @see TopicTypeListPresenter
 */
public interface TopicTypeView {

    /**
     * Shows type title
     */
    void showTypeTitle(String title);

    /**
     * Shows type description
     */
    void showTypeDescription(String description);

    /**
     * Gets type title
     */
    String getTypeTitle();
    
    /**
     * Gets type description
     */
    String getTypeDescription();
    
    /**
     * Hide edit action
     */
    void hideEditAction();
    
    /**
     * Hide create action
     */
    void hideCreateAction();
    
    /**
     * Show error popup dialog
     * @param label for dialog
     */
    void openErrorPopupInTopicTypeDialog(String label);

    /**
     * @param result
     */
    void validationFailure(ValidationResult result);
}