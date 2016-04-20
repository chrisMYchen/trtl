<!DOCTYPE html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>${title}</title>
  <link href='https://fonts.googleapis.com/css?family=Josefin+Sans:400,600,300' rel='stylesheet' type='text/css'>
  <script src="https://code.jquery.com/jquery-1.12.3.min.js" integrity="sha256-aaODHAgvwQW1bFOGXMeX+pC4PZIPsvn2h1sArYOhgXQ="   crossorigin="anonymous"></script>
  <script src="/js/main.js"></script>
  <link rel="stylesheet" href="/css/normalize.css">
  <link rel="stylesheet" href="/css/style.css">
</head>
<body>
  <div id="modal-wrapper" style="display:none">
    <div id="login">
      <div id="login-close"><p>X</p></div>
      <form>
        <input type="text" name="uname" placeholder="USERNAME" size="40">
        <input type="password" name="pword" placeholder="PASSWORD" size="40">
        <input type="submit">
      </form>
    </div>
  </div>
  <div id="signup-wrapper" style="display:none">
    <div id="signup">
      <div id="signup-close"><p>X</p></div>
      <form>
        <input type="text" name="uname" placeholder="USERNAME" size="40">
        <input type="password" name="pword" placeholder="PASSWORD" size="40">
        <input type="submit">
      </form>
    </div>
  </div>
  <div id="nav">
    <div id="branding">
      <div id="logo" class="menu-item"></div>
      <div id="title" class="menu-item">Trtl.</div>
    </div>
    <div id="nav-meta">
      <div id="location" class="menu-item"></div>
    </div>
    <div id="links">
      <div id="signup-button" class="menu-item">Sign-Up</div>
      <div id="login-button" class="menu-item">Login</div>
    </div>
  </div>
  <div id="wrapper">
    ${content}
  </div>
</body>
</html>
