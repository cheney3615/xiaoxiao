/* user-passport-2015 login.index.js Date:2015-05-19 15:44:19 */
seajs.use(["user/passport-2015/js/login.username.js","user/passport-2015/js/login.password.js","user/passport-2015/js/login.authcode.js","user/passport-2015/js/login.submit.js","jdf/1.0.0/ui/placeholder/1.0.0/placeholder","user/passport-2015/js/login.qrcode.js"],function(a,b,c,d,e,f){a.init(),b.init(),c.init(),f.init(),$("input[placeholder]").bind("keydown",function(){$(this).prev("txt").hide()}),$("txt").css("font-family","Arial, Verdana, \u5b8b\u4f53"),document.msCapsLockWarningOff=!0,$("body").delegate("input:checkbox, a","click",function(){$(this).css("outline","rgb(109, 109, 109) none 0px")}),$("body").delegate("input:checkbox, a","focusin",function(){$.browser.webkit?$(this).css("outline","rgb(77, 144, 254) auto 5px"):$(this).css("outline","#aaa 1px dotted")}),$("body").delegate("input:checkbox, a","focusout",function(){$(this).css("outline","rgb(109, 109, 109) none 0px")}),$("body").delegate("#nloginpwd","focus",function(){$(this).siblings("txt").hide()});var g="placeholder"in document.createElement("input");g||$("input[placeholder]").placeholder()});
