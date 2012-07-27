//method to set value of textbox after select user in listbox
void selectUserToBan()
{
    usersPopupToBan.setValue(usersListBoxToBan.getSelectedItem().getLabel());
    usersPopupToBan.close();
    usersPopupToBan.focus();
    banUserButton.setDisabled(false);
}

//method runs after listbox is open
void popupWithUsersToBanOpen()
{
    usersListBoxToBan.focus();
    addUserToBannedGroupButton.setDisabled(true);
}