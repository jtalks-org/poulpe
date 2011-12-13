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
package org.jtalks.poulpe.web.controller.section.moderation;

import org.jtalks.common.model.entity.User;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.validator.AbstractValidator;
import org.jtalks.poulpe.validator.Validator;

/**
 * {@link Validator} implementation for validating {@link User} objects for
 * {@link ModerationDialogPresenter}
 * 
 * @author Alexey Grigorev
 */
class UserValidator extends AbstractValidator<User> {

    private final Branch branch;

    public UserValidator(Branch branch) {
        this.branch = branch;
    }

    /**
     * Adds error when:<br/>
     * - the current branch is moderated by given user
     */
    @Override
    public void validate(User user) {
        if (branch.isModeratedBy(user)) {
            setError(ModerationDialogPresenter.USER_ALREADY_MODERATOR);
        }
    }
}