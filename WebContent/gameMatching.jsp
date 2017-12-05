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

<h3>マッチング中</h3>
<a href="matching?status=reload">更新</a>
<br>
<a href="matching?status=dest">破棄</a>

</body>
