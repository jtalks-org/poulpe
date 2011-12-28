package org.jtalks.common.security;


/**
 * <p>This class is used to retrieve DTO mapping to entities.</p>
 * <p>Mappings are being configured using annotation {@link ModelEntity},
 * added to class should be mapped to model class.</p>
 * Date: 16.09.2011<br />
 * Time: 15:19
 *
 * @author Alexey Malev
 */
public class DtoMapper {
    /**
     * This method resolves the mapping of the specified class.
     *
     * @param classname Fully-qualified classname, mapping of which needs to be resolved.
     * @return Mapped class, if there is mapping for the class corresponds to the specified argument;
     *         <code>null</code> if there is no such class.
     *         appropriate mapping otherwise.
     * @throws IllegalStateException If there is no class with <code>classname</code> can be resolved
     */
    public Class getMapping(String classname) {
        try {
            Class providedClass = Class.forName(classname);
            ModelEntity annotation = (ModelEntity) providedClass.getAnnotation(ModelEntity.class);

            return annotation == null ? null : annotation.value();
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Class [" + classname + "] not found.", e);
        }
    }
}

