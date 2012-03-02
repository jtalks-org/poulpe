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

import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModelList;

import com.google.common.collect.Lists;

/**
 * VM represents two list's with possibility to add or remove item's from some consistent state. It's also provides
 * filtering these list's in View for easy search. After user completed his actions we are have consistent state, stored
 * in internal field {@code consistentState}. This consistent state may be fixed (for example in persistent layer) or
 * ignored if VM simply closed without save action. In any time consistent state may be obtained by getter.
 * 
 * @author Vyacheslav Zhivaev
 */
public class TwoSideListWithFilterVM<E> {

    /** String represents text in filter field for available items list. */
    protected String availFilterTxt;

    /** String represents text in filter field for exist items list. */
    protected String existFilterTxt;

    /** List of available for adding items. */
    protected ListModelList<E> avail;

    /** List of already existed (added) items. */
    protected ListModelList<E> exist;

    /** List represents consistent state of existed items after editing. */
    protected List<E> consistentState;

    /**
     * Constructs VM with simple initialization which avoiding {@code null} values in internal fields.
     */
    public TwoSideListWithFilterVM() {
        availFilterTxt = "";
        existFilterTxt = "";
        avail = new BindingListModelList<E>(Lists.<E> newLinkedList(), false);
        exist = new BindingListModelList<E>(Lists.<E> newLinkedList(), false);
        consistentState = Lists.<E> newLinkedList();
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
        return consistentState;
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
    @NotifyChange({ "avail", "exist", "availSelected", "existSelected" })
    public void add() {
        consistentState.addAll(getAvailSelected());
        updateVm();
    }

    /**
     * Adds all available items in consistent state.
     */
    @Command
    @NotifyChange({ "avail", "exist", "availSelected", "existSelected" })
    public void addAll() {
        consistentState.addAll(getAvail());
        updateVm();
    }

    /**
     * Removes selected item from consistent state.
     */
    @Command
    @NotifyChange({ "avail", "exist", "availSelected", "existSelected" })
    public void remove() {
        consistentState.removeAll(getExistSelected());
        updateVm();
    }

    /**
     * Removes all selected items from consistent state.
     */
    @Command
    @NotifyChange({ "avail", "exist", "availSelected", "existSelected" })
    public void removeAll() {
        consistentState.removeAll(getExist());
        updateVm();
    }

    // -- Utility methods -----------------------

    /**
     * Updates VM state after some major action.
     */
    protected void updateVm() {
        // This is created as template, redefine it if it needs.
        // NOOP
    }

}
