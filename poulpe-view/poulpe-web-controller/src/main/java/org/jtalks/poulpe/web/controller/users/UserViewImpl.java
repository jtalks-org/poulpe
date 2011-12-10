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
package org.jtalks.poulpe.web.controller.users;

import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * 
 * @author Vytautas Kashchuk
 */
public class UserViewImpl extends Window implements UserPresenter.UserView {

    private static final long serialVersionUID = 1L;

    private Textbox firstnameTextbox;
    private Textbox lastnameTextbox;
    private Textbox emailTextbox;
    private Textbox passwordTextbox;
    private Button editButton;

    @Override
    public void hideEditAction() {
        editButton.setVisible(false);
    }

    @Override
    public void showFirstname(String firstname) {
        this.firstnameTextbox.setText(firstname);
    }

    @Override
    public void showLastname(String lastname) {
        this.lastnameTextbox.setText(lastname);
    }

    @Override
    public void showEmail(String email) {
        this.emailTextbox.setText(email);
    }

    @Override
    public void showPassword(String password) {
        this.passwordTextbox.setText(password);
    }

    @Override
    public String getFirstname() {
        return this.firstnameTextbox.getText();
    }

    @Override
    public String getLastname() {
        return this.lastnameTextbox.getText();
    }

    @Override
    public String getEmail() {
        return this.emailTextbox.getText();
    }

    @Override
    public String getPassword() {
        return this.passwordTextbox.getText();
    }
}
