<!DOCTYPE html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="theme-color" content="#A2D85A">
  <title>${title}</title>
  <link href='https://fonts.googleapis.com/css?family=Josefin+Sans:400,600,300' rel='stylesheet' type='text/css'>
  <link href='https://fonts.googleapis.com/css?family=Raleway:400,300,500,200,600' rel='stylesheet' type='text/css'>
  <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
  <script src="https://code.jquery.com/jquery-1.12.3.min.js" integrity="sha256-aaODHAgvwQW1bFOGXMeX+pC4PZIPsvn2h1sArYOhgXQ="   crossorigin="anonymous"></script>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
  <script src="/js/linkify.min.js"></script>
  <script src="/js/linkify-jquery.min.js"></script>
  <script src="/js/main.js"></script>
  <script src="/js/location.js"></script>
  <script src="/js/display.js"></script>
  <script src="/js/post.js"></script>
  <script src="/js/user.js"></script>
  <script src="/js/follow.js"></script>
  <script src="/js/update.js"></script>
  <script src="/js/image.js"></script>

  <link rel="stylesheet" href="/css/normalize.css">
  <link rel="stylesheet" href="/css/style.css">
  <link rel="stylesheet" href="/css/font.css">
  <link rel="stylesheet" href="/css/trtl-bold.css">
</head>
<body>
  <div id="login-wrapper" class="overlay-wrapper" style="display:none">
    <div id="login" class="overlay">
      <div id="login-close" class="close"><p><i class="material-icons">close</i></p></div>
      <div id="login-error" class="error" style="display:none;"></div>
      <form id="login-form">
        <input type="text" name="username" placeholder="USERNAME" size="40" required="required"><br>
        <input type="password" name="password" placeholder="PASSWORD" size="40" required="required"><br>
        <input type="submit">
      </form>
    </div>
  </div>
  <div id="signup-wrapper" class="overlay-wrapper" style="display:none">
    <div id="signup" class="overlay">
      <div id="signup-close" class="close"><p><i class="material-icons">close</i></p></div>
      <div id="signup-error" class="error" style="display:none;"></div>
      <form id="signup-form">
        <input type="text" name="username" placeholder="Username" size="40" required="required"><br>
        <input type="password" name="password" placeholder="Password" size="40" required="required"><br>
        <input type="text" name="email" placeholder="Email Address" size="40" required="required"><br>
        <input type="text" name="firstname" placeholder="First Name" size="40" required="required"><br>
        <input type="text" name="lastname" placeholder="Last Name" size="40" required="required"><br>
        <input type="text" name="phone" placeholder="Phone (optional)" size="40"><br>
        <input type="submit">
      </form>
    </div>
  </div>
  <div id="follow-wrapper" class="overlay-wrapper" style="display:none">
    <div id="follow" class="overlay">
      <div id="follow-close" class="close"><p><i class="material-icons">close</i></p></div>
      <div id="follow-msg" class="msg" style="display:none;"></div>
      <div id="follow-tabs" class="tabs">
        <div id="following-tab" class="follow-tab">Following</div>
        <div id="followers-tab" class="follow-tab">Followers</div>
      </div>
      <div id="following" class="follow-page">
        <form id="follow-form">
          <input type="text" name="followname" placeholder="Follow Another User" size="30">
          <input type="submit" value="Add">
        </form>
        <div id="following-list" class="follow-list"></div>
      </div>
      <div id="followers" class="follow-page">
        <div id="follower-list" class="follow-list"></div>
      </div>
    </div>
  </div>
  <div id="nav">
    <div id="branding">
      <div id="title" class="menu-item">trtl</div>
    </div>
    <div id="account-links">
      <div id="user-name" class="menu-item account loggedin hidden"></div>
      <div id="location" class="menu-item"></div>
      <div id="signup-button" class="menu-item account loggedout">Signup</div>
      <div id="login-button" class="menu-item account loggedout">Login</div>
      <div id="follow-button" class="menu-item loggedin hidden">
        <i class="material-icons">people</i>
      </div>
      <div id="logout-button" class="menu-item account loggedin hidden">Logout</div>
    </div>
  </div>
  <div id="wrapper">
    ${content}
  </div>
</body>
</html>
