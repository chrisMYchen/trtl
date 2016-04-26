<#assign content>
<div id="posts">
  <div class="input">
    <textarea class="input-content" rows="4" placeholder="Post a note to this location"></textarea>
    <div class="input-controls">
<<<<<<< HEAD
      <select class="input-privacy" name="privacy" style="display:none">
=======
      <select id="input-privacy" name="privacy" class="loggedin hidden">
>>>>>>> d0c4e3bd2c1ef6797af3754772a42fa5c5061832
      	<option value="private">Private</option>
      	<option value="public">Public</option>
      </select>
      <div class="input-submit" value="Post">Post</div>
    </div>
  </div>
</div>
</#assign>
<#include "main.ftl">
