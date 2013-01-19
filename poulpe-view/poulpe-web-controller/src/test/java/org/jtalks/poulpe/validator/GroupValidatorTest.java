package org.jtalks.poulpe.validator;

import org.apache.commons.lang3.RandomStringUtils;
import org.jtalks.common.model.entity.Group;
import org.jtalks.poulpe.service.GroupService;
import org.mockito.ArgumentMatcher;
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
 * @author Andrei Alikov
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

    private void givenBindContextReturnsGroupAndNewName(String newName, Group group) {
        BinderImpl binder = mock(BinderImpl.class);
        BindContext bindContext = mock(BindContext.class);
        Property property = new PropertyImpl("", "name", newName);

        when(context.getProperty()).thenReturn(property);
        when(context.getBindContext()).thenReturn(bindContext);
        when(bindContext.getValidatorArg("group")).thenReturn(group);
        when(bindContext.getBinder()).thenReturn(binder);
    }

    @Test
    public void tooLongNameIsInvalid() {
        String longName = RandomStringUtils.randomAlphanumeric(255) + "name";
        givenBindContextReturnsGroupAndNewName(longName, createGroupWithIdAndName(0, ""));
        validator.validate(context);
        verify(context).setInvalid();
    }

    @Test
    public void emptyGroupNameIsInvalid() {
        givenBindContextReturnsGroupAndNewName("", createGroupWithIdAndName(0, ""));
        validator.validate(context);
        verify(context).setInvalid();
    }

    @Test
    public void duplicatedNameIsInvalid() throws Exception {
        Group group1 = createGroupWithId(1);
        Group group2 = createGroupWithId(2);
        group1.setName("group1");
        group2.setName("someGroup");

        givenBindContextReturnsGroupAndNewName(group1.getName(), group2);
        storeGroupsInMockedDb(group1);
        validator.validate(context);
        verify(context).setInvalid();
    }

    @Test
    public void duplicatedGroupNameWithAnotherCaseIsInvalid() throws Exception {
        Group group1 = createGroupWithId(1);
        Group group2 = createGroupWithId(2);
        group1.setName("group1");

        givenBindContextReturnsGroupAndNewName("GroUp1", group2);
        storeGroupsInMockedDb(group1);
        validator.validate(context);
        verify(context).setInvalid();
    }


    @Test
    public void duplicatedGroupNamesWithAnotherCaseAndTwoWordsIsInvalid() throws Exception {
        Group group1 = createGroupWithIdAndName(1, "Group test");
        Group group2 = createGroupWithIdAndName(2, "Some name");

        givenBindContextReturnsGroupAndNewName(group1.getName().toLowerCase(), group2);
        storeGroupsInMockedDb(group1);
        validator.validate(context);
        verify(context).setInvalid();
    }


    @Test
    public void changeGroupNameToTheSameIsValid() throws Exception {
        Group group = createGroupWithIdAndName(1, "myGroup");

        givenBindContextReturnsGroupAndNewName(group.getName(), group);
        storeGroupsInMockedDb(group);
        validator.validate(context);
        verify(context, never()).setInvalid();
    }

    @Test
    public void changeGroupNameFromNullIsValid() throws Exception {
        Group group = createGroupWithIdAndName(1, null);

        givenBindContextReturnsGroupAndNewName("new group", group);

        validator.validate(context);
        verify(context, never()).setInvalid();
    }

    @Test
    public void changeGroupNameFromNullToDuplicatedIsInvalid() throws Exception {
        Group group = createGroupWithIdAndName(1, null);
        Group group2 = createGroupWithIdAndName(2, "myGroup");

        givenBindContextReturnsGroupAndNewName(group2.getName(), group);
        storeGroupsInMockedDb(group2);
        validator.validate(context);
        verify(context).setInvalid();
    }

    @Test
    public void changeGroupNameToTheSameWithSpacesIsValid() throws Exception {
        Group group = createGroupWithIdAndName(1, "myGroup");

        givenBindContextReturnsGroupAndNewName(" " + group.getName() + " ", group);
        storeGroupsInMockedDb(group);
        validator.validate(context);
        verify(context, never()).setInvalid();
    }

    @Test
    public void twoUniqueNamesAreValid() throws Exception {
        Group group1 = createGroupWithId(1);
        Group group2 = createGroupWithId(2);
        group1.setName("group1");

        givenBindContextReturnsGroupAndNewName("group2", group2);
        storeGroupsInMockedDb(group1);
        validator.validate(context);
        verify(context, never()).setInvalid();
    }

    private void storeGroupsInMockedDb(Group... groups) throws Exception {
        for (Group group : groups) {
            List<Group> groupList = new ArrayList<Group>();
            groupList.add(group);
            StringIgnoreCaseMatcher matcher = new StringIgnoreCaseMatcher(group.getName());
            when(groupService.getExactlyByName(argThat(matcher))).thenReturn(groupList);
        }
    }

    private Group createGroupWithIdAndName(long id, String name) {
        Group group = new Group(name, "");
        group.setId(id);
        return group;
    }

    private Group createGroupWithId(long id) {
        return createGroupWithIdAndName(id, "groupName");
    }

    /**
     * Argument matcher that matches String object with specified string ignoring case
     */
    private final static class StringIgnoreCaseMatcher extends ArgumentMatcher<String> {
        private String string;

        StringIgnoreCaseMatcher(String string) {
            this.string = string;
        }

        public boolean matches(Object string) {
            if (string instanceof String) {
                return this.string.equalsIgnoreCase((String) string);
            }
            return  false;
        }
    }
}
