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
package org.jtalks.poulpe.web.controller.zkmacro;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import org.apache.commons.collections.ListUtils;
import org.jtalks.common.model.entity.Group;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModelList;

/**
 * How to use this class:
 * 
 * The component has 2 lists: available groups (left) & those that are already granted/restricted to the
 * permission (right), and allow users to selects some items from one list and moves them
 * to another.
 * For add component to zul page need insert <?component name="dualList"  macroURI="/WEB-INF/zkmacros/DualList.zul"?>
 * 
 * use on page: 
 *  <dualList id = "dualLBox" fullList="${vm.fullList}" rightList="${vm.rightList}"/>
 *  where param fullList - all available groups
 *        param rightList - aleready added grops for permission
 * 
 * get data from component:
 *      you need to send component as arg (component = dualLBox) into some method.
 *      ex: <button id="savePermissionsButton" label="save" onClick="@command('save', component = dualLBox)"/>
 *  in method:
 *      param DualListComponent - get as @BindingParam("component") AbstractComponent
 *      get data - ((DualListVm)DualListComponent.getFellow("DualList").getAttribute("vm")).getRight()  
 * ex.  public void save(@BindingParam("component") AbstractComponent DualListComponent) {
 *        List<Group> addedGroups = ((DualListVm)DualListComponent.getFellow("DualList").getAttribute("vm")).getRight();
 *        //getStateAfterEdit();
 *      } 
 * 
 * @author Enykey
 */
public class DualListVm{

    public static final String LEFT_PROPERTY = "left", RIGHT_PROPERTY = "right";
    public static final String LEFT_SELECTED_PROPERTY = "leftSelected", RIGHT_SELECTED_PROPERTY = "rightSelected";
    /**
     * String represents text in filter field for available items list.
     */
    private String leftFilterTxt = "";
    /**
     * String represents text in filter field for exist items list.
     */
    private String rightFilterTxt = "";

    /**
     * List of available for adding items.
     */
    private ListModelList<Group> left = new BindingListModelList<Group>(new ArrayList<Group>(), false);

    /**
     * List of already existed (added) items.
     */
    private ListModelList<Group> right = new BindingListModelList<Group>(new ArrayList<Group>(), false);

    /**
     * List represents state of existed items after editing.
     */
    private List<Group> stateAfterEdit = new ArrayList<Group>();
    private List<Group> fullList = new ArrayList<Group>();

    /**
     * Constructs VM with simple initialization which avoiding {@code null} values in internal fields.
     */
    
    public DualListVm() {
        left.setMultiple(true);
        right.setMultiple(true);
    }
    
    /**
     * Fill ModelLists for component 
     * 
     * @param fullList full list of groups that not added to permissions
     * @param rightList list of groups already added to permissions
     */    
    @Init
    public void initVm(@BindingParam("fullList") List<Group> fullList,
                       @BindingParam("rightList") List<Group> rightList) {

        this.fullList = fullList;
        
        this.right.clear();
        this.right.addAll(rightList); 
        
        this.left.clear();
        this.left.addAll(ListUtils.subtract(fullList, rightList));
        
        
        this.stateAfterEdit.clear();
        this.stateAfterEdit.addAll(rightList); 
        

    }
    
    // -- Accessors -----------------------------

    /**
     * Gets filter text for available list.
     *
     * @return text for filter field
     */
    public String getAvailFilterTxt() {
        return leftFilterTxt;
    }

    /**
     * Sets filter text for available list.
     *
     * @param leftFilterTxt the filter text to set
     */
    public void setAvailFilterTxt(@Nonnull String leftFilterTxt) {
        this.leftFilterTxt = leftFilterTxt;
    }

    /**
     * Gets filter text for a list of existing items.
     *
     * @return text for filter field
     */
    public String getExistFilterTxt() {
        return rightFilterTxt;
    }

    /**
     * Sets filter text for a list of existing items.
     *
     * @param rightFilterTxt the filter text to set
     */
    public void setExistFilterTxt(@Nonnull String rightFilterTxt) {
        this.rightFilterTxt = rightFilterTxt;
    }

    /**
     * Gets list of available items.
     *
     * @return the list of available items
     */
    public ListModelList<Group> getLeft() {
        return left;
    }

    /**
     * Gets list of existing items.
     *
     * @return the list of right items
     */
    public ListModelList<Group> getRight() {
        return right;
    }

    /**
     * Gets list of items in consistent state (after editing).
     *
     * @return the list of items in consistent state (after editing)
     */
    protected List<Group> getConsistentState() {
        return stateAfterEdit;
    }

    /**
     * Gets set of selected items in available list.
     *
     * @return set of selected items in available list
     */
    public Set<Group> getLeftSelected() {
        return left.getSelection();
    }

    /**
     * Gets set of selected items in existed list.
     *
     * @return set of selected items in existed list
     */
    public Set<Group> getRightSelected() {
        return right.getSelection();
    }

    // -- ZK Commands ---------------------------
    /**
     * Adds available selected item in consistent state.
     */
    @Command
    @NotifyChange({LEFT_PROPERTY, RIGHT_PROPERTY, LEFT_SELECTED_PROPERTY, RIGHT_SELECTED_PROPERTY})
    public void add() {
        stateAfterEdit.addAll(getLeftSelected());
        updateVm();
    }

    /**
     * Adds all available items in consistent state.
     */
    @Command
    @NotifyChange({LEFT_PROPERTY, RIGHT_PROPERTY, LEFT_SELECTED_PROPERTY, RIGHT_SELECTED_PROPERTY})
    public void addAll() {
        stateAfterEdit.addAll(getLeft());
        updateVm();
    }

    /**
     * Removes selected item from consistent state.
     */
    @Command
    @NotifyChange({LEFT_PROPERTY, RIGHT_PROPERTY, LEFT_SELECTED_PROPERTY, RIGHT_SELECTED_PROPERTY})
    public void remove() {
        stateAfterEdit.removeAll(getRightSelected());
        updateVm();
    }

    /**
     * Removes all selected items from consistent state.
     */
    @Command
    @NotifyChange({LEFT_PROPERTY, RIGHT_PROPERTY, LEFT_SELECTED_PROPERTY, RIGHT_SELECTED_PROPERTY})
    public void removeAll() {
        stateAfterEdit.removeAll(getRight());
        updateVm();
    }

    /**
     * Dummy command, used only for updating state of view components via binding. It's fired when user select item in
     * any of two list's in window.
     */
    @Command
    @NotifyChange({"leftSelected", "rightSelected"})
    public void listSelected() {
        // NOOP
    }

    // -- Utility methods -----------------------

    /**
     * Updates VM state after some major action.
     */
    
    protected void updateVm() {
        getRight().clear();
        getRight().addAll(getStateAfterEdit());

        getLeft().clear();
        getLeft().addAll(ListUtils.subtract(fullList, getStateAfterEdit()));
    
    }

    /**
     * Set the list of items after editing
     * @param stateAfterEdit list of groups after editing
     */
    public void setStateAfterEdit(List<Group> stateAfterEdit) {
        this.stateAfterEdit = stateAfterEdit;
    }
    /**
     * Set list of available items.
     *
     * @param left list of available items
     */
    public void setAvail(ListModelList<Group> left) {
        this.left = left;
    }
    /**
     * Set list of exist items.
     *
     * @param right list of exist items
     */
    public void setExist(ListModelList<Group> right) {
        this.right = right;
    }
    /**
     * Get list of right items after edit
     * @return list of groups after edit
     */
    public List<Group> getStateAfterEdit() {
        return stateAfterEdit;
    }
}
