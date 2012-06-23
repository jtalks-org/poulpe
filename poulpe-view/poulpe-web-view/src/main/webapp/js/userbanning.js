var customKeyDown = function () {
    var e = jQuery.Event("keydown", { keyCode:40 });
    $('tr.filter-list').first().find('td').trigger(e);
}
var focusEvents = function () {
    $('.z-bandbox-inp').keydown(function (e) {
        $('.z-listitem-seld').removeClass('z-listbox-odd');
        $('.z-listitem-seld').removeClass('z-listitem-seld');
        $('.z-listitem-focus').removeClass('z-listitem-focus');
        if (e.which == 40 || e.which == 38) {
            $('.z-bandpopup').attr('tabindex', -1).focus();
            setTimeout("customKeyDown()", 10);
        }
    });

    $('.z-bandpopup').keydown(function (e) {
        if ((e.which >= 49 && e.which <= 111) || e.which == 8) {
            $('.z-bandbox-inp').focus();
        }
    });
}

setTimeout("focusEvents()", 500)