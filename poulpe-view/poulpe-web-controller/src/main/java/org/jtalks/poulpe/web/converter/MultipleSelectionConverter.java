package org.jtalks.poulpe.web.converter;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.Converter;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import java.util.Arrays;
import java.util.Set;

/**
 * Created by IntelliJ IDEA. User: ctapobep Date: 12/22/11 Time: 5:27 PM To change this template use File | Settings |
 * File Templates.
 */
public class MultipleSelectionConverter implements Converter {

    /**
     * Returns empty all the time.
     */
    @Override
    public Object coerceToUi(Object val, Component component, BindContext ctx) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object coerceToBean(Object val, Component component, BindContext ctx) {
        Set<Listitem> selectedItems = getSelectedItems(component);
        return fetchIndexesFromItems(selectedItems);
    }

    private int[] fetchIndexesFromItems(Set<Listitem> selectedItems) {
        int[] selectedIndexes = new int[selectedItems.size()];
        int counter = 0;
        for (Listitem nextItem : selectedItems) {
            selectedIndexes[counter++] = nextItem.getIndex();
        }
        Arrays.sort(selectedIndexes);
        return selectedIndexes;
    }

    @SuppressWarnings("unchecked")
    private Set<Listitem> getSelectedItems(Component component) {
        return ((Listbox) component).getSelectedItems();
    }
}
