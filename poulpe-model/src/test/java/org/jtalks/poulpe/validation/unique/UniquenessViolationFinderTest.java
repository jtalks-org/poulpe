package org.jtalks.poulpe.validation.unique;

import static org.jtalks.poulpe.validation.unique.UniquenessViolationFinder.forEntity;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderDefinedContext;

import org.jtalks.common.model.entity.Entity;
import org.jtalks.poulpe.model.dao.hibernate.ObjectsFactory;
import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.model.entity.ComponentType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Alexey Grigorev
 */
public class UniquenessViolationFinderTest {

    private static final String COMPONENT_TYPE_FIELD = "componentType";
    private static final String NAME_FIELD = "name";

    private ConstraintValidatorContext context;
    private ConstraintViolationBuilder violationBuilder;
    private NodeBuilderDefinedContext nameNode;
    private NodeBuilderDefinedContext componentTypeNode;
    private Component component;
    private EntityWrapper forum;

    @BeforeMethod
    public void beforeMethod() {
        context = mock(ConstraintValidatorContext.class);

        violationBuilder = mock(ConstraintViolationBuilder.class);
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);

        nameNode = mock(NodeBuilderDefinedContext.class);
        when(violationBuilder.addNode(NAME_FIELD)).thenReturn(nameNode);

        componentTypeNode = mock(NodeBuilderDefinedContext.class);
        when(violationBuilder.addNode(COMPONENT_TYPE_FIELD)).thenReturn(componentTypeNode);

        forum = forum();
    }

    private EntityWrapper forum() {
        component = ObjectsFactory.createComponent(ComponentType.FORUM);
        return wrap(component);
    }

    public static EntityWrapper wrap(Entity entity) {
        return new EntityWrapper(entity);
    }

    @Test
    public void duplicatesNotSpecified() {
        forEntity(forum).findViolationsAndAddTo(context);
        verifyNoConstraintViolationsAdded();
    }

    private void verifyNoConstraintViolationsAdded() {
        verify(context, never()).buildConstraintViolationWithTemplate(anyString());
    }

    @Test
    public void emptyDuplicates() {
        forum = forum();

        List<EntityWrapper> noDuplicates = Collections.emptyList();
        forEntity(forum).in(noDuplicates).findViolationsAndAddTo(context);

        verifyNoConstraintViolationsAdded();
    }

    @Test
    public void oneDuplicatesType() {
        forum = forum();
        List<EntityWrapper> duplicates = componentTypeDuplicated();

        forEntity(forum).in(duplicates).findViolationsAndAddTo(context);

        verifyComponentTypeConstraintViolationAdded();
    }

    private List<EntityWrapper> componentTypeDuplicated() {
        return Arrays.asList(forum());
    }

    private void verifyComponentTypeConstraintViolationAdded() {
        String errorMessage = forum.getErrorMessage(COMPONENT_TYPE_FIELD);
        verify(context).buildConstraintViolationWithTemplate(errorMessage);
        verify(nameNode, never()).addConstraintViolation();
        verify(componentTypeNode).addConstraintViolation();
    }

    @Test
    public void oneDuplicatesName() {
        forum = forum();
        List<EntityWrapper> duplicates = oneNameDuplication();

        forEntity(forum).in(duplicates).findViolationsAndAddTo(context);

        verifyNameConstaintViolationAdded();
    }
    
    private List<EntityWrapper> oneNameDuplication() {
        Component article = new Component(component.getName(), "desc", ComponentType.ARTICLE);
        return Arrays.asList(wrap(article));
    }

    private void verifyNameConstaintViolationAdded() {
        String errorMessage = forum.getErrorMessage(NAME_FIELD);
        verify(context).buildConstraintViolationWithTemplate(errorMessage);
        verify(nameNode).addConstraintViolation();
        verify(componentTypeNode, never()).addConstraintViolation();
    }

}
