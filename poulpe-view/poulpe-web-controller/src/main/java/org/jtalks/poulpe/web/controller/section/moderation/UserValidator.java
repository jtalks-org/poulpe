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
            addError(ModerationDialogPresenter.USER_ALREADY_MODERATOR);
        }
    }
}