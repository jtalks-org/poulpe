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
 * in internal field {@code afterEdit}. This consistent state may be fixed (for example in persistent layer) or ignored
 * if VM simply closed without save action. In any time consistent state may be obtained by getter.
 * 
 * @author Vyacheslav Zhivaev
 */
public class TwoSideListWithFilterVM<E> {

    /** String represents text in filter filed for available items list. */
    protected String availFilter;

    /** String represents text in filter filed. */
    protected String existFilter;

    /** List of available for adding items. */
    protected ListModelList<E> avail;

    /** List of already existed (added) items. */
    protected ListModelList<E> exist;

    /** List represents state of existed items after editing. */
    protected List<E> afterEdit;

    /**
     * Constructs VM with simple initialization which avoiding {@code null} values.
     */
    public TwoSideListWithFilterVM() {
        availFilter = "";
        existFilter = "";
        avail = new BindingListModelList<E>(Lists.<E> newLinkedList(), false);
        exist = new BindingListModelList<E>(Lists.<E> newLinkedList(), false);
        afterEdit = Lists.<E> newLinkedList();
    }

    // -- Accessors -----------------------------

    /**
     * Gets filter string for available list.
     * 
     * @return String for filter field
     */
    public String getAvailFilter() {
        return availFilter;
    }

    /**
     * Sets filter string for available list.
     * 
     * @param availFilter the filter string to set
     */
    public void setAvailFilter(@Nonnull String availFilter) {
        this.availFilter = availFilter;
    }

    /**
     * Gets filter string for a list of existing items.
     * 
     * @return String for filter field
     */
    public String getExistFilter() {
        return existFilter;
    }

    /**
     * Sets filter string for a list of existing items.
     * 
     * @param existFilter the filter string to set
     */
    public void setExistFilter(@Nonnull String existFilter) {
        this.existFilter = existFilter;
    }

    /**
     * Gets list of available items.
     * 
     * @return the avail
     */
    public ListModelList<E> getAvail() {
        return avail;
    }

    /**
     * Gets list of existing items.
     * 
     * @return the exist
     */
    public ListModelList<E> getExist() {
        return exist;
    }

    /**
     * @return the afterEdit
     */
    public List<E> getAfterEdit() {
        return afterEdit;
    }

    /**
     * @return
     */
    public Set<E> getAvailSelected() {
        return avail.getSelection();
    }

    /**
     * @return
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
        afterEdit.addAll(getAvailSelected());
        updateVM();
    }

    /**
     * Adds all available items in consistent state.
     */
    @Command
    @NotifyChange({ "avail", "exist", "availSelected", "existSelected" })
    public void addAll() {
        afterEdit.addAll(getAvail());
        updateVM();
    }

    /**
     * Removes selected item from consistent state.
     */
    @Command
    @NotifyChange({ "avail", "exist", "availSelected", "existSelected" })
    public void remove() {
        afterEdit.removeAll(getExistSelected());
        updateVM();
    }

    /**
     * Removes all selected items from consistent state.
     */
    @Command
    @NotifyChange({ "avail", "exist", "availSelected", "existSelected" })
    public void removeAll() {
        afterEdit.removeAll(getExist());
        updateVM();
    }

    // -- Utility methods -----------------------

    /**
     * Updates VM state after some major action.
     */
    protected void updateVM() {
        // This is created as template, redefine it if it needs.
        // NOOP
    }

}
