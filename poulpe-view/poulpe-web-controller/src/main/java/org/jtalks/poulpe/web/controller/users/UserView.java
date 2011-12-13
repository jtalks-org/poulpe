package org.jtalks.poulpe.web.controller.users;

public interface UserView {

    void showFirstname(String firstname);

    void showLastname(String lastname);

    void showEmail(String email);

    void showPassword(String password);

    String getFirstname();

    String getLastname();

    String getEmail();

    String getPassword();

    void hideEditAction();
}