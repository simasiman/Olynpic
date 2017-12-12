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
    <META charset="UTF-8">
    <title>[pane-tori] - ゲーム</title>
    <link rel="stylesheet" href="css/reset.css" type="text/css">
    <link rel="stylesheet" href="css/game_base.css" type="text/css">
    <link rel="stylesheet" href="css/game_2.css" type="text/css"><!--1人用と2人用でcssを切り替えたい-->
<style type="text/css">
.selected {
    border: 2px solid black;
}
.selected img{
    -webkit-filter: grayscale(100%);
    filter: grayscale(100%);
}
.canSelect {
    border: 2px solid blue;
}
.correct {
    border: 2px solid red;
}
    </style>
</head>
</head>
<body>
<!-- 各種メッセージの表示 -->
<!-- 各種ボタンの表示 -->
<%=HtmlGame.makeGameReloadHtml(match, key)%>
<!-- ゲーム状況の表示 -->
<%=HtmlGame.makeGameMessageHtml(match, key)%>
<!-- ゲーム選択パネルの表示 -->
<%=HtmlGame.makeGamePanelHtml(match, key)%>
<!-- 選択された単語、現在選択可能な文字の表示 -->
<%=HtmlGame.makeUserSelectedHtml(match, key)%>
<!-- 各種ボタンの表示 -->
<%=HtmlGame.makeGameReloadHtml(match, key)%>
</body>
