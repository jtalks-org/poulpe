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
package org.jtalks.poulpe.validation.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.jtalks.poulpe.model.dao.hibernate.ObjectsFactory;
import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.model.entity.ComponentType;
import org.jtalks.poulpe.validation.annotations.UniqueField;
import org.jtalks.poulpe.validation.util.AnnotatedField;
import org.jtalks.poulpe.validation.util.Refl;
import org.testng.annotations.Test;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class ReflTest {

    @Test
    public void getBranchFields() {
        List<AnnotatedField<UniqueField>> fields = Refl.getAnnotatedFields(Component.class, UniqueField.class);
        List<String> names = toNames(fields);

        assertTrue(names.contains("name"));
    }

    @Test
    public void getComponentFields() {
        List<AnnotatedField<UniqueField>> fields = Refl.getAnnotatedFields(Component.class, UniqueField.class);

        List<String> names = toNames(fields);
        assertTrue(names.contains("name"));
        assertTrue(names.contains("componentType"));
    }

    private static List<String> toNames(List<AnnotatedField<UniqueField>> fields) {
        return Lists.transform(fields, fieldNamesRetriever);
    }

    private final static Function<AnnotatedField<?>, String> fieldNamesRetriever = 
            new Function<AnnotatedField<?>, String>() {
        public String apply(AnnotatedField<?> input) {
            return input.getFieldName();
        }
    };

    @Test
    public void convertToMap() {
        Component component = ObjectsFactory.createComponent(ComponentType.ARTICLE);
        List<AnnotatedField<UniqueField>> fields = Refl
                .getAccessibleAnnotatedFields(Component.class, UniqueField.class);

        Map<String, Object> result = Refl.convertToMap(component, toFields(fields));

        assertEquals(result.get("name"), component.getName());
        assertEquals(result.get("componentType"), component.getComponentType());
        assertEquals(result.size(), 2);
    }

    private static List<Field> toFields(List<AnnotatedField<UniqueField>> fields) {
        return Lists.transform(fields, fieldRetriever);
    }

    private final static Function<AnnotatedField<?>, Field> fieldRetriever = 
            new Function<AnnotatedField<?>, Field>() {
        public Field apply(AnnotatedField<?> input) {
            return input.getField();
        }
    };

}
