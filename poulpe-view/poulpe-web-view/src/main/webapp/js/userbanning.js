/**
 * Method to emulate KeyDown event to listbox of users to ban in filter.
 */
var customKeyDown = function () {
    //keyCode 40 it is code of key Down (arrow down)
    var e = jQuery.Event("keydown", { keyCode:40 });
    $('tr.filter-list').first().find('td').trigger(e);
}
/**
 *  Method sets listeners to textbox and listbox of filter
 */
var focusEvents = function () {
    //add listener to textbox
    $('.z-bandbox-inp').keydown(function (e) {
        //do remove classes to select from listbox
        $('.z-listitem-seld').removeClass('z-listbox-odd');
        $('.z-listitem-seld').removeClass('z-listitem-seld');
        $('.z-listitem-focus').removeClass('z-listitem-focus');
        //which it is keyCode. If we have focus on textbox and press key Down or Up (arrows), we do focus to listbox
        if (e.which == 40 || e.which == 38) {
            $('.z-bandpopup').attr('tabindex', -1).focus();
            //after that, emulate ress key Down to listbox (for select)
            setTimeout("customKeyDown()", 10);
        }
    });

    //add listener to listbox
    $('.z-bandpopup').keydown(function (e) {
        //which it is keyCode. 49..111 keys with chars to input. 8 it is Baskspace key.
        if ((e.which >= 49 && e.which <= 111) || e.which == 8) {
            //do focus of textbox
            $('.z-bandbox-inp').focus();
        }
    });
}

//after load script, run method to add listeners
setTimeout("focusEvents()", 500)