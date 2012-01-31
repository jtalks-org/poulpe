package org.jtalks.poulpe.web.controller.section;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;

public class ZkInitializer {

    private final Component component;

    public ZkInitializer(Component component) {
        this.component = component;
    }
    
    @SuppressWarnings("deprecation")
    public void init() {
        Components.wireVariables(component, component);
        Components.addForwards(component, component);
    }
    
    public void wireToZul(String zul) {
        Executions.createComponents(zul, component, null);
    }

}
