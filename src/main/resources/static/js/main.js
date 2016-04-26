var locationInfo = {pos: null};
var userInfo = {id: -1};

$(function(){
    loginSetup();
    signupSetup();
    friendSetup();
    locationStart();
    displayNotes();
    postNotes();

    $("#branding").click(function(){
        location.href="/";
    });
});
