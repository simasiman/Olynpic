<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="modelPack.*"%>
<%@ page import="Utility.Utility" %>
<%@ page import="java.util.ArrayList"%>
<%
// マッチング情報の取得
Match match = (Match)request.getAttribute("matchResult");

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
    <title>結果画面</title>
</head>
<body>
    <h1>結果画面</h1>
    <%User user = match.getUser(key);%>
    選手名：<%=user.getName()%><br>
    点数　：<%=user.getScore()%><br>
    <a href="top">トップへ戻る</a>
    <h2>選択した単語</h2>
    <ul>
    <%for (int j = 0; j < user.getSelectedPanel().size(); j++)
    {
        Panel p = user.getSelectedPanel().get(j);
        Word word = p.getSelectedWord();
        %>
        <li><img class="mono" src="img/panel/<%=p.getPicture()%>" height="80"><%=p.getBaseWord()%>(<%=word.getWord()%>)[<%=word.getBaseScore()%>+<%=word.getBonusScore()%>]
    <%}%>
    </ul>
    <a href="top">トップへ戻る</a>
</body>