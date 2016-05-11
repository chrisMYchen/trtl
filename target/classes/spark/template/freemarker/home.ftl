<#assign content>
<div id="sidebar">
  <div id="map-container" class="sidebar-element">
    <div id="map"></div>
  </div>
  <div id="filtering" class="sidebar-element">
    <div class="filter-option active" data-filter="0">All</div>
    <div class="filter-option" data-filter="1">Friends</div>
    <div class="filter-option" data-filter="2">Mine</div>
  </div>
</div>
<div id="posts-wrapper">
  <div class="input">
    <textarea class="input-content" rows="4" placeholder="Post a note to this location"></textarea>
    <div id="image-thumb"></div>
    <div class="input-controls">
      <div class="input-submit">Post</div>
      <div id="image-upload" class="hidden loggedin">
        <i class="material-icons">image</i>
        <input type="file" id="image-input" name="pic" accept="image/*">
      </div>
      <select id="input-privacy" name="privacy" class="loggedin hidden">
      	<option value="private">Private</option>
      	<option value="public">Public</option>
      </select>
    </div>
  </div>
  <div id="posts" data-columns>
  </div>
</div>
</#assign>
<#include "main.ftl">
