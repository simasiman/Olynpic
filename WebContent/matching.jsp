<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="modelPack.*"%>
<%@ page import="Utility.*" %>
<%@ page import="java.net.*"%>
<%@ page import="java.util.ArrayList"%>
<%
Cookie[] aryCookies = request.getCookies();

String key = (String)session.getAttribute("key");
String name = (String)session.getAttribute("name");

User user = MatchUserList.get(key);
if (key == null || key.isEmpty() || user == null)
{
    response.sendRedirect("top");
    return;
}

Cookie cooKey = new Cookie("key", key);
cooKey.setMaxAge(60 * 60 * 24 * 90);
response.addCookie(cooKey);
Cookie cooName = new Cookie("name", URLEncoder.encode(name, "UTF-8"));
cooName.setMaxAge(60 * 60 * 24 * 90);
response.addCookie(cooName);
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" charset="utf-8">
<link rel="stylesheet" href="css/reset.css" type="text/css">
<link rel="stylesheet" href="css/matching.css" type="text/css">
<title>[pane-tori] - マッチング</title>
<script type="text/javascript">
WebSocketDemo = {};
(function(d) {
    function $(query) {
        return document.querySelector(query);
    }
    d.connect = function() {
        var ws = new WebSocket("ws://<%=GameSetting.SERVER_ADDRESS%>:8080/<%=GameSetting.PROJECT_NAME%>/matching");
        ws.onmessage = function(message) {
            var text = message.data;
            if (text.indexOf('<!--complete-->') == 0)
            {
            	window.location.href = "game";
            }
            else if (text.indexOf('<!--destruct-->') == 0)
            {
            	window.location.href = "top";
            }
        };
        ws.onopen = function() {
        	ws.send(<%=key%>);
        }
        d.webSocket = ws;
    };
    d.disconnect = function() {
    	d.webSocket.close();
    }
}) (WebSocketDemo);

window.onload = function() {
	WebSocketDemo.connect();
}

window.onbeforeunload = function(e) {
	WebSocketDemo.disconnect();
};
</script>
</head>
<body>
<div class="page">
	<header><img src="img/logo/pane-tori-logo_s.png" alt="ゲームのロゴ"></header>

  <div class="nowMatching_message_back">
	<div class="nowMatching">只今マッチング中です</div>
	<div class="message"><img src="img/logo/running.gif" alt="マッチング中"></div>
	<a href="top" class="back">トップページに戻る</a>
  </div><!-- nowMatching_message_backここまで -->

	<footer>&copy; 2017&nbsp; ARAI CORPORATION.</footer>
</div><!--pageここまで-->
</body>
</html>