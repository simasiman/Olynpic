<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="modelPack.*"%>
<%@ page import="Utility.*" %>
<%@ page import="java.net.*"%>
<%@ page import="java.util.*"%>
<%

Match match = (Match)request.getAttribute("match");
if (match == null)
{
    response.sendRedirect("index.jsp");
    return;
}

Cookie[] aryCookies = request.getCookies();

String key = (String)session.getAttribute("key");
String name = (String)session.getAttribute("name");

if (key == null || key.isEmpty())
{
    response.sendRedirect("index");
    return;
}

Cookie cooKey = new Cookie("key", key);
cooKey.setMaxAge(60 * 60 * 24 * 90);
response.addCookie(cooKey);
Cookie cooName = new Cookie("name", URLEncoder.encode(name, "UTF-8"));
cooName.setMaxAge(60 * 60 * 24 * 90);
response.addCookie(cooName);

long baseTime = match.getSelectedTime().getTime() + (GameSetting.MATCH_TIME * 1000);

%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="css/reset.css" type="text/css">
<link rel="stylesheet" href="css/game_base.css" type="text/css">
<link rel="stylesheet" href="css/game_2.css" type="text/css">
<link rel="stylesheet" href="css/game_1.css" type="text/css">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript">
var baseTime = <%=baseTime%>;
var body = 

$(function() {
	countDown();
});
function countDown() {
    var now = new Date();
    var endTime = new Date(baseTime);
    var restTime = endTime.getTime() - now.getTime();
    var second = Math.floor(restTime/ 1000);
    var oneTenthSecond = Math.floor((restTime% 1000)/ 100);

    if(restTime > 0) {
        $("#timer").text("あと " + second + "." + oneTenthSecond + '秒');
    } else {
       	$("#timer").text("時間切れ！");
    }
    setTimeout('countDown()', 100); //0.1秒ごとに実行
}

WebSocketDemo = {};
(function(d) {
    function $(query) {
        return document.querySelector(query);
    }
    d.connect = function() {
        var ws = new WebSocket("ws://<%=GameSetting.SERVER_ADDRESS%>:8080/<%=GameSetting.PROJECT_NAME%>/game");
        ws.onmessage = function(message) {
            var text = message.data;
            document.getElementsByTagName("body")[0].innerHTML = text;
            if (text.indexOf('<!--[selected]-->') == 0)
            {
            	baseTime = new Date().getTime() + (<%=GameSetting.MATCH_TIME%> * 1000);;
            }
        };
        ws.onopen = function() {
        	ws.send(<%=key%>);
        }
        d.webSocket = ws;
    };
    d.send = function(text) {
    	d.webSocket.send(text);
    };
}) (WebSocketDemo);

window.onload = function() {
	WebSocketDemo.connect();
}
</script>
<title>[pane-tori] - ゲーム</title>
</head>
<body>
<%=HtmlGame.makeGameHtml(match,key)%>
</body>
