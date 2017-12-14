<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="modelPack.*"%>
<%@ page import="Utility.*" %>
<%@ page import="java.net.*"%>
<%@ page import="java.util.ArrayList"%>
<%
Match match = (Match)request.getAttribute("match");

if (match == null)
{
    response.sendRedirect("index.jsp");
    return;
}

Cookie[] aryCookies = request.getCookies();

String key = null;
String name = null;

if (aryCookies != null)
{
    for (int i = 0; i < aryCookies.length; i++)
    {
        String cookie = aryCookies[i].getName();
        if (cookie.equals("key"))
        {
            key = aryCookies[i].getValue();
        }
        else if (cookie.equals("name"))
        {
            name = URLDecoder.decode(aryCookies[i].getValue(), "UTF-8");
        }
    }
}

if (key == null || key.isEmpty())
{
    response.sendRedirect("index");
    return;
}

if (name == null || name.isEmpty())
{
    name = Utility.getDefaultName();
    Cookie cooKey = new Cookie("name", URLEncoder.encode(name, "UTF-8"));
    response.addCookie(cooKey);
}

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
	var endTime = new Date(); //終了時刻（javaで基準時刻に15秒加算したものを(ミリ秒をlong型として)取得
    endTime.setSecond(endTime.getSecond() + 15);
	var restTime = endTime.getTime() - now.getTime(); //残り時間
	var second = Math.floor(restTime/ 1000); //ミリ秒を秒に変換
	var oneTenthSecond = Math.abs(Math.floor((restTime% 1000)/ 100)); //ミリ秒を0.1秒に変換

	$("#timer").text("あと " + second +"."+ oneTenthSecond + '秒');
		setTimeout('countDown()', 100); //0.1秒ごとに実行
	}
<!--何もしなくても15秒で自動更新（要らなかったら消してください）-->
<!--	setTimeout("location.reload()",1000*15);-->
</script>
<title>[pane-tori] - ゲーム</title>
</head>
<body>

<!--  プレイヤー1の選択情況の表示 -->
<%=HtmlGame.makeGameHtml(match,key)%>

<!-- 各種ボタンの表示 -->
<%=HtmlGame.makeGameReloadHtml(match, key)%>

</body>
