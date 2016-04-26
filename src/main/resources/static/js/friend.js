/*************************/
/** Friends Functionality **/
/*************************/
function friendSetup(){
  /* Allow escape */
  $(document).keydown(function(e){
    if(e.which == 27){
      $("#friend-wrapper").hide();
    }
  });

  /* Close friend dialog on "X click */
  $("#friend-close").click(closeFriendDialog);

  /* Open friend dialog on from nav */
  $("#friend-button").click(openFriendDialog);

  /* Submit friend info */
  $("#friend-form").submit(friendSubmit);

  /* Reset form*/
  $("#friend-form").on("reset", function(){
    $("#friend-form input[name=friendname]").css("background", "#FFF");
  });

  /* check friend username */
  $("#friend-form input[name=friendname]").on("input", function(){
    usernameCheck($(this), "#AFA", "#FAA");
  });
}

function openFriendDialog(){
  $("#friend-wrapper").show();
  refreshFriendList();
}

function closeFriendDialog(){
  $("#friend-wrapper").hide();
  $("#friend-form")[0].reset();
  $("#friend-error").hide();
  $("#friend-error").empty();
}

function friendSubmit(e){
  e.preventDefault();
  $("#friend-error").hide();
  $("#friend-error").empty();
  addFriend();
}

function addFriend(){
  var friend = $("#friend-form input[name=friendname]").val();
  var data = {userID: userInfo.id, friendUsername: friend};
  $.post("/addFriend", data, function(response){
    var res = JSON.parse(response);
    console.log(res);
    if((res.error == "no-error")){
      $("#friend-form")[0].reset();
      refreshFriendList();
      var msg = "User " + friend + " was added as a friend.";
      friendMsg(msg, false);
    }
    else{
      $("#friend-form")[0].reset();
      friendMsg(res.error, true);
    }
  });
}

function refreshFriendList(){
  $("#friend-list").empty();
  getFriendList();
}

function friendMsg(message, error){
  var body = $("<p></p>").html(message);
  var elem = $("#friend-msg");
  elem.empty();
  elem.append(body);
  if(error){
    elem.toggleClass("error", true);
    elem.toggleClass("success", false);
  }
  else{
    elem.toggleClass("error", false);
    elem.toggleClass("success", true);
  }
  elem.show();
}

function getFriendList(){
  var user = {username: userInfo.username};
  $.post("/userInfo", user, function(data){
    var res = JSON.parse(data);
    console.log(res);
    if(res.error == "no-error"){
      fillFriendList(res.friends);
    }
    else{
      $("#friend-list").html(res.error);
    }

  })
}

function fillFriendList(friends){
  var dom = $("#friend-list");
  for(var i = 0; i < friends.length; i++){
    var friend = friendDOM(friends[i]);
    dom.append(friend)
  }
}

function friendDOM(friend){
  var div = $("<div></div>").addClass("friend-item");
  div.html(friend);
  return div;
}
