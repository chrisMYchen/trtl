/*************************/
/** Follower Functionality **/
/*************************/
function followSetup(){

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

  $("#follower-list").on("click", ".follow-remove", function(){
    console.log("follower-remove");
    removeFollower(this);
  });

  $("#following-list").on("click", ".follow-remove", function(){
    unfollow(this);
  });

  $("#follower-list").on("click", ".follow-accept", function(){
    console.log("accept");
    acceptFollower(this);
  });

  $("#follower-list").on("click", ".followback", function(){
    var name = $(this).closest(".follow-controls").siblings(".follow-user").html();
    requestFollow(name);
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
    refreshFollowLists();
  }
  else {
    $("#following-tab").toggleClass("active", false);
    $("#followers-tab").toggleClass("active", true);
    $("#following").toggle(false);
    $("#followers").toggle(true);
    $("#follow-form")[0].reset();
    refreshFollowLists();
  }
}

function followSubmit(e){
  e.preventDefault();
  $("#friend-msg").hide();
  $("#friend-msg").empty();
  var follow = $("#follow-form input[name=followname]").val();
  requestFollow(follow);
}


function requestFollow(name){
  var data = {userID: userInfo.id, friendUsername: name};
  console.log(data);
  $.post("/requestFollow", data, function(response){
    var res = JSON.parse(response);
    console.log(res);
    if((res.error == "no-error")){
      $("#follow-form")[0].reset();
      refreshFollowLists();
      var msg = "Follow request for user " + name + " was sent.";
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

function removeFollower(elem){
  var followname = $(elem).parents(".follow-item").children(".follow-user").html();
  var req = {friendUsername: followname, userID: userInfo.id};
  console.log(req);
  $.post("/removeFollower", req, function(data){
    var res = JSON.parse(data);
    if(res.error == "no-error"){
      refreshFollowLists();
      var msg = "You have removed " + followname + " as a follower.";
      followMsg(msg, false);
    }
    else{
      followMsg(res.error, true);
    }
  });
}

function unfollow(elem){
  var followname = $(elem).parents(".follow-item").children(".follow-user").html();
  var req = {friendUsername: followname, userID: userInfo.id};
  console.log(req);
  $.post("/unfollow", req, function(data){
    var res = JSON.parse(data);
    if(res.error == "no-error"){
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
      var following = res.following.concat(res.pending_following);
      fillFollowerList(res.followers, res.pending_followers, following, $("#follower-list"));
      fillFollowingList(res.following, res.pending_following, $("#following-list"));
      notify(res.pending_followers, res.pending_following);
    }
    else{
      $("#following-list").html(res.error);
      $("#follower-list").html(res.error);
    }
  });
}

function fillFollowerList(list, pending, following, dom){
  for (var i = 0; i < pending.length; i++){
    var pendName = pending[i];
    var followback = (following.indexOf(pendName) == -1) ? true : false;
    var pending_item = followDOM(pendName, true, true, followback);
    dom.append(pending_item);
  }
  for(var i = 0; i < list.length; i++){
    var followName = list[i];
    var followback = (following.indexOf(followName) == -1) ? true : false;
    var follow = followDOM(followName, false, true, followback);
    dom.append(follow);
  }
}

function fillFollowingList(list, pending, dom){
  for (var i = 0; i < pending.length; i++){
    var pending_item = followDOM(pending[i], true, false, false);
    dom.append(pending_item);
  }
  for(var i = 0; i < list.length; i++){
    var follow = followDOM(list[i], false, false, false);
    dom.append(follow);
  }
}

function followDOM(follow, pending, follower, followback){
  var div = $("<div></div>").addClass("follow-item");
  var user = $("<div></div>").addClass("follow-user");
  user.html(follow);

  var remove = $("<div></div>").addClass("follow-remove");
  var xicon = $("<i></i>").addClass("material-icons").html("close");
  remove.append(xicon);

  var controls = $("<div></div>").addClass("follow-controls");

  if(followback){
    var fback = $("<div></div>").addClass("followback").html("Follow");
    controls.append(fback);
  }

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

function notify(followers, following){
  var button = $("#follow-button");
  var elem = $(".follow-notify");

  if (followers.length > 0){
    elem.html(followers.length);
    button.toggleClass("notifications", true);
    $("#followers-tab .tab-notify").html(followers.length);
  } else{
    elem.empty();
    $("#followers-tab .tab-notify").empty();
    button.toggleClass("notifications", false);
  }

  if(following.length > 0){
    $("#following-tab .tab-notify").html(following.length);
  } else{
    $("#following-tab .tab-notify").empty();
  }

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
  elem.delay(10000).fadeOut();
}
