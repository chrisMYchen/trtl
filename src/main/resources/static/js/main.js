var locationInfo = {pos: null};
var userInfo = {id: -1};

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
