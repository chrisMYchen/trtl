var locationInfo = {pos: null};
var userInfo = null;

$(function(){
    loginSetup();
    signupSetup();
    locationStart();
    displayNotes();
    postNotes();

    $("#branding").click(function(){
        location.href="/";
    });
});
