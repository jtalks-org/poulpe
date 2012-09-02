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
package org.jtalks.poulpe.web.controller.component;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.Validate;
import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.model.entity.ComponentType;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.service.exceptions.JcommuneRespondedWithErrorException;
import org.jtalks.poulpe.service.exceptions.JcommuneUrlNotConfiguredException;
import org.jtalks.poulpe.service.exceptions.NoConnectionToJcommuneException;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.component.dialogs.AddComponentVm;
import org.jtalks.poulpe.web.controller.component.dialogs.DeleteComponentDialog;
import org.jtalks.poulpe.web.controller.component.dialogs.EditComponentVm;
import org.jtalks.poulpe.web.controller.zkutils.BindUtilsWrapper;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Messagebox;

/**
 * Adding, removing, editing and configuring of components.
 *
 * @author Alexey Grigorev
 * @author Leonid Kazantcev
 */
public class ComponentsVm {
    /**
     * These are the properties of the VM to specify in {@link NotifyChange}.
     */
    public static final String SELECTED = "selected";
    public static final String SHOW_REINDEX_START_NOTIFICATION = "showReindexStartedNotification";

    private static final String JCOMMUNE_REINDEX_NOT_CONNECTED_TITLE = "component.error.jcommune.title.not_connected";
    private static final String JCOMMUNE_REINDEX_NOT_CONNECTED_TEXT = "component.error.jcommune.text.not_connected";
    private static final String JCOMMUNE_REINDEX_NOT_CONFIGURED_TITLE = "component.error.jcommune.title.not_configured";
    private static final String JCOMMUNE_REINDEX_NOT_CONFIGURED_TEXT = "component.error.jcommune.text.not_configured";
    private static final String JCOMMUNE_REINDEX_ERROR_RESPONSE_TITLE = "component.error.jcommune.title.error_response";
    private static final String JCOMMUNE_REINDEX_ERROR_RESPONSE_TEXT = "component.error.jcommune.text.error_response";

    private static final String DEFAULT_NAME = "name";
    private static final String DEFAULT_DESCRIPTION = "descr";
    public static final String COMPONENTS_PAGE_LOCATION = "/WEB-INF/pages/component/components.zul";

    private final ComponentService componentService;
    private final DialogManager dialogManager;
    private final WindowManager windowManager;
    private final SelectedEntity<Component> selectedEntity;
    private boolean showReindexStartedNotification;

    private BindUtilsWrapper bindWrapper = new BindUtilsWrapper();
    private final ComponentList componentsToUpdate;
    private final DeleteComponentDialog deleteComponentDialog;
    private Component selected;

    /**
     * @param componentService service for loading and saving component
     * @param dialogManager    shows confirmation dialog for deletion
     * @param windowManager    object for opening and closing application windows
     * @param selectedEntity   desktop-scoped bean to which selected entities passed, used for editing components
     */
    public ComponentsVm(ComponentService componentService, DialogManager dialogManager, WindowManager windowManager,
                        SelectedEntity<Component> selectedEntity, ComponentList componentsToUpdate) {
        this.componentService = componentService;
        this.dialogManager = dialogManager;
        this.windowManager = windowManager;
        this.selectedEntity = selectedEntity;
        this.componentsToUpdate = componentsToUpdate;
        this.deleteComponentDialog = new DeleteComponentDialog(componentService, componentsToUpdate);
    }

    /**
     * @return {@link Component} with type ADMIN_PANEL if it exists, null otherwise
     */
    public Component getPoulpe() {
        return componentService.getByType(ComponentType.ADMIN_PANEL);
    }

    /**
     * @return true if ADMIN_PANEL component is exists, false otherwise
     */
    public boolean isPoulpeVisible() {
        return !(getPoulpe() == null);
    }

    /**
     * @return {@link Component} with type FORUM if it exists, null otherwise
     */
    public Component getJcommune() {
        return componentService.getByType(ComponentType.FORUM);
    }

    /**
     * @return true if FORUM component is exists, false otherwise
     */
    public boolean isJcommuneVisible() {
        return !(getJcommune() == null);
    }

    /**
     * Deletes selected component. Selected component is set using {@link #setSelected(Component)}.
     *
     * @throws IllegalStateException if no component selected
     */
    @Command
    public void deleteComponent() {
        Validate.validState(selected != null, "entity to delete must be selected");
        deleteComponentDialog.confirmDeletion(selected, this);
    }

    /**
     * Sending empty httpRequest to jcommune, this request starts re-index process at jcommune.
     */
    @Command
    public void reindexComponent() {
        try {
            componentService.reindexComponent((Jcommune) selected);
            showReindexStartedNotification();

        } catch (NoConnectionToJcommuneException e) {
            Messagebox.show(Labels.getLabel(JCOMMUNE_REINDEX_NOT_CONNECTED_TEXT),
                    Labels.getLabel(JCOMMUNE_REINDEX_NOT_CONNECTED_TITLE), Messagebox.OK, Messagebox.ERROR);
        } catch (JcommuneUrlNotConfiguredException e) {
            Messagebox.show(Labels.getLabel(JCOMMUNE_REINDEX_NOT_CONFIGURED_TEXT),
                    Labels.getLabel(JCOMMUNE_REINDEX_NOT_CONFIGURED_TITLE), Messagebox.OK, Messagebox.ERROR);
        } catch (JcommuneRespondedWithErrorException e) {
            Messagebox.show(Labels.getLabel(JCOMMUNE_REINDEX_ERROR_RESPONSE_TEXT) + e.getMessage(),
                    Labels.getLabel(JCOMMUNE_REINDEX_ERROR_RESPONSE_TITLE), Messagebox.OK, Messagebox.ERROR);
        }
    }

    /**
     * Shows a window for adding {@link org.jtalks.poulpe.model.entity.Poulpe} component.
     */
    @Command
    public void addNewPoulpe() {
        selectedEntity.setEntity(componentService.
                baseComponentFor(ComponentType.ADMIN_PANEL).newComponent(DEFAULT_NAME, DEFAULT_DESCRIPTION));
        AddComponentVm.openWindowForAdding(windowManager);
    }

    /**
     * Shows a window for adding {@link org.jtalks.poulpe.model.entity.Jcommune} component.
     */
    @Command
    public void addNewJcommune() {
        selectedEntity.setEntity(componentService.
                baseComponentFor(ComponentType.FORUM).newComponent(DEFAULT_NAME, DEFAULT_DESCRIPTION));
        AddComponentVm.openWindowForAdding(windowManager);
    }

    /**
     * Shows a component edit window for currently selected element. Selected component is set using {@link
     * #setSelected(Component)}.
     */
    @Command
    public void configureComponent() {
        selectedEntity.setEntity(selected);
        EditComponentVm.openWindowForEdit(windowManager);
    }

    /**
     * @param selected currently selected component
     */
    public void setSelected(Component selected) {
        this.selected = selected;
    }

    /**
     * @param bindWrapper {@link BindUtilsWrapper} instance to set
     */
    @VisibleForTesting
    void setBindWrapper(BindUtilsWrapper bindWrapper) {
        this.bindWrapper = bindWrapper;
    }

    /**
     * Opens Components window.
     *
     * @param windowManager implementation of {@link WindowManager} for opening and closing application windows
     */
    public static void show(WindowManager windowManager) {
        windowManager.open(COMPONENTS_PAGE_LOCATION);
    }

    /**
     * @return true if component {@link org.jtalks.poulpe.model.entity.Jcommune} with {@link ComponentType} FORUM type
     *         are not created yet, false otherwise
     */
    public boolean isJcommuneAvailable() {
        return componentService.getAvailableTypes().contains(ComponentType.FORUM);
    }

    @NotifyChange(SHOW_REINDEX_START_NOTIFICATION)
    private void showReindexStartedNotification() {
        showReindexStartedNotification = true;
    }

    /**
     * Gets visibility status of notification window, boolean show added because after single opening of popup window
     * before next check we should have false at showReindexStartedNotification.
     *
     * @return true if notification is visible
     */
    public boolean isShowReindexStartedNotification() {
        boolean show = showReindexStartedNotification;
        showReindexStartedNotification = false;
        return show;
    }
}