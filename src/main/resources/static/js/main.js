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
        location.reload();
    });

    $(".menu-item").on("click", ".dropdown", function(e){
      e.stopPropagation();
    });

    $(".menu-item").on("click", function(e){
      $(this).children(".dropdown").toggle();
    });
});
