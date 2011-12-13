package org.jtalks.poulpe.web.controller.userbanning;

import java.util.ArrayList;
import java.util.List;

import org.jtalks.common.model.entity.User;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

@SuppressWarnings("serial")
public class UserBanningViewImpl extends Window implements UserBanningView, AfterCompose {

    private UserBanningPresenter presenter;
    private Combobox userCombobox;
    private Listbox usersToBan;
    private Checkbox permanent;
    private Intbox banLength;
    private Textbox banReason;
    private Button addButton;
    private Button submitButton;
    private Button resetButton;

    private ListModelList<User> modelUserBanning;

    private List<User> usersToBanList;

    @Override
    public void afterCompose() {
        Components.addForwards(this, this);
        Components.wireVariables(this, this);
        usersToBanList = new ArrayList<User>();

        userCombobox.setItemRenderer(new ComboitemRenderer<User>() {
            @Override
            public void render(Comboitem item, User user) throws Exception {
                item.setLabel(user.getUsername());
                item.setValue(user);
            }
        });

        usersToBan.setItemRenderer(new ListitemRenderer<User>() {
            @Override
            public void render(Listitem item, User user) throws Exception {
                Listcell cell_1 = new Listcell(user.getUsername());
                Listcell cell_2 = new Listcell(user.getEmail());
                Listcell cell_3 = new Listcell("NOT IMPLEMENTED YET");
                Listcell cell_4 = new Listcell("NOT IMPLEMENTED YET");
                item.appendChild(cell_1);
                item.appendChild(cell_2);
                item.appendChild(cell_3);
                item.appendChild(cell_4);
            }
        });
        presenter.initView(this);
    }

    @Override
    public void updateView(final List<User> users) {
        userCombobox.setModel(new BindingListModelList(users, false));
        modelUserBanning = new ListModelList<User>(usersToBanList);
        usersToBan.setModel(modelUserBanning);
        addButton.setDisabled(true);
        manageButtons();
    }

    public void refreshView() {
        userCombobox.setValue(null);
        addButton.setDisabled(true);
        modelUserBanning.clear();
        modelUserBanning.addAll(usersToBanList);
        manageButtons();
    }

    public void manageButtons() {
        if (usersToBanList.isEmpty()) {
            submitButton.setDisabled(true);
            resetButton.setDisabled(true);
        } else {
            submitButton.setDisabled(false);
            resetButton.setDisabled(false);
        }
    }

    public void onClick$addButton() {
        Object selectedInCombo = userCombobox.getModel().getElementAt(userCombobox.getSelectedIndex());
        if (validateComboboxValue(selectedInCombo)) {
            usersToBanList.add((User) selectedInCombo);
            refreshView();
        }
    }

    public void onClick$permanent() {
        // banLength.setValue(null);
        if (permanent.isChecked()) {
            banLength.setDisabled(true);
        } else {
            banLength.setDisabled(false);
        }
    }

    public void onClick$submitButton() {
        if (validateBanForm()) {
            presenter.banBasters(usersToBanList, permanent.isChecked(), banLength.getValue(), banReason.getValue());
        } else {

        }
    }

    public void onClick$resetButton() {
        clearView();
    }

    public void clearView() {
        usersToBanList.clear();
        refreshView();
    }

    public void onSelect$userCombobox() {
        if (userCombobox.getSelectedIndex() < 0)
            return;
        Object selectedInCombo = userCombobox.getModel().getElementAt(userCombobox.getSelectedIndex());
        if (validateComboboxValue(selectedInCombo)) {
            addButton.setDisabled(false);
        }
    }

    public boolean validateBanForm() {
        if (banReason.getValue() == null || banReason.getValue().equals("")) {
            banReason.setErrorMessage(Labels.getLabel("userbanning.validation.reason_cant_be_void"));
            return false;
        }
        if (permanent.isChecked()) {
            return true;
        } else if (banLength.getValue() == null || banLength.getValue() == 0) {
            banLength.setErrorMessage(Labels.getLabel("userbanning.validation.wrong_ban_length"));
            return false;
        }
        return true;
    }

    public boolean validateComboboxValue(Object value) {
        if (value == null)
            return false;
        if (!(value instanceof User))
            return false;
        if (((User) value).isPermanentBan()) {
            userCombobox.setErrorMessage(Labels.getLabel("userbanning.validation.user_already_banned"));
            return false;
        }
        if (usersToBanList.contains(value)) {
            userCombobox.setErrorMessage(Labels.getLabel("userbanning.validation.user_already_within_ban_list"));
            return false;
        }
        return true;
    }

    public UserBanningPresenter getPresenter() {
        return presenter;
    }

    public void setPresenter(UserBanningPresenter presenter) {
        this.presenter = presenter;
    }

    public void setUserCombobox(Combobox combo) {
        this.userCombobox = combo;
    }

    public void setUsersToBan(Listbox list) {
        this.usersToBan = list;
    }

    public void setPermanent(Checkbox check) {
        this.permanent = check;
    }

    public void setBanLength(Intbox box) {
        this.banLength = box;
    }

    public void setBanReason(Textbox box) {
        this.banReason = box;
    }
}
