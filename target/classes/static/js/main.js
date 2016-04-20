var location;
var userInfo;

$(function(){
    login();
    signup();
    locationStart();
    displayNotes();
    postNotes();

});



function login(){
  $(document).keydown(function(e){
    if(e.which == 27){
      $("#login-wrapper").hide();
    }
  });

  $("#login-close").click(function(){
    $("#login-wrapper").hide();
  });

  $("#login-button").click(function(){
      $("#signup-wrapper").hide();
      $("#login-wrapper").show();
  });

  $("#login input[type=submit]").click(function(){
      $("#login-wrapper").hide();
  });
}

function signup(){
  $(document).keydown(function(e){
    if(e.which == 27){
      $("#signup-wrapper").hide();
    }
  });

  $("#signup-close").click(function(){
    $("#signup-wrapper").hide();
  });

  $("#signup-button").click(function(){
    $("#login-wrapper").hide();
    $("#signup-wrapper").show();
  });

  $("#signup input[type=submit]").click(function(){
      $("#signup-wrapper").hide();
  });
}
