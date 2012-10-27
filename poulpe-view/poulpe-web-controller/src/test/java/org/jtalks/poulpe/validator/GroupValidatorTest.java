package org.jtalks.poulpe.validator;

import org.apache.commons.lang3.RandomStringUtils;
import org.jtalks.common.model.entity.Group;
import org.jtalks.poulpe.service.GroupService;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.impl.BinderImpl;
import org.zkoss.bind.impl.PropertyImpl;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * @author Leonid Kazancev
 */
public class GroupValidatorTest {
    GroupValidator validator;

    @Mock
    GroupService groupService;
    @Mock
    ValidationContext context;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        validator = new GroupValidator(groupService);
    }

    private void givenBindContextReturnsNameAndGroupId(String name, long groupId) {
        BinderImpl binder = mock(BinderImpl.class);
        BindContext bindContext = mock(BindContext.class);
        Property property = new PropertyImpl("", "name", name);

        when(context.getProperty()).thenReturn(property);
        when(context.getBindContext()).thenReturn(bindContext);
        when(bindContext.getValidatorArg("group")).thenReturn(createGroupWithId(groupId));
        when(bindContext.getBinder()).thenReturn(binder);
    }

    @Test
    public void tooLongNameShouldFailValidation() {
        givenBindContextReturnsNameAndGroupId(RandomStringUtils.randomAlphanumeric(255) + "name", 1);
        validator.validate(context);
        verify(context).setInvalid();
    }

    @Test
    public void duplicatedMailShouldFailValidation() throws Exception {
        Group group1 = createGroupWithId(1);
        Group group2 = createGroupWithId(2);
        group1.setName("group1");
        group2.setName("someGroup");

        givenBindContextReturnsNameAndGroupId(group1.getName(), group2.getId());
        storeGroupsInMockedDb(group1, group2);
        validator.validate(context);
        verify(context).setInvalid();
    }


    @Test
    public void testTheSameNameValidation() throws Exception {
        Group group = createGroupWithId(1);
        givenBindContextReturnsNameAndGroupId(group.getName(), group.getId());
        storeGroupsInMockedDb(group);
        validator.validate(context);
        verify(context, never()).setInvalid();
    }

    @Test
    public void testTwoUniqueNames() throws Exception {
        Group group1 = createGroupWithId(1);
        Group group2 = createGroupWithId(2);
        group1.setName("group1");
        group2.setName("group2");
        givenBindContextReturnsNameAndGroupId("group3", group2.getId());
        storeGroupsInMockedDb(group1, group2);
        validator.validate(context);
        verify(context, never()).setInvalid();
    }

    private void storeGroupsInMockedDb(Group... groups) throws Exception {
        for (Group group : groups) {
            List<Group> groupList = new ArrayList<Group>();
            groupList.add(group);
            when(groupService.getByName(group.getName())).thenReturn(groupList);
        }
    }

    private Group createGroupWithId(long id) {
        Group group = new Group("groupName", "");
        group.setId(id);
        return group;
    }
}
