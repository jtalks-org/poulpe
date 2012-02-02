package org.jtalks.poulpe.web.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.ConventionWires;

/**
 * Initializer for zk-based view objects - mainly for easing the testing for
 * encapsulating static method calls
 * 
 * @author Alexey Grigorev
 */
public class ZkInitializer {

    private final Component component;

    public ZkInitializer(Component component) {
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

}
