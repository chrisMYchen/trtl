<#macro filmLink f>
  <a href="/film${f.getID()}">${f.getName()}</a>
</#macro>
<#macro actorLink a>
  <a href="/actor${a.getID()}">${a.getName()}</a>
</#macro>
