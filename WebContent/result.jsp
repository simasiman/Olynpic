<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="modelPack.*"%>
<%@ page import="Utility.Utility" %>
<%@ page import="java.net.*"%>
<%@ page import="java.util.ArrayList"%>
<%

Match match = (Match)request.getAttribute("matchResult");

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

%>
<!DOCTYPE html>
<html>
<head>
    <META charset="UTF-8">
    <title>[pane-tori] - リザルト</title>
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