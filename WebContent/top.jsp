<%@ page import="Utility.Utility" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="modelPack.*"%>
<%@ page import="java.util.ArrayList"%>

<%

Cookie[] aryCookies = request.getCookies();
String key = null;
String name = null;

if (aryCookies != null)
{
    for (int i = 0; i < aryCookies.length; i++)
    {
        switch (aryCookies[i].getName())
        {
            case "key":
                key = aryCookies[i].getValue();
                break;
            case "name":
                name = aryCookies[i].getValue();
                break;
        }
    }
}

//初回アクセス時の挙動については要修正
if (key == null || key.isEmpty())
{
    Cookie cooKey = new Cookie("key", Utility.getKey());
    response.addCookie(cooKey);
    response.sendRedirect("index.jsp");
    return;
}

%>

<!DOCTYPE html>
<html>

<head>
    <META charset="UTF-8">
    <title>トップ画面</title>
</head>

<body>

<form name ="form" action="game" method="post">
    <input type="text" name="key" value="<%=key%>">   
    <input type="text" name="name" value="<%=name%>">
    <input type="submit" name="mode1" value="一人対戦"/>
    <input type="submit" name="mode2" value="二人対戦"/>
</form>

<a href="forTester.jsp">テスター画面</a>

</body>