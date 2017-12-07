<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="modelPack.*"%>
<%@ page import="Utility.*" %>
<%@ page import="java.util.ArrayList"%>
<%
// マッチング情報の取得
Match match = (Match)request.getAttribute("match");

// マッチング情報が存在しなければ、トップ画面に遷移する
if (match == null)
{
    response.sendRedirect("top");
    return;
}

// クッキーによる名前の取得と設定
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
            name = aryCookies[i].getValue();            
        }
    }
}

// キーが未設定であればクッキーに設定
if (key == null || key.isEmpty())
{
    Cookie cooKey = new Cookie("key", Utility.getKey());
    response.addCookie(cooKey);
    response.sendRedirect("index.jsp");
    return;
}

// 名前が未設定であればクッキーに設定
if (name == null || name .isEmpty())
{
    Cookie cooName = new Cookie("name", (String)session.getAttribute("name"));
    response.addCookie(cooName);
}

%>
<!DOCTYPE html>
<html>
<head>
    <META charset="UTF-8">
    <title>ゲーム画面</title>
    
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
