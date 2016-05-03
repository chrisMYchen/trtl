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
  $("#follow-close").click(closeFollowDialog);

  /* Open follow dialog on from nav */
  $("#follow-button").click(openFollowDialog);

  /* Submit friend info */
  $("#follow-form").submit(followSubmit);

  /* Reset form*/
  $("#follow-form").on("reset", function(){
    $("#follow-form input[name=followname]").css("background", "#FFF");
  });

  /* check friend username */
  $("#follow-form input[name=followname]").on("input", function(){
    usernameCheck($(this), "#AFA", "#FAA");
  });
}

function openFollowDialog(){
  $("#follow-wrapper").show();
  refreshFollowList();
}

function closeFollowDialog(){
  $("#follow-wrapper").hide();
  $("#follow-form")[0].reset();
  $("#follow-msg").hide();
  $("#follow-msg").empty();
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

function removeFollow(elem){
  var followname = elem.parent().val();
  var req = {friendUsername: followname, userID: userInfo.id};
  console.log(req);
  $.post("/unfollow", req, function(data){
    var res = JSON.parse(data);
    if(res.error = "no-error"){
      console.log(req);
      console.log(res);
      refreshFollowList();
    }
    else{
      followMsg(res.error, true);
    }
  });
}

/* <div id="pending-list"></div>
   <div id="following-list"></div>
   <div id="follower-list"></div> */

function refreshFollowLists(){
  $("#pending-list").empty();
  getPending();

  $("#following-list").empty();
  getFollowingList();

  $("#follower-list").empty();
  getFollowerList();
}

function getFollowList(){
  var user = {username: userInfo.username};
  $.post("/userInfo", user, function(data){
    var res = JSON.parse(data);
    console.log(res);
    if(res.error == "no-error"){
      var dom = $("#follow-list");
      fillFollowList(res.followers, dom);
    }
    else{
      $("#follow-list").html(res.error);
    }

  })
}

function fillFollowList(list, dom){
  for(var i = 0; i < friends.length; i++){
    var friend = friendDOM(friends[i]);
    dom.append(friend)
  }
}

function followDOM(follow){
  var div = $("<div></div>").addClass("follow-item");
  var remove = $("<div></div>").addClass("follow-remove");
  var xicon = $("<i></i>").addClass("material-icons").html("close");
  remove.append(xicon);
  div.html(follow);
  div.append(remove);
  return div;
}

function followMsg(message, error){
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
