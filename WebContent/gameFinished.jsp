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

if (key == null || key.isEmpty())
{
    Cookie cooKey = new Cookie("key", Utility.getKey());
    response.addCookie(cooKey);
    response.sendRedirect("index.jsp");
    return;
}

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
</head>

</head>

<body>

<h1>※対戦が終了しました。</h1>

<%
String result = "";
int winLose = match.getUserWin(key);
switch (winLose)
{
    case User.WIN:
        result = "勝ちました。";
        break;
        
    case User.LOSE:
        result = "負けました。";
        break;
        
    case User.DRAW:
        result = "引き分けです。";
        break;
}

%>
<h2><%=result%></h2>

<table>
    <tr>
        <%
        ArrayList<Panel> panelList = match.getPanelList();
        %>
        <%for (int i = 0; i < panelList.size(); i++) {%>
            <td>
                <%=panelList.get(i).getBaseWord()%>
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
    for (User user : match.getUserList())
    {
        for (int j = 0; j < user.getSelectedPanel().size(); j++)
        {
            if (j == 0)
            {%>
                <h3><%=user.getName()%></h3>
                <h4><%=user.getScore()%></h4>
                <ul>
            <%}
            Panel p = user.getSelectedPanel().get(j);
            %>
            <li><%=p.getBaseWord()%>(<%=p.getSelectedWord().getWord()%>)
        <%}%>
        </ul>
    <%}%>

<%if (!match.isFinish()) {%>
    <a href="game">更新</a>
<%}%>

<a href="result">結果画面へ</a>

</body>
