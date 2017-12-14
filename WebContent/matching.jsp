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
<meta http-equiv="content-type" charset="utf-8">
<link rel="stylesheet" href="css/reset.css" type="text/css">
<link rel="stylesheet" href="css/matching.css" type="text/css">
<title>[pane-tori] - マッチング</title>
</head>
<body>
<div class="page">
	<header><img src="img/logo/pane-tori-logo_s.png" alt="ゲームのロゴ"></header>

	<div class="nowMatching">只今マッチング中です</div>
	<a href="matching" class="reload">更新</a>
	<div class="message">マッチングが完了すると、自動的にゲームが開始されます</div>
	<a href="matching?status=dest" class="back">トップページに戻る</a>

	<footer>copy 2017&nbsp; ARAI CORPORATION.</footer>
</div><!--pageここまで-->
</body>
</html>