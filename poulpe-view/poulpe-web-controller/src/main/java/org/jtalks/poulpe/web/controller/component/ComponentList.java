package org.jtalks.poulpe.web.controller.component;

import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.web.controller.zkutils.BindUtilsWrapper;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Those components that are going to be shown in the tree under Components menu item.
 *
 * @author stanislav bashkirtsev
 */
public class ComponentList implements Iterable<Component> {
    private final BindUtilsWrapper bindUtils = new BindUtilsWrapper();
    private final List<Component> components = new CopyOnWriteArrayList<Component>();
    private final Set<Object> listeningVms = new CopyOnWriteArraySet<Object>();

    public void renew(List<Component> components) {
        this.components.clear();
        this.components.addAll(components);
        bindUtils.notifyAllPropsChanged(listeningVms);
    }

    public List<Component> getList() {
        return components;
    }

    public void add(Component component) {
        components.add(component);
        bindUtils.notifyAllPropsChanged(listeningVms);
    }

    public void remove(Component component) {
        components.remove(component);
        bindUtils.notifyAllPropsChanged(listeningVms);
    }

    public void componentsUpdated() {
        bindUtils.notifyAllPropsChanged(listeningVms);
    }

    public void registerListener(@Nonnull Object vmToListen) {
        listeningVms.add(vmToListen);
    }

    @Override
    public Iterator<Component> iterator() {
        return components.iterator();
    }
}
