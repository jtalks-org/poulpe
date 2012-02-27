package org.jtalks.poulpe.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.ConventionWires;

/**
 * Helper class for zk-based view objects - mainly for easing the testing. It
 * encapsulates static method calls and methods inherited from zk classes. <br>
 * <br>
 * 
 * Helps with:<br>
 * <ul>
 * 
 * <li>Initializing components and wiring its variables to actual ui components
 * ({@link #wireByConvention()} and {@link #wireToZul(String)}</li>
 * 
 * <li>Managing child objects via {@link #addComponent(Component)} and the like</li>
 * 
 * </ul>
 * 
 * @author Alexey Grigorev
 */
public class ZkHelper {

    private final Component component;

    public ZkHelper(Component component) {
        this.component = component;
    }

    /**
     * Wires zk objects to their ui representation
     */
    public void wireByConvention() {
        ConventionWires.wireVariables(component, component);
        ConventionWires.addForwards(component, component);
    }

    /**
     * Wires view to zul file
     * 
     * @param zul file to be wired to
     */
    public void wireToZul(String zul) {
        Executions.createComponents(zul, component, null);
    }

    /**
     * Removes all children of the given class
     * 
     * @param cls elements of which are to be removed
     */
    public <E extends Component> void removeAll(Class<E> cls) {
        List<Component> childrenToSave = filterOut(cls);
        removeAllChildComponents();
        addComponents(childrenToSave);
    }

    /**
     * Gets all the elements but none of the given class
     * 
     * @param classToFilterOut element of which are to be excluded from result
     * @return list of {@link Component} objects
     */
    private <E extends Component> List<Component> filterOut(Class<E> classToFilterOut) {
        List<Component> forKeeping = new ArrayList<Component>();
        for (Component cmp : component.getChildren()) {
            if (!classToFilterOut.isInstance(cmp)) {
                forKeeping.add(cmp);
            }
        }
        return forKeeping;
    }

    /**
     * Clears the list of children
     */
    private void removeAllChildComponents() {
        component.getChildren().clear();
    }

    /**
     * Adds new child component
     * 
     * @param cmp component to be added
     */
    public void addComponent(Component cmp) {
        component.appendChild(cmp);
    }

    /**
     * Adds a collection of components
     * 
     * @param components to be added
     */
    public void addComponents(Collection<Component> components) {
        component.getChildren().addAll(components);
    }

}
