function postNotes(){
  $(".input-submit").click(postHandler);
}

function postHandler(data){
  data.preventDefault();
  console.log(data);
  var post = $(".input-content").val();
  $(".input-content").val("");
  postNote(post);
}

function postNote(post){
  
}
