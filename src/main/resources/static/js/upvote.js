function upvoteSetup(){
	$("body").on("click", ".upvote i", upvoteHandler);
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