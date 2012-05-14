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

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModelList;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * VM represents two list's with possibility to add or remove item's from some consistent state. It's also provides
 * filtering these list's in View for easy search. After user completed his actions we are have consistent state, stored
 * in internal field {@code stateAfterEdit}. This consistent state may be fixed (for example in persistent layer) or
 * ignored if VM simply closed without save action. In any time consistent state may be obtained by getter.
 *
 * @author Vyacheslav Zhivaev
 */
public class TwoSideListWithFilterVm<E> {
    public static final String AVAIL_PROPERTY = "avail", EXIST_PROPERTY = "exist",
            AVAIL_SELECTED_PROPERTY = "availSelected", EXIST_SELECTED_PROPERTY = "existSelected";
    /**
     * String represents text in filter field for available items list.
     */
    private String availFilterTxt = "";
    /**
     * String represents text in filter field for exist items list.
     */
    private String existFilterTxt = "";

    /**
     * List of available for adding items.
     */
    private ListModelList<E> avail = new BindingListModelList<E>(new ArrayList<E>(), false);

    /**
     * List of already existed (added) items.
     */
    private ListModelList<E> exist = new BindingListModelList<E>(new ArrayList<E>(), false);

    /**
     * List represents state of existed items after editing.
     */
    private List<E> stateAfterEdit = new ArrayList<E>();

    /**
     * Constructs VM with simple initialization which avoiding {@code null} values in internal fields.
     */
    public TwoSideListWithFilterVm() {
        avail.setMultiple(true);
        exist.setMultiple(true);
    }

    // -- Accessors -----------------------------

    /**
     * Gets filter text for available list.
     *
     * @return text for filter field
     */
    public String getAvailFilterTxt() {
        return availFilterTxt;
    }

    /**
     * Sets filter text for available list.
     *
     * @param availFilterTxt the filter text to set
     */
    public void setAvailFilterTxt(@Nonnull String availFilterTxt) {
        this.availFilterTxt = availFilterTxt;
    }

    /**
     * Gets filter text for a list of existing items.
     *
     * @return text for filter field
     */
    public String getExistFilterTxt() {
        return existFilterTxt;
    }

    /**
     * Sets filter text for a list of existing items.
     *
     * @param existFilterTxt the filter text to set
     */
    public void setExistFilterTxt(@Nonnull String existFilterTxt) {
        this.existFilterTxt = existFilterTxt;
    }

    /**
     * Gets list of available items.
     *
     * @return the list of available items
     */
    public ListModelList<E> getAvail() {
        return avail;
    }

    /**
     * Gets list of existing items.
     *
     * @return the list of existed items
     */
    public ListModelList<E> getExist() {
        return exist;
    }

    /**
     * Gets list of items in consistent state (after editing).
     *
     * @return the list of items in consistent state (after editing)
     */
    protected List<E> getConsistentState() {
        return stateAfterEdit;
    }

    /**
     * Gets set of selected items in available list.
     *
     * @return set of selected items in available list
     */
    public Set<E> getAvailSelected() {
        return avail.getSelection();
    }

    /**
     * Gets set of selected items in existed list.
     *
     * @return set of selected items in existed list
     */
    public Set<E> getExistSelected() {
        return exist.getSelection();
    }

    // -- ZK Commands ---------------------------

    /**
     * Adds available selected item in consistent state.
     */
    @Command
    @NotifyChange({AVAIL_PROPERTY, EXIST_PROPERTY, AVAIL_SELECTED_PROPERTY, EXIST_SELECTED_PROPERTY})
    public void add() {
        stateAfterEdit.addAll(getAvailSelected());
        updateVm();
    }

    /**
     * Adds all available items in consistent state.
     */
    @Command
    @NotifyChange({AVAIL_PROPERTY, EXIST_PROPERTY, AVAIL_SELECTED_PROPERTY, EXIST_SELECTED_PROPERTY})
    public void addAll() {
        stateAfterEdit.addAll(getAvail());
        updateVm();
    }

    /**
     * Removes selected item from consistent state.
     */
    @Command
    @NotifyChange({AVAIL_PROPERTY, EXIST_PROPERTY, AVAIL_SELECTED_PROPERTY, EXIST_SELECTED_PROPERTY})
    public void remove() {
        stateAfterEdit.removeAll(getExistSelected());
        updateVm();
    }

    /**
     * Removes all selected items from consistent state.
     */
    @Command
    @NotifyChange({AVAIL_PROPERTY, EXIST_PROPERTY, AVAIL_SELECTED_PROPERTY, EXIST_SELECTED_PROPERTY})
    public void removeAll() {
        stateAfterEdit.removeAll(getExist());
        updateVm();
    }

    /**
     * Dummy command, used only for updating state of view components via binding. It's fired when user select item in
     * any of two list's in window.
     */
    @Command
    @NotifyChange({"availSelected", "existSelected"})
    public void listSelected() {
        // NOOP
    }

    // -- Utility methods -----------------------

    /**
     * Updates VM state after some major action.
     */
    protected void updateVm() {
        // This is created as template, redefine it for your needs.
        // NOOP
    }

    public void setStateAfterEdit(List<E> stateAfterEdit) {
        this.stateAfterEdit = stateAfterEdit;
    }

    public void setAvail(ListModelList<E> avail) {
        this.avail = avail;
    }

    public void setExist(ListModelList<E> exist) {
        this.exist = exist;
    }

    public List<E> getStateAfterEdit() {
        return stateAfterEdit;
    }
}
