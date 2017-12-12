<%@page import="java.net.URLDecoder"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="modelPack.*"%>
<%@ page import="Utility.Utility"%>
<%@ page import="java.net.*"%>
<%@ page import="java.util.ArrayList"%>
<%
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
    key = Utility.getDefaultKey();
    Cookie cooKey = new Cookie("key", key);
    response.addCookie(cooKey);
}

if (name == null || name.isEmpty())
{
    name = Utility.getDefaultName();
    Cookie cooKey = new Cookie("name", URLEncoder.encode(name, "UTF-8"));
    response.addCookie(cooKey);
}

//終了済みのマッチングが存在しないかを確認
Match match = MatchList.getMatchFinished(key);
if (match != null)
{
    // 終了済みのマッチングが存在すれば、リザルト画面へ遷移
    response.sendRedirect("result");
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
    <form name ="form" action="matching" method="post">
        <input type="hidden" name="key" value="<%=key%>">   
        <input type="text" name="name" value="<%=name%>">
        <input type="submit" name="mode1" value="1人"/>
        <input type="submit" name="mode2" value="2人"/>
    </form>
    <a href="forTester.jsp">テスター画面</a>
</body>