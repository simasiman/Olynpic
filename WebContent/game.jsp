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
response.addCookie(cooKey);
Cookie cooName = new Cookie("name", URLEncoder.encode(name, "UTF-8"));
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
$(function() {
	countDown();
});
function countDown() {
    var now = new Date(); //現在時刻
    <!--	var standardTime = new Date(1512978854142); //基準時刻（jsp(ミリ秒をlong型として)で取得）-->
    var endTime = new Date(<%=baseTime%>); //終了時刻（javaで基準時刻に15秒加算したものを(ミリ秒をlong型として)取得）
    var restTime = endTime.getTime() - now.getTime(); //残り時間
    var second = Math.floor(restTime/ 1000); //ミリ秒を秒に変換
    var oneTenthSecond = Math.floor((restTime% 1000)/ 100); //ミリ秒を0.1秒に変換

    if(restTime > 0) {
        $("#timer").text("あと " + second +"."+ oneTenthSecond + '秒');
    } else {
       	$("#timer").text("時間切れ！");
    }
    setTimeout('countDown()', 100); //0.1秒ごとに実行
}
<!--何もしなくても15秒で自動更新（要らなかったら消してください）-->
<!--	setTimeout("location.reload()",1000*15);-->
</script>
<title>[pane-tori] - ゲーム</title>
</head>
<body>
<%=HtmlGame.makeGameHtml(match,key)%>
</body>
