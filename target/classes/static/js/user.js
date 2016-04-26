/*************************/
/** Login Functionality **/
/*************************/
function loginSetup(){
  /* Allow escape */
  $(document).keydown(function(e){
    if(e.which == 27){
      $("#login-wrapper").hide();
    }
  });

  /* Close login dialog on "X click */
  $("#login-close").click(closeLoginDialog);

  /* Open login dialog on from nav */
  $("#login-button").click(function(){
      closeSignupDialog();
      openLoginDialog();
  });

  /* Submit login info */
  $("#login-form").submit(loginSubmit);

  /* Cookies! */
  checkLoginCookie();

  /* Logout*/
  $("#logout-button").click(logout)
}

function testLogin(){
  $("")
}

function openLoginDialog(){
  $("#login-wrapper").show();
}

function closeLoginDialog(){
  $("#login-wrapper").hide();
  $("#login-form")[0].reset();
  $("#login-error").hide();
  $("#login-error").empty();
}

function loginSubmit(e){
  e.preventDefault();
  $("#login-error").hide();
  $("#login-error").empty();
  sendLogin();
}

function sendLogin(){
  var data = $("#login-form").serialize();
  console.log(data);
  $.post("/login", data, function(response){
    var res = JSON.parse(response);
    console.log(res);
    if(res.error = "no-error"){
      login(res.userID);
      closeLoginDialog();
    }
    else{
      loginError(res.error);
    }
  });
}

function login(userID){
  userInfo = {id: userID};
  setLoginMode(true);
  setLoginCookie(userID);
}

function loginError(message){
  var body = $("<p></p>").html(message);
  var elem = $("#login-error");
  elem.empty();
  elem.append(body);
  elem.show();
}

function setLoginMode(value){
  if(value){
    $("#account-links").hide();
    $("#user-name").html("Welcome " + userInfo.id);
    $(".loggedin").show();
    $('.loggedout').hide();
  }
  else{
    $("#user-name").html("");
    $(".loggedin").hide();
    $('.loggedout').show();
  }
}

function getUserInfo(userID){

}

function logout(){
  removeLoginCookie();
  setLoginMode(false);
}


/***********************/
/** Cookie Management **/
/***********************/
function checkLoginCookie(){
  var cookie = getCookie("userid");
  console.log(cookie);
  if (cookie != null){
    login(parseInt(cookie));
  }
}

function setLoginCookie(userID){
  var cookie = "userid=" + userID + ";";
  console.log(cookie);
  document.cookie = cookie;
}

function removeLoginCookie(){
  document.cookie = "userid=; expires=Thu, 01 Jan 1970 00:00:00 UTC";
}

function getCookie(name){
  var cname = name +"=";
  var cookie = document.cookie.split(";");
  for( var i = 0; i < cookie.length; i++){
    var c = cookie[i];
    while (c.charAt(0)==' ') {
      c = c.substring(1);
    }
    if (c.indexOf(cname) == 0) {
      return c.substring(cname.length,c.length);
    }
  }
    return null;
}

/*********************************/
/** Signup and Account Creation **/
/*********************************/

function signupSetup(){
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
  $("#signup-error").hide();
  $("#signup-error").empty();

}

function signupSubmit(e){
  e.preventDefault();
  sendSignup();
}

function sendSignup(){
  var data = $("#signup-form").serialize();
  console.log(data);
  $.post("/newUser", data, function(response){
    var res = JSON.parse(response);
    console.log(res);
    if(res.error == "no-error"){
      login(res.userID);
      closeSignupDialog();
    }
    else{
      signupError(res.error);
    }
  });
}

function signupError(message){
  var body = $("<p></p>").html(message);
  var elem = $("#signup-error");
  elem.empty();
  elem.append(body);
  elem.show();
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
