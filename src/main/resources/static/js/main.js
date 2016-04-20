var posts = [
    {"content": "First post. Hope you like it!", "handle": "cpax", "userreal": "Christina Paxson", "time": "April 8th"},
    {"content": "Hey! Hope you are enjoying the library! It seems like I spend all my time here sometimes.", "handle": "josiah", "userreal": "Josiah Carberry", "time": "April 9th"},
    {"content": "All nighter, yet again!", "handle": "kliu", "userreal": "Kate Liu", "time": "April 10th"},
    {"content": "You should check out the northeast corner of the fifth floor!", "handle": "cchen", "userreal": "Chris Chen", "time": "April 11th"},
    {"content": "I hate this place! You should go to the rock instead", "handle": "hkaul", "userreal": "Hemang Kaul", "time": "April 11th"}
]

$(function(){
    createPosts();
    modal();
    locationStart();

});

function locationStart(){
  if(navigator.geolocation){
    navigator.geolocation.getCurrentPosition(function(data){
      locationHandler({lat: data.coords.latitude, lng: data.coords.longitude});
    });
  } else{
    var url = "https://www.googleapis.com/geolocation/v1/geolocate";
    var key = "AIzaSyBAAQ3e48GdAUahvhMjG5XZ8Idw9Adbgjs";
    $.post(url, {key:key}, function(data){
      locationHandler(data.position)
    });
  }
}

function locationHandler(position){
  $("#location").html("Your location is: " + position.lat + ", " + position.lng);
}

function createPosts(){
   for(var i = 0; i < posts.length; i++){
       var post = posts[i];
       var dom = $("<div></div>").attr("class","post");

       /* User */
       var user = $("<div></div>").attr("class","post-user");
       var userrn = $("<div></div>").attr("class","post-realname").append(post.userreal);
       var handle = $("<a></a>").attr("class","post-handle").attr("href","/user/" + post.handle).append("@" + post.handle);;
       user.append(userrn).append(handle);

       /* Content */
       var content = $("<div></div>").attr("class","post-content").append(post.content);

       /* Meta */
       var meta = $("<div></div>").attr("class","post-meta");
       var time = $("<div></div>").attr("class","post-time").append(post.time);
       var share = $("<a></a>").attr("class","post-share").append("Share");
       meta.append(time).append(share);

       dom.append(user).append(content).append(meta);

       $("#posts").append(dom);
   }
}

function login(){
  $(document).keydown(function(e){
    if(e.which == 27){
      $("#modal-wrapper").hide();
    }
  });

  $("#modal-close").click(function(){
    $("#modal-wrapper").hide();
  });

  $("#login").click(function(){
      $("#modal-wrapper").show();
  });

  $("#modal input[type=submit]").click(function(){
      $("#modal-wrapper").hide();
  })
}
