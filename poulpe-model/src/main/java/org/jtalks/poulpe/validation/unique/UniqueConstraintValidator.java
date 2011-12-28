package org.jtalks.poulpe.validation.unique;

import static org.jtalks.poulpe.validation.unique.UniquenessViolationFinder.forEntity;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorFactory;

import org.jtalks.common.model.entity.Entity;
import org.jtalks.poulpe.validation.annotations.UniqueConstraint;
import org.jtalks.poulpe.validation.annotations.UniqueField;

/**
 * Class for verifying using JSR-303 that a given object (the instance with
 * {@link UniqueConstraint} annotation on it) violates no uniqueness constraints
 * and can be safely saved - and if no, the appropriate message will be added to
 * resulting verification set with names of fields violated the constraints.<br>
 * <br>
 * 
 * Created by spring's {@link ConstraintValidatorFactory} implementation wiring
 * an instance of {@link UniquenessViolatorsRetriever} for retrieving the data.<br>
 * <br>
 * 
 * The checking itself requires using two annotations - {@link UniqueConstraint}
 * on classes and {@link UniqueField} on their fields. When facing
 * {@link UniqueConstraint}, validator calls this class, and it looks for fields
 * marked with {@link UniqueField}, then checks whether these constraints are
 * violated or not by retrieving the data from the database, and, finally, if it
 * does violate, creates appropriate messages.<br>
 * <br>
 * 
 * For more info on how to use it, see {@link UniqueConstraint}.
 * 
 * @author Tatiana Birina
 * @author Alexey Grigorev
 * 
 * @see UniqueConstraint
 * @see UniqueField
 */
public class UniqueConstraintValidator implements ConstraintValidator<UniqueConstraint, Entity> {

    private UniquenessViolatorsRetriever uniquenessViolatorsRetriever;

    /**
     * Does nothing
     */
    @Override
    public void initialize(UniqueConstraint annotation) {
    }

    /**
     * Determines if there are no unique constraint violations. If there are,
     * adds to the context names of fields whose uniqueness is violated.
     * Disables the default constraint violation.
     * 
     * @return {@code true} if no duplicates are found, {@code false} otherwise
     */
    @Override
    public boolean isValid(Entity bean, ConstraintValidatorContext context) {
        EntityWrapper entity = new EntityWrapper(bean);

        List<EntityWrapper> duplicates = uniquenessViolatorsRetriever.duplicatesFor(entity);
        if (duplicates.isEmpty()) {
            return true;
        }

        forEntity(entity).in(duplicates).findViolationsAndAddTo(context);
        return false;
    }

    /**
     * @return the uniquenessViolatorsRetriever
     */
    public UniquenessViolatorsRetriever getUniquenessViolatorsRetriever() {
        return uniquenessViolatorsRetriever;
    }

    /**
     * @param uniquenessViolatorsRetriever
     */
    public void setUniquenessViolatorsRetriever(UniquenessViolatorsRetriever uniquenessViolatorsRetriever) {
        this.uniquenessViolatorsRetriever = uniquenessViolatorsRetriever;
    }

}