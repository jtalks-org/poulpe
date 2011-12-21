package org.jtalks.poulpe.model.dao.hibernate.constraints;

import java.lang.annotation.ElementType;
import javax.validation.Path;
import javax.validation.TraversableResolver;

// TODO: what is it for?
public class CustomTraversableResolver implements TraversableResolver {

	public boolean isReachable(Object traversableObject,
			Path.Node traversableProperty, Class<?> rootBeanType,
			Path pathToTraversableObject, ElementType elementType) {
		return true;
	}

	public boolean isCascadable(Object object, Path.Node node, Class<?> c,
			Path path, ElementType elementType) {
		return true;
	}
}
