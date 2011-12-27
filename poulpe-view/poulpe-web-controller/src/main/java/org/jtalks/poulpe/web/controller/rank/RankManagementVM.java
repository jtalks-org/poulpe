/**
 * Copyright (C) 2011 JTalks.org Team This library is free software; you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version. This library is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package org.jtalks.poulpe.web.controller.rank;

import javax.annotation.Nonnull;
import org.jtalks.poulpe.model.entity.Rank;
import org.jtalks.poulpe.service.RankService;
import org.jtalks.poulpe.service.exceptions.NotUniqueException;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.ListModelList;

/**
 * View Model for rank management page.
 *
 * @author Pavel Vervenko
 */
public class RankManagementVM {

    ListModelList<Rank> items;
    private RankService rankService;
    private Rank selected;
    private Component editor;

    /**
     * Construct the object with injected service.
     *
     * @param rankService rankService
     */
    public RankManagementVM(@Nonnull RankService rankService) {
        this.rankService = rankService;
        initData();
    }

    /**
     * Get ranks list.
     *
     * @return ranks list
     */
    @NotifyChange
    public ListModelList<Rank> getItems() {
        return items;
    }

    /**
     * Load data to the list.
     */
    private void initData() {
        items = new ListModelList<Rank>(rankService.getAll());
    }

    @NotifyChange
    public Rank getSelected() {
        return selected;
    }

    public void setSelected(Rank selected) {
        this.selected = selected;
    }

    @Command
    public void edit() {
        openEditor();
    }

    @Command
    public void check() {
        
    }
    
    @Command
    public void dialogClosed() {
        editor = null;
    }
    
    @Command
    @NotifyChange
    public void save() throws NotUniqueException {
        rankService.saveRank(selected);
        dialogClosed();
        initData();
    }  
    
    @Command
    public void newItem() {
        selected = new Rank("new", 100);
        openEditor();
    }
    
    private Component getCurrentWindow() {
        for (Component c : Executions.getCurrent().getDesktop().getComponents()) {
            if (c.getId().equals("rankManagementWindow")) {
                return c;
            }
        }
        return null;
    }

    private void openEditor() {
        editor = Executions.getCurrent().createComponents("/RankEditor.zul", getCurrentWindow(), null);
    }
}
