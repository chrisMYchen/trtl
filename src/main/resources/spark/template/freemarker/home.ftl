<#assign content>
<div id="posts">
  <div class="input">
    <textarea class="input-content" rows="4" placeholder="Post a note to this location"></textarea>
    <div class="input-controls">
      <select class="input-privacy" name="privacy" style="display:none">
      	<option value="private">Private</option>
      	<option value="public">Public</option>
      </select>
      <div class="input-submit" value="Post">Post</div>
    </div>
  </div>
</div>
</#assign>
<#include "main.ftl">
