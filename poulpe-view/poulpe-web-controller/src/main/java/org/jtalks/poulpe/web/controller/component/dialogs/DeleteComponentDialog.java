package org.jtalks.poulpe.web.controller.component.dialogs;

import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.service.exceptions.JcommuneRespondedWithErrorException;
import org.jtalks.poulpe.service.exceptions.JcommuneUrlNotConfiguredException;
import org.jtalks.poulpe.service.exceptions.NoConnectionToJcommuneException;
import org.jtalks.poulpe.web.controller.component.ComponentList;
import org.jtalks.poulpe.web.controller.component.ComponentsVm;
import org.jtalks.poulpe.web.controller.zkutils.BindUtilsWrapper;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;

/**
 * @author stanislav bashkirtsev
 */
public class DeleteComponentDialog implements EventListener<Event> {
    private static final String JCOMMUNE_CONNECTION_FAILED = "component.error.jcommune_no_connection";
    private static final String JCOMMUNE_RESPONSE_FAILED = "component.error.jcommune_no_response";
    private static final String JCOMMUNE_URL_FAILED = "component.error.jcommune_no_url";
    private static final String COMPONENT_DELETING_FAILED_DIALOG_TITLE = "component.deleting_problem_dialog.title";

    private final ComponentService componentService;
    private final ComponentList componentsToUpdate;
    private Component toDelete;

    public DeleteComponentDialog(ComponentService componentService, ComponentList componentsToUpdate) {
        this.componentService = componentService;
        this.componentsToUpdate = componentsToUpdate;
    }

    public void confirmDeletion(Component toDelete) {
        this.toDelete = toDelete;
        String title = String.format(Labels.getLabel("dialogmanager.delete.title"), toDelete.getName());
        String text = String.format(Labels.getLabel("dialogmanager.delete.question"), toDelete.getName());
        Messagebox.show(text, title, Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, Messagebox.NO, this);
    }

    public void deleteComponent() {
        try {
            componentService.deleteComponent(toDelete);
            componentsToUpdate.remove(toDelete);
            toDelete = null;
        } catch (NoConnectionToJcommuneException elementDoesNotExist) {
            showError(JCOMMUNE_CONNECTION_FAILED);
        } catch (JcommuneRespondedWithErrorException elementDoesNotExist) {
            showError(JCOMMUNE_RESPONSE_FAILED);
        } catch (JcommuneUrlNotConfiguredException elementDoesNotExist) {
            showError(JCOMMUNE_URL_FAILED);
        }
    }

    private void showError(String errorCode) {
        Messagebox.show(Labels.getLabel(errorCode),
                Labels.getLabel(COMPONENT_DELETING_FAILED_DIALOG_TITLE), Messagebox.OK, Messagebox.ERROR);
    }

    @Override
    public void onEvent(Event event) throws Exception {
        if ((Integer) event.getData() == Messagebox.YES) {
            deleteComponent();
        }
    }
}
