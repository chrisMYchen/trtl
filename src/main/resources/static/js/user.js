function login(){
  /* Allow escape */
  $(document).keydown(function(e){
    if(e.which == 27){
      $("#login-wrapper").hide();
    }
  });

  $("#login-close").click(closeLoginDialog);

  $("#login-button").click(function(){
      closeSignupDialog();
      openLoginDialog();
  });

  $("#login-form").submit(loginSubmit);
}

function openLoginDialog(){
  $("#login-wrapper").show();

}

function closeLoginDialog(){
  $("#login-wrapper").hide();
  $("#login-form")[0].reset();
}

function loginSubmit(e){
  e.preventDefault();
  console.log(e);
}

function signup(){
  $(document).keydown(function(e){
    if(e.which == 27){
      closeSignupDialog();
    }
  });

  $("#signup-close").click(closeSignupDialog);

  $("#signup-button").click(function(){
    closeLoginDialog();
    openSignupDialog();
  });

  $("#signup-form input[name=uname]").on("input", usernameCheck);

  $("#signup-form").submit(signupSubmit);
}

function openSignupDialog(){
  $("#signup-wrapper").show();
}

function closeSignupDialog(){
  $("#signup-wrapper").hide();
  $("#signup-form")[0].reset();
}

function signupSubmit(e){
  e.preventDefault();
}

function usernameCheck(e){
  var inputElem = $(this);
  var uname = inputElem.val();


  if(uname.length > 3){
    $.post("/checkUsername", {username: uname}, function(response){
      var res = JSON.parse(response);
      console.log(res);
      if(res.error == "no-error"){
        if(res.exists){
          inputElem.css("background", "#F99");
        }
        else{
          inputElem.css("background", "#9F9");
        }
      }
    });
  }
  else{
    inputElem.css("background", "#fff");
  }
}
