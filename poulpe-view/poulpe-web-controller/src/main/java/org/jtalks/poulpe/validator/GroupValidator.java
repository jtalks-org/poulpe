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
package org.jtalks.poulpe.validator;

import org.jtalks.common.model.entity.Group;
import org.jtalks.poulpe.service.GroupService;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.BeanValidator;
import org.zkoss.util.resource.Labels;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Leonid Kazancev
 * @author Andrei Alikov
 */
public class GroupValidator extends BeanValidator {
    private static final String DUPLICATED_GROUP_MESSAGE = "err.usergroups.name.same_name_violation";
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    //Injected
    private final GroupService groupService;

    /**
     * @param groupService to have access to database and check whether a group already exists in DB
     */
    public GroupValidator(GroupService groupService) {
        this.groupService = groupService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(ValidationContext validationContext) {
        Group group = (Group) validationContext.getBindContext().getValidatorArg("group");
        String oldName = group.getName();
        String newName = (String) validationContext.getProperty().getValue();
        boolean beanValidationFailed = beanValidationFails(validationContext, new Group(newName));
        if (!beanValidationFailed && nameWasChanged(newName, oldName)) {
            checkForUniqueness(validationContext, newName, oldName);
        }
    }

    /**
     * Checks if name of the group was changed
     * @param newName new group name
     * @param oldName old group name
     * @return true and false otherwise
     */
    private boolean nameWasChanged(String newName, String oldName) {
        String trimmedNewName = newName.trim();
        if (oldName != null && trimmedNewName.equals(oldName.trim())) {
            return false;
        }

        return true;
    }

    /**
     * Checks if group is unique
     * @param validationContext validation context
     * @param newName old name of the group
     * @param oldName new name of the group
     */
    private void checkForUniqueness(ValidationContext validationContext, String newName, String oldName) {
        List<Group> sameNameGroups = groupService.getByName(newName.trim());
        if (sameNameGroups.size() > 0) {
            addInvalidMessage(validationContext, Labels.getLabel(DUPLICATED_GROUP_MESSAGE));
        }
    }

    /**
     * Checks if new group name is valid with default bean validator
     * @param validationContext validation context
     * @param group group object with new group name
     * @return
     */
    private boolean beanValidationFails(ValidationContext validationContext, Group group) {
        Set<ConstraintViolation<?>> violations =
                new HashSet<ConstraintViolation<?>>(validator.validateProperty(group, "name"));

        handleConstraintViolation(validationContext, violations);

        return !violations.isEmpty();
    }

}
