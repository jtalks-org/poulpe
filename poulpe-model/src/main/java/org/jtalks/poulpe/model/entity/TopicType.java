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
package org.jtalks.poulpe.model.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.jtalks.common.model.entity.Entity;
import org.jtalks.poulpe.validation.annotations.UniqueConstraint;
import org.jtalks.poulpe.validation.annotations.UniqueField;

/**
 * Represent topic types on the page of general configuration
 * 
 * @author Pavel Vervenko
 * @author Alexey Grigorev
 */
@UniqueConstraint
public class TopicType extends Entity {

    public static final String TITLE_CANT_BE_VOID = "topictypes.error.topictype_name_cant_be_void";
    public static final String TITLE_ALREADY_EXISTS = "topictypes.error.topictype_name_already_exists";
    public static final String ERROR_LABEL_SECTION_NAME_WRONG = "sections.editsection.name.err";
    
    @NotNull(message = TITLE_CANT_BE_VOID)
    @NotEmpty(message = TITLE_CANT_BE_VOID)
    @Length(min = 1, max = 254, message = ERROR_LABEL_SECTION_NAME_WRONG)
    @UniqueField(message = TITLE_ALREADY_EXISTS)
    private String title;
    
    private String description;

    /**
     * Default constructor of entity.
     */
    public TopicType() {
    }

    /**
     * Construct TopicType with specified title and description.
     */
    public TopicType(String title, String description) {
        this.title = title;
        this.description = description;
    }
    
    /**
     * Get the TopicType description.
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of TopicType.
     * @param description description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the title of the TopicType.
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of TopicType.
     * @param title title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
