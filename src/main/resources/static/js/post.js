function postNotes(){
  $(".input").submit(function(data){
    data.preventDefault();
    console.log(data);
  });
}
