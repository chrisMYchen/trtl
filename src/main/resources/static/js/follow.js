/*************************/
/** Follower Functionality **/
/*************************/
function followSetup(){

  setFollowTab(true);

  /* Allow escape */
  $(document).keydown(function(e){
    if(e.which == 27){
      $("#follow-wrapper").hide();
    }
  });

  /* Close follow dialog on "X click */
  $("#follow-close p").click(closeFollowDialog);

  /* Open follow dialog on from nav */
  $("#follow-button").click(openFollowDialog);

  /* Submit friend info */
  $("#follow-form").submit(followSubmit);

  /* Setup followers / following tabs */
  $("#following-tab").click(function(){
    console.log("following");
    setFollowTab(true);
  });

  $("#followers-tab").click(function(){
    console.log("followers");
    setFollowTab(false);
  });

  /* Reset form*/
  $("#follow-form").on("reset", function(){
    $("#follow-form input[name=followname]").css("background", "#FFF");
  });

  /* check friend username */
  $("#follow-form input[name=followname]").on("input", function(){
    usernameCheck($(this), "#AFA", "#FAA");
  });

/*  $("#follower-list").on("click", ".remove", function(){
    removeFollower(this);
  });*/

  $("#following-list").on("click", ".follow-remove", function(){
    removeFollowing(this);
  });

  $("#follower-list").on("click", ".follow-accept", function(){
    console.log("accept");
    acceptFollower(this);
  });
}

function openFollowDialog(){
  $("#follow-wrapper").show();
  refreshFollowLists();
}

function closeFollowDialog(){
  $("#follow-wrapper").hide();
  $("#follow-form")[0].reset();
  $("#follow-msg").hide();
  $("#follow-msg").empty();
  $("#follow-button").toggleClass("active",false);
}

function setFollowTab(following){
  if(following){
    $("#following-tab").toggleClass("active", true);
    $("#followers-tab").toggleClass("active", false);
    $("#following").toggle(true);
    $("#followers").toggle(false);
    $("#follow-form")[0].reset();
  }
  else {
    $("#following-tab").toggleClass("active", false);
    $("#followers-tab").toggleClass("active", true);
    $("#following").toggle(false);
    $("#followers").toggle(true);
    $("#follow-form")[0].reset();

  }
}

function followSubmit(e){
  e.preventDefault();
  $("#friend-msg").hide();
  $("#friend-msg").empty();
  addFollow();
}

function addFollow(){
  var follow = $("#follow-form input[name=followname]").val();
  var data = {userID: userInfo.id, friendUsername: follow};
  $.post("/requestFollow", data, function(response){
    var res = JSON.parse(response);
    console.log(res);
    if((res.error == "no-error")){
      $("#follow-form")[0].reset();
      refreshFollowLists();
      var msg = "Follow request for user " + follow + " was sent.";
      followMsg(msg, false);
    }
    else{
      $("#follow-form")[0].reset();
      followMsg(res.error, true);
    }
  });
}


function acceptFollower(elem){
  var follow = $(elem).parents(".follow-item").children(".follow-user").html();
  var data = {userID: userInfo.id, friendUsername: follow};
  $.post("/acceptFollower", data, function(response){
    var res = JSON.parse(response);
    console.log(res);
    if((res.error == "no-error")){
      refreshFollowLists();
      var msg = "You have accepted " + follow + " as a follower.";
      followMsg(msg, false);
    }
    else{
      followMsg(res.error, true);
    }
  });
}

function removeFollowing(elem){
  var followname = $(elem).parents(".follow-item").children(".follow-user").html();
  var req = {friendUsername: followname, userID: userInfo.id};
  console.log(req);
  $.post("/unfollow", req, function(data){
    var res = JSON.parse(data);
    if(res.error = "no-error"){
      refreshFollowLists();
      var msg = "You have unfollowed " + followname + ".";
      followMsg(msg, false);
    }
    else{
      followMsg(res.error, true);
    }
  });
}

function refreshFollowLists(){
  $("#following-list").empty();
  $("#follower-list").empty();
  getFollowList();
}

function getFollowList(){
  var user = {userID: userInfo.id};
  $.post("/myInfo", user, function(data){
    var res = JSON.parse(data);
    console.log(res);
    if(res.error == "no-error"){
      fillFollowList(res.followers, res.pending_followers, true, $("#follower-list"));
      fillFollowList(res.following, res.pending_following, false, $("#following-list"));
    }
    else{
      $("#following-list").html(res.error);
      $("#follower-list").html(res.error);
    }
  });
}

function fillFollowList(list, pending, follower, dom){
  for (var i = 0; i < pending.length; i++){
    var pending_item = followDOM(pending[i], true, follower);
    dom.append(pending_item);
  }
  for(var i = 0; i < list.length; i++){
    var follow = followDOM(list[i], false, follower);
    dom.append(follow);
  }
}

function followDOM(follow, pending, follower){
  var div = $("<div></div>").addClass("follow-item");
  var user = $("<div></div>").addClass("follow-user");
  user.html(follow);

  var remove = $("<div></div>").addClass("follow-remove");
  var xicon = $("<i></i>").addClass("material-icons").html("close");
  remove.append(xicon);

  var controls = $("<div></div>").addClass("follow-controls");

  if(pending){
    var pending = $("<div></div>").addClass("pending-label");
    div.addClass("pending");
    controls.append(pending);
    if(follower){
      var accept = $("<div></div>").addClass("follow-accept");
      var checkicon = $("<i></i>").addClass("material-icons").html("check");
      accept.append(checkicon);
      controls.append(accept);
      pending.html("request");
    }
    else{
      pending.html("pending")
    }
  }

  controls.append(remove);

  div.append(user).append(controls);

  return div;
}

function followMsg(message, error){
  var body = $("<p></p>").html(message);
  var elem = $("#follow-msg");
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
  window.setTimeout(function(){
    elem.hide();
  }, 5000);
}
