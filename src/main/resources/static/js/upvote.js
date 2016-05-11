function upvoteSetup(){
	$("body").on("click", ".enabled .upvote-icon", upvoteHandler);
	$("body").on("click", ".disabled .upvote-icon", displayUpvoteHover);
}

function upvoteHandler(){
	var button = $(this);
	var upvote_div = button.closest(".upvote");
	var noteid = button.closest(".post").attr("data-noteid");

	var req = {noteID: noteid, userID: userInfo.id};
	var count_div = upvote_div.children(".upvote-count");
	var count = parseInt(count_div.html());

	if(upvote_div.hasClass("upvoted")){
		$.post("/removeUpvote", req, function(response){
			var res = JSON.parse(response)
			if(res.error == "no-error"){
				upvote_div.toggleClass("upvoted");
				count--;
				count_div.html(count);
			}

		});
	}
	else{
		$.post("/upvote", req, function(response){
			var res = JSON.parse(response)
			if(res.error == "no-error"){
				upvote_div.toggleClass("upvoted");
				count++;
				count_div.html(count);
			}

		});
	}

}

function displayUpvoteHover(e){
  var elem = $("<div></div>").addClass("hovermenu").html("Please signup or login to upvote.");
  $("body").append(elem);
  elem.offset({top:e.pageY,left:e.pageX});
  elem.delay(1000).fadeOut();
  window.setTimeout(function(){elem.remove()}, 2000);
}
