package org.jtalks.poulpe.model.dao.hibernate.constraints;

import static ch.lambdaj.Lambda.collect;
import static ch.lambdaj.Lambda.on;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.List;
import java.util.Map;

import org.jtalks.poulpe.model.dao.hibernate.ObjectsFactory;
import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.model.entity.ComponentType;
import org.testng.annotations.Test;

public class ReflTest {

    @Test
    public void getBranchFields() {
        List<Field> fields = Refl.getAnnotatedFields(Component.class, UniqueField.class);
        List<String> names = toNames(fields);

        assertTrue(names.contains("name"));
    }

    @Test
    public void getComponentFields() {
        List<Field> fields = Refl.getAnnotatedFields(Component.class, UniqueField.class);

        List<String> names = toNames(fields);
        assertTrue(names.contains("name"));
        assertTrue(names.contains("componentType"));
    }

    private static List<String> toNames(List<Field> fields) {
        return collect(fields, on(Member.class).getName());
    }

    @Test
    public void convertToMap() {
        Component component = ObjectsFactory.createComponent(ComponentType.ARTICLE);
        List<Field> fields = Refl.getAccessibleAnnotatedFields(Component.class, UniqueField.class);

        Map<String, Object> result = Refl.convertToMap(component, fields);

        assertEquals(result.get("name"), component.getName());
        assertEquals(result.get("componentType"), component.getComponentType());
        assertEquals(result.size(), 2);
    }

}
