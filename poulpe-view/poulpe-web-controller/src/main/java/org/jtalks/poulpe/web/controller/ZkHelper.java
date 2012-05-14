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
package org.jtalks.poulpe.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
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

    /**
     * Creates a helper with a component to work with.
     * @param component the component this helper will be working with
     */
    public ZkHelper(@Nonnull Component component) {
        this.component = component;
    }

    /**
     * Wires zk objects to their ui representation for events handling.<br>
     * Adds forward conditions to myid source component so onXxx source event received by myid component can be
     * forwarded to the specified target component with the target event name onXxx$myid.
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
    public Component wireToZul(String zul) {
        return Executions.createComponents(zul, component, null);
    }

    /**
     * Wire components to controller.
     * 
     * @param component the reference component for selector
     * @param controller the controller object to be injected with variables
     */
    public void wireComponents(Component component, Object controller) {
        Selectors.wireComponents(component, controller, false);
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
     * Returns the first component that match the selector.
     * 
     * @param root the reference component for selector
     * @param selector the selector string
     * @return component matching the selector
     */
    public Component findComponent(String selector) {
        List<Component> comps = Selectors.find(component, selector);
        if(comps.size() > 0)
            return comps.get(0);
        return null;
    }

    /**
     * Find the component by id.
     *
     * @param id component's Id
     * @return found component or null
     */
    public Component getCurrentComponent(String id) {
        for (Component c : Executions.getCurrent().getDesktop().getComponents()) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
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

    /**
     * Returns the label of the specified key based on the current Locale, or null if no found. 
     *
     * @param key the label code to return its value
     * @return the label value according to current locale
     * @see Labels#getLabel(String)
     */
    public String getLabel(String key) {
        return Labels.getLabel(key);
    }

    /**
     * Sends a temporary redirect response to the client using the specified redirect location URL by use of the
     * current execution, {@link Executions#getCurrent}. 
     * 
     * @param uri the URI to redirect to, or null to reload the same page
     */
    public void sendRedirect(String uri) {
        Executions.sendRedirect(uri);
    }

    /**
     * Reloads current page.
     */
    public void reloadPage() {
        Executions.sendRedirect(null);
    }

    /**
     * Returns an instance of javax.servlet.ServletResponse, or null if not available.
     * 
     * @return the native response
     */
    public HttpServletResponse getResponse() {
        return (HttpServletResponse) Executions.getCurrent().getNativeResponse();
    }

    /**
     * Constructs a cookie with a specified name and value. See {@link Cookie#Cookie(String, String)}.
     * The method is used to ease testing.
     * 
     * @param name a <code>String</code> specifying the name of the cookie
     * @param value a <code>String</code> specifying the value of the cookie
     * @return new cookie
     */
    public Cookie createCookie(String name, String value) {
        return new Cookie(name, value);
    }
}
