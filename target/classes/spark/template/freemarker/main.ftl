<!DOCTYPE html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>${title}</title>
  <link href='https://fonts.googleapis.com/css?family=Josefin+Sans:400,600,300' rel='stylesheet' type='text/css'>
  <script src="https://code.jquery.com/jquery-1.12.3.min.js" integrity="sha256-aaODHAgvwQW1bFOGXMeX+pC4PZIPsvn2h1sArYOhgXQ="   crossorigin="anonymous"></script>
  <script src="/js/main.js"></script>
  <script src="/js/location.js"></script>
  <script src="/js/display.js"></script>
  <script src="/js/post.js"></script>
  <script src="/js/user.js"></script>
  <link rel="stylesheet" href="/css/normalize.css">
  <link rel="stylesheet" href="/css/style.css">
</head>
<body>
  <div id="login-wrapper" class="overlay-wrapper" style="display:none">
    <div id="login" class="overlay">
      <div id="login-close" class="close"><p>X</p></div>
      <div id="login-error" class="error" style="display:none;"></div>
      <form id="login-form">
        <input type="text" name="username" placeholder="USERNAME" size="40" required="required">
        <input type="password" name="password" placeholder="PASSWORD" size="40" required="required">
        <input type="submit">
      </form>
    </div>
  </div>
  <div id="signup-wrapper" class="overlay-wrapper" style="display:none">
    <div id="signup" class="overlay">
      <div id="signup-close" class="close"><p>X</p></div>
      <div id="signup-error" class="error" style="display:none;"></div>
      <form id="signup-form">
        <input type="text" name="firstname" placeholder="First Name" size="40" required="required">
        <input type="text" name="lastname" placeholder="Last Name" size="40" required="required">
        <input type="text" name="email" placeholder="Email Address" size="40" required="required">
        <input type="text" name="phone" placeholder="Phone (optional)" size="40">
        <input type="text" name="username" placeholder="Username" size="40" required="required">
        <input type="password" name="password" placeholder="Password" size="40" required="required">
        <input type="submit">
      </form>
    </div>
  </div>
  <div id="nav">
    <div id="branding">
      <div id="title" class="menu-item">Trtl.</div>
    </div>
    <div id="nav-meta">
      <div id="location" class="menu-item"></div>
    </div>
    <div id="account-links">
      <div id="signup-button" class="menu-item account">Sign-Up</div>
      <div id="login-button" class="menu-item account">Login</div>
    </div>
    <div id="user-info" style="display:none">
      <div id="user-name" class="menu-item"></div>
    </div>
  </div>
  <div id="wrapper">
    ${content}
  </div>
</body>
</html>
