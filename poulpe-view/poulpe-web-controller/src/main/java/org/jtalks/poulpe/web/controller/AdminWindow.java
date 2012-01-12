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
public class AdminWindow extends GenericForwardComposer<Component> {
    
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
//        show("WEB-INF/pages/edit_comp.zul");
    }
    
    /**
     * Show the sections list view
     */
    public void onShowSections() {
        show("sections.zul");
    }
    
    public void onShowUserBanning(){
        show("userbanning.zul");
    }

    /**
     * Shows User Groups window that allows admins to CRUD groups.
     */
    public void onShowUserGroups(){
        show("groups.zul");
    }

    /**
     * Show the users list view
     */
    public void onShowUsers() {
        show("users.zul");
    }
    
    /**
     * Show the ranks page.
     */
    public void onShowRanks() {
        show("ranks.zul");
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