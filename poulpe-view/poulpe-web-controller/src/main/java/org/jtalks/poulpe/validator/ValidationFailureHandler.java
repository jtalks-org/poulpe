package org.jtalks.poulpe.validator;

import java.util.Map;

import org.jtalks.poulpe.validation.ValidationError;
import org.jtalks.poulpe.validation.ValidationResult;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.impl.InputElement;

import com.google.common.collect.ImmutableMap;

public class ValidationFailureHandler {
	
	public void validationFailure(ValidationResult result, String textBoxName, Textbox textBox) {
        Map<String, ? extends InputElement> comps = ImmutableMap.of(textBoxName, textBox);
        
        for (ValidationError error : result.getErrors()) {
            String fieldName = error.getFieldName();
            if (comps.containsKey(fieldName)) {
                String message = Labels.getLabel(error.getErrorMessageCode());

                InputElement ie = comps.get(fieldName);
                ie.setErrorMessage(message);
            }
        }
    }

}
