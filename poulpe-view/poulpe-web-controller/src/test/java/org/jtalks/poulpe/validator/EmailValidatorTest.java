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

import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.test.fixtures.TestFixtures;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.impl.BinderImpl;
import org.zkoss.bind.sys.ValidationMessages;

import static org.mockito.Mockito.*;

/**
 * @author Nickolay Polyarniy
 */
public class EmailValidatorTest {

    EmailValidator validator;

    @Mock
    UserService userService;
    @Mock
    ValidationContext context;
    @Mock
    Property property;
    @Mock
    BindContext bindContext;
    @Mock
    BinderImpl binder;
    @Mock
    ValidationMessages vmsgs;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        validator = new EmailValidator(userService);
    }

    private void setUpContext(String email, long userId) {
        when(context.getProperty()).thenReturn(property);
        when(property.getValue()).thenReturn(email);
        PoulpeUser user = TestFixtures.user();
        user.setId(userId);
        when(context.getBindContext()).thenReturn(bindContext);
        when(bindContext.getValidatorArg("user")).thenReturn(user);
        when(bindContext.getBinder()).thenReturn(binder);
        when(binder.getValidationMessages()).thenReturn(vmsgs);
    }

    private void setUpUserService(PoulpeUser... users) throws Exception {
        for (PoulpeUser user : users) {
            when(userService.getByEmail(user.getEmail())).thenReturn(user);
            when(userService.isEmailAlreadyUsed(user.getEmail())).thenReturn(true);
        }
    }

    @Test
    public void testLengthValidation() {
        setUpContext("ABCDEABCDEABCDEABCDEABCDEABCDEABCDEABCDEABCDEABCDEABCDEABCDEABCDEABCDEABCDEABC" +
                "DEABCDEABCDEABCDEABCDEABCDEABCDEABCDEABCDEABCDEABCDEABCDEABCDEABCDEABCDEABCDEABCDEA" +
                "BCDEABCDEABCDEABCDEABCDEABCDEABCDEABCDEABCDEABCDEABCDEABCDEABCDEABCDEABCDEABCDEABCD" +
                "EABCDE@rambler.ru", 1);
        validator.validate(context);
        verify(context, atLeast(1)).setInvalid();
    }

    @Test
    public void testPatternValidation() {
        setUpContext("asdasd", 1);
        validator.validate(context);
        verify(context, atLeast(1)).setInvalid();
    }

    @Test
    public void testExistenceValidation() throws Exception {
        PoulpeUser user1 = TestFixtures.user();
        PoulpeUser user2 = TestFixtures.user();
        //user2 wants to save email which already used by user1
        setUpContext(user1.getEmail(), user2.getId());
        setUpUserService(user1, user2);
        validator.validate(context);
        if (user1.getId() != user2.getId()) {
            verify(context, atLeast(1)).setInvalid();
        }
    }

    /**
     * Checks the next situation: user A was with email E1,
     * in edit_user E1 was changed to E2, then it was changed back to E1.
     * So user A trying to save email E1 while he already have saved email E1.
     */
    @Test
    public void testTheSameEmailValidation() throws Exception {
        PoulpeUser user = TestFixtures.user();
        setUpContext(user.getEmail(), user.getId());
        setUpUserService(user);
        validator.validate(context);
        verify(context, never()).setInvalid();
    }

}
