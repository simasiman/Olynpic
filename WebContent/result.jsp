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

User user = match.getUser(key);
int playerCount = match.getPlayerCount();

String clsWinLose = "";
String message = "";
String imageAlt = "";
String image = "img/result/";
if (match.getPlayerCount() == 1)
{
    clsWinLose = "fin ";
    message = "CONGRATULATIONS!!";
    image += "fin.png";
    imageAlt = "勝った人";
}
else
{
    switch (user.getWin())
    {
        case User.WIN:
            clsWinLose = "win ";
            message = "WIN!!";
            image += "win.png";
            imageAlt = "勝った人";
            break;
        case User.LOSE:
            clsWinLose = "lose ";
            message = "LOSE...";
            image += "lose.png";
            imageAlt = "負けた人";
            break;
        case User.DRAW:
            // 引き分け処理については未実装
            clsWinLose = "";
            message = "";
            image += "";
            break;
    }
}
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
<div class="wapper">

	<div class="result <%=clsWinLose%>">
		<div class="resultText <%=clsWinLose%>" title="<%=message%>"><%=message%></div>
		<div class="resultScore <%=clsWinLose%> clearfix">
			<div>Total score : <span><%=user.getScore()%></span></div>
			<div>
			score : <%=user.getTotalScoreBase()%><br>
			＋<span>bonus : <%=user.getTotalScoreBonus()%></span><br>
			＊<span>ヒントの有無での加算</span> : <%=user.getScoreMultiple()%>
			</div>
		</div><!--resultScoreここまで-->
		
		<img class="figure <%=clsWinLose%>" src="<%=image%>" alt="<%=imageAlt%>">
		<%if (match.getPlayerCount() == 2) {%>
		<img class="podium" src="img/result/podium.png" alt="表彰台">
		<%}%>
	</div><!--resultここまで-->

	<div class="toTop">
	
	<a href="">トップページに戻る</a></div>

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
            String picture = "";
            if (p.isOriginal())
            {
                picture = "img/panel/" + p.getPicture();
            }
            else
            {
            picture = "img/userUpload/" + p.getPicture();
            }
        %>
		<li class="getPanelList clearfix">
			<img src="<%=picture%>" alt="<%=p.getBaseWord()%>" title="<%=p.getBaseWord()%>">
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
		<div><%=playerCount%>人プレイ ランキング</div>
		<div class="highScore">
			<div>ハイスコア<span><%=name%></span></div>

			<ol>
			<%
			ArrayList<User> privateHighScore = (ArrayList<User>)request.getAttribute("PrivateHighScore");
			if (privateHighScore == null)
			{
			    privateHighScore = new ArrayList<User>();
			}
			for (int i = 0; i < privateHighScore.size(); i++)
			{
			    User u = privateHighScore.get(i);
			%>
			<li class="player clearfix">
				<div class="rank"><%=i+1%>.</div>
				<div class="num"><%=u.getHighScore()%></div>
			</li>
			<%}%>
			</ol>
		</div><!--highScoreここまで-->
		
		<div class="rankingWapper">
			<div>ランキング（全体）</div>
			<div class="ranking">
				<div>ハイスコア</div>
				<ol>
				<%
			    ArrayList<User> totalHighScore = (ArrayList<User>)request.getAttribute("TotalHighScore");
			    if (totalHighScore == null)
			    {
			        totalHighScore = new ArrayList<User>();
    			}
    			for (int i = 0; i < totalHighScore.size(); i++)
    			{
    			    User u = totalHighScore.get(i);
    			%>
				<li class="clearfix">
					<div class="rank"><%=i+1%>.</div>
					<div class="name"><%=u.getName()%></div>
					<div class="num"><%=u.getHighScore()%></div>
				</li>
				<%}%>
				</ol>
			</div><!--rankingここまで-->

			<div class="ranking">
				<div>勝敗数</div>
				<ol>
				<%
			    ArrayList<User> totalWinLose = (ArrayList<User>)request.getAttribute("TotalWinLose");
			    if (totalWinLose == null)
			    {
			        totalWinLose = new ArrayList<User>();
    			}
    			for (int i = 0; i < totalWinLose.size(); i++)
    			{
    			    User u = totalWinLose.get(i);
    			%>
				<li class="player clearfix">
					<div class="rank"><%=i+1%>.</div>
					<div class="name"><%=u.getName()%></div>
					<div class="num"><%=u.getWinCount()%></div>
				</li>
				<%}%>
				</ol>
			</div><!--rankingここまで-->

		</div><!--rankingWapperここまで-->

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