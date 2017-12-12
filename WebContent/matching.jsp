<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="modelPack.*"%>
<%@ page import="Utility.Utility" %>
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
    <title>マッチング画面</title>
</head>
</head>
<body>
    <h3>マッチング中</h3>
    <a href="matching?status=reload">更新</a>
    <br>
    <a href="matching?status=dest">破棄</a>
</body>