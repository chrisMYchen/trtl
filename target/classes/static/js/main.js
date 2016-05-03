var locationInfo = {pos: null};
var userInfo = {id: -1};

$(function(){
    loginSetup();
    signupSetup();
    followSetup();
    locationStart();
    displayNotes();
    postNotes();
    image();

    $("#branding").click(function(){
        location.reload();
    });

    $(".menu-item").on("click", ".dropdown", function(e){
      e.stopPropagation();
    });

    $(".menu-item").on("click", function(e){
      $(this).toggleClass("active");
      $(this).children(".dropdown").toggle();
    });

    $("body").on("click", function(e){
      if($(e.target).closest(".menu-item").length == 0){
        $(".dropdown").hide();
        $(".menu-item").toggleClass("active", false);
      }
    });
});
