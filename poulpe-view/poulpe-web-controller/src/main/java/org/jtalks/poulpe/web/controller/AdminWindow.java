package org.jtalks.poulpe.web.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Center;

/**
 * 
 * 
 * @author Vladimir Bukhoyarov
 *
 */
public class AdminWindow extends GenericForwardComposer {
    
    private static final long serialVersionUID = 1L;
    private Center workArea;
    private Component currentComponent;
    
    /**
     * Show the component list view
     */
    public void onShowComponents() {
        show("components.zul");
    }

    /**
     * Show the branches list view
     */
    public void onShowBranches() {
        show("brancheditor.zul");
    }
    
    /**
     * Show the topic type list view
     */
    public void onShowTopicTypes() {
        show("topictypelist.zul");
    }
    
    /**
     * Show the sections list view
     */
    public void onShowSections() {
        show("sections.zul");
    }
    
    private void show(String pathToZulFile) {
        if (currentComponent != null) {
            currentComponent.detach();
        }
        currentComponent = Executions.createComponents(pathToZulFile, null, null);
        currentComponent.setParent(workArea);
        currentComponent.setVisible(true);
    }
    

}
