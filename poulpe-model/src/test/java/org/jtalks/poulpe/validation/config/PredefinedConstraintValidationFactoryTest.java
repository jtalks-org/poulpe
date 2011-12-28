package org.jtalks.poulpe.validation.config;

import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;

import org.jtalks.poulpe.validation.config.PredefinedConstraintValidationFactory;
import org.jtalks.poulpe.validation.unique.UniqueConstraintValidator;
import org.testng.annotations.Test;

public class PredefinedConstraintValidationFactoryTest {
    @Test
    public void setValidators() {
        UniqueConstraintValidator uniqueConstraintValidator = new UniqueConstraintValidator();

        PredefinedConstraintValidationFactory factory = new PredefinedConstraintValidationFactory();
        
        List<ConstraintValidator<?, ?>> list = Arrays
                .<ConstraintValidator<?, ?>> asList(uniqueConstraintValidator);
        factory.setValidators(list);
        
        UniqueConstraintValidator actual = factory.getInstance(UniqueConstraintValidator.class);

        assertEquals(actual, uniqueConstraintValidator);
    }
}
