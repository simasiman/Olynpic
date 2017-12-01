<%@ page import="Utility.Utility" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="modelPack.*"%>
<%@ page import="java.util.ArrayList"%>

<%

Match match = (Match)request.getAttribute("match");

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

//初回アクセス時の挙動については要修正
if (name == null || name.isEmpty())
{
    Cookie cooKey = new Cookie("name", (String)session.getAttribute("name"));
    response.addCookie(cooKey);
}

%>

<!DOCTYPE html>
<html>

<head>
    <META charset="UTF-8">
    <title>ゲーム画面</title>
</head>

<body>

<%=key%>
<%=name%>
<%=match.getPlayerCount()%>
<%=match.getPlayerTurn()%>

<%
if (!match.isStart())
{ %>
    <h3>マッチング中</h3>
    <a href="game">更新</a>
<%
    return;
}%>

<h1>ゲームで使用中のパネル</h1>
<table>
    <tr>
        <td><%=match.getMatchNo()%></td>
        <%for (User u : match.getUserList()) {%>
            <td><%=u.getKey()%></td>
            <td><%=u.getName()%></td>
        <% } %>
        <td><%=match.getStartTime()%></td>
        <td><%=match.getEndTime()%></td>
        <td><%=match.getPanelList().size()%></td>
    </tr>
</table>

<table>
    <tr>
        <%
        ArrayList<Panel> panelList = match.getPanelList();
        %>
        <%for (int i = 0; i < panelList.size(); i++) {%>
            <td>
                <%if (!match.isEnableContinue() || panelList.get(i).isUsed()) {%>
                    <%=panelList.get(i).getBaseWord()%>
                <%} else {%>
                    <a href="game?selectedPanel=<%=i%>">
                    <%=panelList.get(i).getBaseWord()%>
                    </a>
                <%}%>
            </td>
            <%if (i != 0 && i % 3 == 2) {%>
                </tr>
                <tr>
            <%}%>
        <%}%>
    </tr>
</table>

<ul>
    <li><%=match.getNowWord()%>
    <li><%=match.getFirstWord()%>
    <li><%=match.getLastWord()%>
</ul>

<h2>選択した単語</h2>
    
    <%
    for (int i = 0; i < match.getUserList().size(); i++)
    {
        User u = match.getUserList().get(i);
        if (i == 0)
        {%>
            <h3><%=u.getName()%></h3>
            <ul>
        <%}
        for (int j = 0; j < u.getSelectedPanel().size(); j++)
        {
            Panel p = u.getSelectedPanel().get(j);
        %>
            <li><%=p.getBaseWord()%>(<%=p.getSelectedWord().getWord()%>)
        <%}%>
    <%}%>
    </ul>

<%if (!match.isFinish()) {%>
    <a href="game">更新</a>
<%}%>

<a href="result">結果画面へ</a>

</body>
