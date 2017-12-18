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

if (aryCookies != null)
{
    for (Cookie cookie : aryCookies)
    {
        String ckName = cookie.getName();
        if (ckName.equals("key") && (key == null || key.isEmpty()))
        {
            key = cookie.getValue();
        }
        else if (ckName.equals("name") && (name == null || name.isEmpty()))
        {
            name = URLDecoder.decode(cookie.getValue(), "UTF-8");
        }
    }
}

if (key == null || key.isEmpty())
{
    response.sendRedirect("index");
    return;
}

Cookie cooKey = new Cookie("key", key);
cooKey.setMaxAge(60 * 60 * 24 * 90);
response.addCookie(cooKey);
Cookie cooName = new Cookie("name", URLEncoder.encode(name, "UTF-8"));
cooName.setMaxAge(60 * 60 * 24 * 90);
response.addCookie(cooName);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="css/reset.css" type="text/css">
<link rel="stylesheet" href="css/result.css" type="text/css">
<title>リザルト画面</title>
</head>
<body>
<div class="page">
<header><img src="img/logo/pane-tori-logo_s.png" alt="ゲームのロゴ"></header>

<%User user = match.getUser(key);%>
    選手名：<%=user.getName()%><br>
    点数　：<%=user.getScoreExam()%><br>
    ミス　：<%=user.getMiss()%><br>

<div class="wapper">
	<div class="rank">
		<div class=""><!-- 1位と2位でクラス名か画像を変える -->
			<img src="" alt="結果">
		</div>
	</div><!--rankここまで-->

	<div class="toTop"><a href="">トップページに戻る</a></div>

	<div class="contents clearfix">
	<div class="getPanel">
		<p>詳細結果</p>
		<ol>
		<li class="clearfix">
			<div>獲得したパネル</div>
			<div class="resultBlock first">
				<div class="score first">score + <span>bonus</span></div>
				<div class="getWord first">獲得した言葉</div>
			</div>
		</li>
		<%for (int j = 0; j < user.getSelectedPanel().size(); j++)
        {
        Panel p = user.getSelectedPanel().get(j);
        Word word = p.getSelectedWord();
        %>
		<li class="getPanelList clearfix">
			<img src="img/panel/<%=p.getPicture()%>" alt="<%=p.getBaseWord()%>" title="<%=p.getBaseWord()%>">
			<div class="resultBlock">
				<div class="score"><span><%=word.getBaseScore()%></span> ＋ <span><%=word.getBonusScore()%></span></div>
				<div class="getWord"><span>「<%=word.getWord()%>」</span></div>
			</div>
		</li>
		<%}%>
		</ol>
	</div><!--getPanelここまで-->
	
	<!--右側のカラム-->
	<div class="right">

		<div class="highScore">
			<div>ハイスコア（プレイヤー名）</div>
			<ol>
			<li>1.125</li>
			<li>2.122</li>
			<li>3.100</li>
			</ol>
		</div>
		
		<div class="rankingWapper">
			<div>ランキング（全体）</div>
			<div class="ranking">
				<div>ハイスコア</div>
				<ol>
				<li>1.ななしさん 125</li>
				<li>2.ナナチ 122</li>
				<li>3.名もなきアスリート 100</li>
				</ol>
			</div>
			<div class="ranking">
				<div>勝敗数</div>
				<ol>
				<li>1.ななしさん 125</li>
				<li>2.ナナチ 122</li>
				<li>3.名もなきアスリート 100</li>
				</ol>
			</div>
		<div class="ranking">
			<div>総スコア</div>
				<ol>
				<li>1.ななしさん 125</li>
				<li>2.ナナチ 122</li>
				<li>3.名もなきアスリート 100</li>
				</ol></div>
		</div>
	
	</div><!--rightここまで-->
	
</div><!--contentsここまで-->
	<div class="toTop"><a href="">トップページに戻る</a></div>
	<div class="logo"><img src="img/logo/arai_logo_s.png" alt="企業ロゴ"><img src="img/logo/olympic_logo.png" alt="大会ロゴ"></div>
	<div>ARAIはTOKYO2020を応援しています</div>
</div><!--wapperここまで-->
<footer><p>&copy; 2017 ARAI CORPORATION.</p></footer>
</div>
</body>
</html>