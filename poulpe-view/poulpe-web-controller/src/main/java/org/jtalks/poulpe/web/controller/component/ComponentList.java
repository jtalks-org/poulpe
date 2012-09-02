package org.jtalks.poulpe.web.controller.component;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.Validate;
import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.web.controller.zkutils.BindUtilsWrapper;

import javax.validation.constraints.NotNull;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Those components that are going to be shown in the tree under Components menu item. If any VM that follows MVVM
 * pattern needs notification about component removal/adding/editing, it should be subscribed to this list by {@link
 * #registerListener(Object)}. If something changed, then this list notifies VMs about changes <b>for all their
 * properties</b>. This is done because here we don't know what of properties of the VM should be actually notified, so
 * it's safer to notify all of them so pages that depend on this list will be refreshed with data in VMs.
 *
 * @author stanislav bashkirtsev
 */
public class ComponentList implements Iterable<Component> {

    /**
     * Clears the previous items in the list and puts new ones.
     *
     * @param components a new list of the components to be set instead of previous one
     */
    public void renew(List<Component> components) {
        this.components.clear();
        this.components.addAll(components);
        bindUtils.notifyAllPropsChanged(listeningVms);
    }

    /**
     * Gets an immutable copy of underlying list of the components.
     *
     * @return immutable copy of the underlying list of the components
     */
    public List<Component> getList() {
        return ImmutableList.copyOf(components);
    }

    /**
     * Adds a new component to the list (you should check on your own whether this component already there or not
     * because no checks are done for that). After adding a component, notifies all the registered VMs about change.
     *
     * @param component a new component to be added to the list, note that no checks or validation is done here, so do
     *                  it on your own
     */
    public void add(Component component) {
        components.add(component);
        bindUtils.notifyAllPropsChanged(listeningVms);
    }

    /**
     * Removes the component from the list if it's already there and notifies all the registered VMs about changes. Just
     * for simplicity the VMs will be notified about the changes no matter whether the component actually was removed or
     * it wasn't there in the first place.
     *
     * @param component a component to remove from the list, does nothing if component wasn't there in the first place,
     *                  but notifies VMs anyway
     */
    public void remove(Component component) {
        components.remove(component);
        bindUtils.notifyAllPropsChanged(listeningVms);
    }

    /**
     * You should invoke this method if the content of the list left unchanged, but the component itself changed some
     * its properties like name, this will notify all the listening VMs about this change.
     */
    public void componentsUpdated() {
        bindUtils.notifyAllPropsChanged(listeningVms);
    }

    /**
     * Register a new VM to listen to the changes of the list of components. If some changes appear, this VM will be
     * notified about them via {@link org.zkoss.bind.BindUtils}. This is required because list of components can be used
     * in different places, like in the tree of components and on the page of editing components. <b>Note</b>, that
     * specified VM should follow MVVM pattern to be notified, otherwise this notification won't have effect.
     *
     * @param vmToListen a VM that follows MVVM pattern in ZK to be notified about changes in the lis of components,
     *                   won't have effect if VM already registered for notifications
     */
    public void registerListener(@NotNull Object vmToListen) {
        Validate.notNull(vmToListen);
        listeningVms.add(vmToListen);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Component> iterator() {
        return components.iterator();
    }

    private final List<Component> components = new CopyOnWriteArrayList<Component>();
    private final Set<Object> listeningVms = new CopyOnWriteArraySet<Object>();
    private final BindUtilsWrapper bindUtils = new BindUtilsWrapper();
}
