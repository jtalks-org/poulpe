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


import org.jtalks.common.validation.ValidationError;
import org.jtalks.common.validation.ValidationResult;
import org.testng.annotations.Test;
import org.zkoss.zul.impl.InputElement;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.*;

public class ValidationFailureHandlerTest {

    @Test
    public void testOneInputElementHandlerShouldSetError() {
        InputElement element = mock(InputElement.class);
        ValidationFailureHandler handler = new ValidationFailureHandler("first input", element);

        ValidationResult result = new ValidationResult(new ValidationError("first input", "an error"));

        handler.validationFailure(result);

        verify(element).setErrorMessage(anyString());
    }

    @Test
    public void testOneInputElementHandlerShouldNotSetError() {
        InputElement element = mock(InputElement.class);
        ValidationFailureHandler handler = new ValidationFailureHandler("first input", element);

        ValidationResult result = new ValidationResult(new ValidationError("first input2", "an error"));

        handler.validationFailure(result);

        verify(element, never()).setErrorMessage(anyString());
    }

    @Test
    public void testOneInputElementHandlerWithEmptyValidationResult() {
        InputElement element = mock(InputElement.class);
        ValidationFailureHandler handler = new ValidationFailureHandler("first input", element);

        ValidationResult result = new ValidationResult(new HashSet<ValidationError>());

        handler.validationFailure(result);

        verify(element, never()).setErrorMessage(anyString());
    }

    @Test
    public void testTwoInputElementHandlerShouldSetOneError() {
        InputElement element = mock(InputElement.class);
        InputElement element2 = mock(InputElement.class);
        ValidationFailureHandler handler =
                new ValidationFailureHandler("first input", element, "second input", element2);

        ValidationResult result = new ValidationResult(new ValidationError("first input", "an error"));

        handler.validationFailure(result);

        verify(element).setErrorMessage(anyString());
        verify(element2, never()).setErrorMessage(anyString());
    }

    @Test
    public void testTwoInputElementHandlerShouldSetTwoErrors() {
        InputElement element = mock(InputElement.class);
        InputElement element2 = mock(InputElement.class);
        ValidationFailureHandler handler =
                new ValidationFailureHandler("first input", element, "second input", element2);

        List<ValidationError> list =
                Arrays.asList(new ValidationError("first input", "an error"),
                              new ValidationError("second input", "an error2"));
        ValidationResult result = new ValidationResult(new HashSet<ValidationError>(list));

        handler.validationFailure(result);

        verify(element).setErrorMessage(anyString());
        verify(element2).setErrorMessage(anyString());
    }

    @Test
    public void testTwoInputElementHandlerShouldSetNoErrors() {
        InputElement element = mock(InputElement.class);
        InputElement element2 = mock(InputElement.class);
        ValidationFailureHandler handler =
                new ValidationFailureHandler("first input", element, "second input", element2);

        List<ValidationError> list =
                Arrays.asList(new ValidationError("first input2", "an error"),
                              new ValidationError("second input2", "an error2"));
        ValidationResult result = new ValidationResult(new HashSet<ValidationError>(list));

        handler.validationFailure(result);

        verify(element, never()).setErrorMessage(anyString());
        verify(element2, never()).setErrorMessage(anyString());
    }

    @Test
    public void testTwoInputElementHandlerWithEmptyValidationResult() {
        InputElement element = mock(InputElement.class);
        InputElement element2 = mock(InputElement.class);
        ValidationFailureHandler handler =
                new ValidationFailureHandler("first input", element, "second input", element2);

        ValidationResult result = new ValidationResult(new HashSet<ValidationError>());

        handler.validationFailure(result);

        verify(element, never()).setErrorMessage(anyString());
        verify(element2, never()).setErrorMessage(anyString());
    }

    @Test
    public void testThreeInputElementHandlerShouldSetOneError() {
        InputElement element = mock(InputElement.class);
        InputElement element2 = mock(InputElement.class);
        InputElement element3 = mock(InputElement.class);
        ValidationFailureHandler handler =
                new ValidationFailureHandler("first input", element, "second input", element2, "third input", element3);

        ValidationResult result = new ValidationResult(new ValidationError("first input", "an error"));

        handler.validationFailure(result);

        verify(element).setErrorMessage(anyString());
        verify(element2, never()).setErrorMessage(anyString());
        verify(element3, never()).setErrorMessage(anyString());
    }

    @Test
    public void testThreeInputElementHandlerShouldSetTwoErrors() {
        InputElement element = mock(InputElement.class);
        InputElement element2 = mock(InputElement.class);
        InputElement element3 = mock(InputElement.class);
        ValidationFailureHandler handler =
                new ValidationFailureHandler("first input", element, "second input", element2, "third input", element3);

        List<ValidationError> list =
                Arrays.asList(new ValidationError("first input", "an error"),
                        new ValidationError("second input", "an error2"));
        ValidationResult result = new ValidationResult(new HashSet<ValidationError>(list));

        handler.validationFailure(result);

        verify(element).setErrorMessage(anyString());
        verify(element2).setErrorMessage(anyString());
        verify(element3, never()).setErrorMessage(anyString());
    }

    @Test
    public void testThreeInputElementHandlerShouldSetThreeErrors() {
        InputElement element = mock(InputElement.class);
        InputElement element2 = mock(InputElement.class);
        InputElement element3 = mock(InputElement.class);
        ValidationFailureHandler handler =
                new ValidationFailureHandler("first input", element, "second input", element2, "third input", element3);

        List<ValidationError> list =
                Arrays.asList(new ValidationError("first input", "an error"),
                              new ValidationError("second input", "an error2"),
                              new ValidationError("third input", "an error2"));
        ValidationResult result = new ValidationResult(new HashSet<ValidationError>(list));

        handler.validationFailure(result);

        verify(element).setErrorMessage(anyString());
        verify(element2).setErrorMessage(anyString());
        verify(element3).setErrorMessage(anyString());
    }

    @Test
    public void testThreeInputElementHandlerShouldSetNoErrors() {
        InputElement element = mock(InputElement.class);
        InputElement element2 = mock(InputElement.class);
        InputElement element3 = mock(InputElement.class);
        ValidationFailureHandler handler =
                new ValidationFailureHandler("first input", element, "second input", element2, "third input", element3);

        List<ValidationError> list = Arrays.asList(new ValidationError("first input2", "an error"),
                                                   new ValidationError("second input2", "an error2"),
                                                   new ValidationError("third input2", "an error2"));
        ValidationResult result = new ValidationResult(new HashSet<ValidationError>(list));

        handler.validationFailure(result);

        verify(element, never()).setErrorMessage(anyString());
        verify(element2, never()).setErrorMessage(anyString());
        verify(element3, never()).setErrorMessage(anyString());
    }

    @Test
    public void testThreeInputElementHandlerWithEmptyValidationResult() {
        InputElement element = mock(InputElement.class);
        InputElement element2 = mock(InputElement.class);
        InputElement element3 = mock(InputElement.class);
        ValidationFailureHandler handler =
                new ValidationFailureHandler("first input", element, "second input", element2, "third input", element3);

        ValidationResult result = new ValidationResult(new HashSet<ValidationError>());

        handler.validationFailure(result);

        verify(element, never()).setErrorMessage(anyString());
        verify(element2, never()).setErrorMessage(anyString());
        verify(element3, never()).setErrorMessage(anyString());
    }
}
