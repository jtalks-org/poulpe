package org.jtalks.poulpe.validator;

import java.util.Map;

import org.jtalks.poulpe.validation.ValidationError;
import org.jtalks.poulpe.validation.ValidationResult;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.impl.InputElement;

import com.google.common.collect.ImmutableMap;

/**
 * @author Alexey Grigorev
 * @author Tatiana Birina
 */
public class ValidationFailureHandler implements ValidationFailure {

    private final Map<String, ? extends InputElement> comps;

    public ValidationFailureHandler(String name, Textbox textBox) {
        comps = ImmutableMap.of(name, textBox);
    }

    public ValidationFailureHandler(String name1, Textbox textBox1, String name2, Textbox textBox2) {
        comps = ImmutableMap.of(name1, textBox1, name2, textBox2);
    }

    public ValidationFailureHandler(String name1, Textbox textBox1, String name2, Textbox textBox2, String name3,
            Textbox textBox3) {
        comps = ImmutableMap.of(name1, textBox1, name2, textBox2, name3, textBox3);
    }

    @Override
    public void validationFailure(ValidationResult result) {
        for (ValidationError error : result.getErrors()) {
            String fieldName = error.getFieldName();
            if (comps.containsKey(fieldName)) {
                showErrorMessage(error, fieldName);
            }
        }
    }

    private void showErrorMessage(ValidationError error, String fieldName) {
        String message = Labels.getLabel(error.getErrorMessageCode());
        InputElement ie = comps.get(fieldName);
        ie.setErrorMessage(message);
    }

}
