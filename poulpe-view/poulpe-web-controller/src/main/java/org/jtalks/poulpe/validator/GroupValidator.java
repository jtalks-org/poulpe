package org.jtalks.poulpe.validator;

import org.jtalks.common.model.entity.Group;
import org.jtalks.poulpe.service.GroupService;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.BeanValidator;
import org.zkoss.util.resource.Labels;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

/**
 * @author Leonid Kazancev
 */
public class GroupValidator extends BeanValidator {
    private static final String DUPLICATED_GROUP_MESSAGE = "err.usergroups.name.same_name_violation";
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    //Injected
    private final GroupService groupService;

    /**
     * @param groupService to have access to database and check whether a mail already exists in DB
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
        String name = (String) validationContext.getProperty().getValue();
        group.setName(name);
        boolean beanValidationFailed = beanValidationFails(validationContext, group);
        if (!beanValidationFailed) {
            checkForUniqueness(validationContext, name, group);
        }
        group.setName(oldName);
    }

    private void checkForUniqueness(ValidationContext validationContext, String name, Group group) {
        long groupId = group.getId();
        if (((groupId == 0) && isGroupWithThisNameExists(name))
                || (!(groupId == 0) && isExistingGroupAreAnotherGroup(name, groupId))) {
            addInvalidMessage(validationContext, Labels.getLabel(DUPLICATED_GROUP_MESSAGE));
        }
    }

    private boolean isGroupWithThisNameExists(String groupName) {
        List<Group> groupList = groupService.getByName(groupName);
        for (Group group : groupList) {
            if (group.getName().equals(groupName)) {
                return true;
            }
        }
        return false;
    }

    private boolean isExistingGroupAreAnotherGroup(String groupName, long id) {
        return isGroupWithThisNameExists(groupName) && groupService.getByName(groupName).get(0).getId() != id;
    }

    private boolean beanValidationFails(ValidationContext validationContext, Group group) {
        Set<ConstraintViolation<Group>> set = validator.validateProperty(group, "name");
        if (!set.isEmpty()) {
            addInvalidMessage(validationContext, set.iterator().next().getMessage());
            return true;
        }
        return false;
    }

}
