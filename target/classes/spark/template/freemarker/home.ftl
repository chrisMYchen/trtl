<#assign content>
<div id="posts">
  <div class="input">
    <textarea class="input-content" rows="4" placeholder="Post a note to this location"></textarea>
    <div class="input-controls">
      <div class="input-submit" value="Post">Post</div>
      <select id="input-privacy" name="privacy" class="loggedin hidden">
      	<option value="private">Private</option>
      	<option value="public">Public</option>
      </select>
    </div>
  </div>
<div class = "testImageUpload">
  	<form action = "/postNoteImage" method="post" enctype="multipart/form-data">
	  	<textarea class="input-content" name="text" rows="4" placeholder="Post a note to this location"></textarea>
	    <div class="input-controls">
	      <select id="input-privacy" name="privacy" class="loggedin hidden">
	      	<option value="private">Private</option>
	      	<option value="public">Public</option>
	      </select>
	      <input type="file" name="pic" accept="image/*">
	      <div class="input-submit" value="Post">Post</div>
	      <input type="submit" value="Upload Image" name="submit">
	    </div>
  	</form>
  </div>
  <div id="updates"></div>
</div>
</#assign>
<#include "main.ftl">
