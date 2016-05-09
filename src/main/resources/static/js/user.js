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
  $("#login-close p").click(closeLoginDialog);

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

function openLoginDialog(){
  $("#login-wrapper").show();
}

function closeLoginDialog(){
  $("#login-wrapper").hide();
  $("#login-form")[0].reset();
  $("#login-error").hide();
  $("#login-error").empty();
  $("#login-button").toggleClass("active", false);
}

function loginSubmit(e){
  e.preventDefault();
  $("#login-error").hide();
  $("#login-error").empty();
  sendLogin();
}

function sendLogin(){
  var data = $("#login-form").serialize();
  $.post("/login", data, function(response){
    var res = JSON.parse(response);
    if((res.error == "no-error") && (res.userID != -1)){
      setLoginCookie(res.userID);
      window.location.reload();
    }
    else{
      $("#login-form")[0].reset();
      loginError(res.error);
    }
  });
}

function login(userID){
  var req = {userID: userID};

  $.post("/getUser", req, function(data){
    var res = JSON.parse(data);
    userInfo = {id: userID, username: res.username};
    setLoginMode(true);
    setLoginCookie(userID);
  });
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
    $("#user-name").html("Welcome " + userInfo.username);
    $(".loggedin").toggleClass("hidden", false);
    $(".loggedout").toggleClass("hidden", true);
    refreshFollowLists();
  }
  else{
    $("#user-name").html("");
    $(".loggedin").toggleClass("hidden", true);
    $(".loggedout").toggleClass("hidden", false);
  }
}

function logout(){
  removeLoginCookie();
  setLoginMode(false);
  window.location.reload();
}


/***********************/
/** Cookie Management **/
/***********************/
function checkLoginCookie(){
  var cookie = getCookie("userid");
  if (cookie != null){
    login(parseInt(cookie));
  }
}

function setLoginCookie(userID){
  var cookie = "userid=" + userID + ";";
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

  $("#signup-close p").click(closeSignupDialog);

  $("#signup-button").click(function(){
    closeLoginDialog();
    openSignupDialog();
  });

  /* Reset form*/
  $("#signup-form").on("reset", function(){
    $("#signup-form input[name=username]").css("background", "#FFF");
  });

  $("#signup-form input[name=username]").on("input", function(){
    usernameCheck($(this), "#FAA", "#AFA");
  });

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
  $("#signup-button").toggleClass("active", false);
}

function signupSubmit(e){
  e.preventDefault();
  $("#signup-error").empty();
  sendSignup();
}

function sendSignup(){

  var validation = checkSignupFields();
  if(validation){
    var data = $("#signup-form").serialize();
    $.post("/newUser", data, function(response){
      var res = JSON.parse(response);
      if(res.error == "no-error"){
        login(res.userID);
        closeSignupDialog();
      }
      else{
        signupError(res.error);
      }
    });
  }
}

function checkSignupFields(){
  var flag = true;
  var username = $("#signup-form input[name=username]").val();
  if(username.length == 0){
    signupError("Please enter a username");
    flag = false;
  }

  var pword = $("#signup-form input[name=password]").val();
  if(pword.length < 5){
    signupError("Passwords must be at least 5 characters long.");
    flag = false;
  }

  var email = $("#signup-form input[name=email]").val();
  var regex = /[a-zA-z.]+[@]{1}[a-zA-z.]+/
  if(!regex.test(email)){
    signupError("Please enter a valid email.");
    flag = false;
  }

  var firstname = $("#signup-form input[name=firstname]").val();
  if(firstname == 0){
    signupError("Please provide your first name.");
    flag = false;
  }

  var lastname = $("#signup-form input[name=lastname]").val();
  if(lastname == 0){
    signupError("Please provide your last name.");
    flag = false;
  }

  return flag;

}

function signupError(message){
  var body = $("<p></p>").html(message);
  var elem = $("#signup-error");
  elem.append(body);
  elem.show();
}

function usernameCheck(elem, exists, notexists){
  var uname = elem.val();

  if(uname.length > 2){
    $.post("/checkUsername", {username: uname}, function(response){
      var res = JSON.parse(response);
      if(res.error == "no-error"){
        if(res.exists){
          elem.css("background", exists);
        }
        else{
          elem.css("background", notexists);
        }
      }
    });
  }
  else{
    elem.css("background", "#fff");
  }
}
