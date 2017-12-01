<%@ page contentType="text/html; charset=Windows-31J"%>
<%@ page import="modelPack.*"%>
<%@ page import="java.util.ArrayList"%>

<%

Cookie[] aryCookies = request.getCookies();
String key = null;

if (aryCookies != null)
{
    for (int i = 0; i < aryCookies.length; i++)
    {
        if (aryCookies[i].getName().equals("key"))
        {
            key = aryCookies[i].getValue();
        }
    }
}

%>

<!DOCTYPE html>
<html>

<head>
    <META charset="UTF-8">
    <title>テスト用画面</title>
</head>

<body>

<table>
<%for (int i=0;i<aryCookies.length;i++) {%>
    <TR>
        <TD><%=aryCookies[i].getName()%></TD>
        <TD><%=aryCookies[i].getValue()%></TD>
    </TR>
<%}%>
</table>

<h1>一人対戦マッチングテーブル</h1>
<table border="1">
    <tr>
        <th>マッチNo</th>
        <th>ユーザキー</th>
        <th>ユーザ名前</th>
        <th>開始時間</th>
        <th>終了時間</th>
        <th>パネル枚数</th>
    </tr>
    <%for (Match m : MatchList.getMatchList()){ 
          if (m.getPlayerCount() != 1)
          {
              continue;
          }
    %>
        <tr>
            <td><%=m.getMatchNo()%></td>
            <%for (User u : m.getUserList()) {%>
                <td><%=u.getKey()%></td>
                <td><%=u.getName()%></td>
            <% } %>
            <td><%=m.getStartTime()%></td>
            <td><%=m.getEndTime()%></td>
            <td><%=m.getPanelList().size()%></td>
        </tr>
    <%}%>
</table>

<h1>二人対戦マッチングテーブル</h1>
<table border="1">
    <tr>
        <th>マッチNo</th>
            <th>ユーザキー</th>
            <th>ユーザ名前</th>
            <th>ユーザキー</th>
            <th>ユーザ名前</th>
            <th>開始時間</th>
            <th>終了時間</th>
            <th>パネル枚数</th>
    </tr>
    <%for (Match m : MatchList.getMatchList()){ 
          if (m.getPlayerCount() != 2)
          {
              continue;
          }
    %>
        <tr>
            <td><%=m.getMatchNo()%></td>
            <%for (User u : m.getUserList()) {%>
                <td><%=u.getKey()%></td>
                <td><%=u.getName()%></td>
            <% } %>
            <td><%=m.getStartTime()%></td>
            <td><%=m.getEndTime()%></td>
            <td><%=m.getPanelList().size()%></td>
        </tr>
    <%}%>
</table>

<a href="top">トップへ戻る</a>

</body>