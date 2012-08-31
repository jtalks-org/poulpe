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

public class DualListVm{

    public static final String CANDIDATE_PROPERTY = "candidate", CHOSEN_PROPERTY = "chosen";
    public static final String CANDIDATE_SELECTED_PROPERTY = "candidateSelected", CHOSEN_SELECTED_PROPERTY = "chosenSelected";
    /**
     * String represents text in filter field for available items list.
     */
    private String candidateFilterTxt = "";
    /**
     * String represents text in filter field for exist items list.
     */
    private String chosenFilterTxt = "";

    /**
     * List of available for adding items.
     */
    private ListModelList<Group> candidate = new BindingListModelList<Group>(new ArrayList<Group>(), false);

    /**
     * List of already existed (added) items.
     */
    private ListModelList<Group> chosen = new BindingListModelList<Group>(new ArrayList<Group>(), false);

    /**
     * List represents state of existed items after editing.
     */
    private List<Group> stateAfterEdit = new ArrayList<Group>();
    private List<Group> fullList = new ArrayList<Group>();

    /**
     * Constructs VM with simple initialization which avoiding {@code null} values in internal fields.
     */
    
    public DualListVm() {
        candidate.setMultiple(true);
        chosen.setMultiple(true);
    }
    
    /**
     * Fill ModelLists for component 
     * 
     * @param candidateList list of groups that not added to permissions
     * @param chosenList list of groups already added to permissions
     */    
    @Init
    public void initVm(@BindingParam("candidateList") List<Group> candidateList, @BindingParam("chosenList") List<Group> chosenList){
        this.candidate.clear();
        this.candidate.addAll(candidateList);
        
        this.chosen.clear();
        this.chosen.addAll(chosenList); 
        
        this.stateAfterEdit.clear();
        this.stateAfterEdit.addAll(chosenList); 
        
        this.fullList = ListUtils.union(candidateList, chosenList);
    }
    
    // -- Accessors -----------------------------

    /**
     * Gets filter text for available list.
     *
     * @return text for filter field
     */
    public String getAvailFilterTxt() {
        return candidateFilterTxt;
    }

    /**
     * Sets filter text for available list.
     *
     * @param candidateFilterTxt the filter text to set
     */
    public void setAvailFilterTxt(@Nonnull String candidateFilterTxt) {
        this.candidateFilterTxt = candidateFilterTxt;
    }

    /**
     * Gets filter text for a list of existing items.
     *
     * @return text for filter field
     */
    public String getExistFilterTxt() {
        return chosenFilterTxt;
    }

    /**
     * Sets filter text for a list of existing items.
     *
     * @param chosenFilterTxt the filter text to set
     */
    public void setExistFilterTxt(@Nonnull String chosenFilterTxt) {
        this.chosenFilterTxt = chosenFilterTxt;
    }

    /**
     * Gets list of available items.
     *
     * @return the list of available items
     */
    public ListModelList<Group> getCandidate() {
        return candidate;
    }

    /**
     * Gets list of existing items.
     *
     * @return the list of chosen items
     */
    public ListModelList<Group> getChosen() {
        return chosen;
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
    public Set<Group> getCandidateSelected() {
        return candidate.getSelection();
    }

    /**
     * Gets set of selected items in existed list.
     *
     * @return set of selected items in existed list
     */
    public Set<Group> getChosenSelected() {
        return chosen.getSelection();
    }

    // -- ZK Commands ---------------------------

    /**
     * Adds available selected item in consistent state.
     */
    @Command
    @NotifyChange({CANDIDATE_PROPERTY, CHOSEN_PROPERTY, CANDIDATE_SELECTED_PROPERTY, CHOSEN_SELECTED_PROPERTY})
    public void add() {
        stateAfterEdit.addAll(getCandidateSelected());
        updateVm();
    }

    /**
     * Adds all available items in consistent state.
     */
    @Command
    @NotifyChange({CANDIDATE_PROPERTY, CHOSEN_PROPERTY, CANDIDATE_SELECTED_PROPERTY, CHOSEN_SELECTED_PROPERTY})
    public void addAll() {
        stateAfterEdit.addAll(getCandidate());
        updateVm();
    }

    /**
     * Removes selected item from consistent state.
     */
    @Command
    @NotifyChange({CANDIDATE_PROPERTY, CHOSEN_PROPERTY, CANDIDATE_SELECTED_PROPERTY, CHOSEN_SELECTED_PROPERTY})
    public void remove() {
        stateAfterEdit.removeAll(getChosenSelected());
        updateVm();
    }

    /**
     * Removes all selected items from consistent state.
     */
    @Command
    @NotifyChange({CANDIDATE_PROPERTY, CHOSEN_PROPERTY, CANDIDATE_SELECTED_PROPERTY, CHOSEN_SELECTED_PROPERTY})
    public void removeAll() {
        stateAfterEdit.removeAll(getChosen());
        updateVm();
    }

    /**
     * Dummy command, used only for updating state of view components via binding. It's fired when user select item in
     * any of two list's in window.
     */
    @Command
    @NotifyChange({"candidateSelected", "chosenSelected"})
    public void listSelected() {
        // NOOP
    }

    // -- Utility methods -----------------------

    /**
     * Updates VM state after some major action.
     */
    
    protected void updateVm() {
        getChosen().clear();
        getChosen().addAll(getStateAfterEdit());

        getCandidate().clear();
        getCandidate().addAll(ListUtils.subtract(fullList, getStateAfterEdit()));
    
    }

    public void setStateAfterEdit(List<Group> stateAfterEdit) {
        this.stateAfterEdit = stateAfterEdit;
    }
    /**
     * Set list of available items.
     *
     * @param candidate list of available items
     */
    public void setAvail(ListModelList<Group> candidate) {
        this.candidate = candidate;
    }
    /**
     * Set list of exist items.
     *
     * @param chosen list of exist items
     */
    public void setExist(ListModelList<Group> chosen) {
        this.chosen = chosen;
    }
    /**
     * Get list of chosen items after edit
     * @return list of groups after edit
     */
    public List<Group> getStateAfterEdit() {
        return stateAfterEdit;
    }
}
