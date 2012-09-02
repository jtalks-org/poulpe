package org.jtalks.poulpe.web.controller.component;

import org.jtalks.poulpe.model.entity.Component;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Those components that are going to be shown in the tree under Components menu item.
 *
 * @author stanislav bashkirtsev
 */
public class ComponentList implements Iterable<Component> {
    private final List<Component> components = new CopyOnWriteArrayList<Component>();

    public void renew(List<Component> components) {
        this.components.clear();
        this.components.addAll(components);
    }

    public List<Component> getList(){
        return components;
    }

    public void add(Component component) {
        components.add(component);
    }

    public void remove(Component component) {
        components.remove(component);
    }

    @Override
    public Iterator<Component> iterator() {
        return components.iterator();
    }
}
