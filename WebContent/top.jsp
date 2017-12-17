<%@ page contentType="text/html; charset=UTF-8"%><%@ page import="java.net.URLDecoder"%><%@ page import="modelPack.*"%><%@ page import="Utility.Utility"%><%@ page import="java.net.*"%><%@ page import="java.util.ArrayList"%><%
Cookie[] aryCookies = request.getCookies();

String key = (String)session.getAttribute("key");
String name = (String)session.getAttribute("name");

if (aryCookies != null)
{
    for (int i = 0; i < aryCookies.length; i++)
    {
        String cookie = aryCookies[i].getName();
        if (cookie.equals("key") && (key == null || key.isEmpty()))
        {
            key = aryCookies[i].getValue();
        }
        else if (cookie.equals("name") && (name == null || name.isEmpty()))
        {
            name = URLDecoder.decode(aryCookies[i].getValue(), "UTF-8");
        }
    }
}

if (key == null || key.isEmpty())
{
    key = Utility.getDefaultKey();
}
if (name == null || name.isEmpty())
{
    name = Utility.getDefaultName();
}

Cookie cooKey = new Cookie("key", key);
response.addCookie(cooKey);
Cookie cooName = new Cookie("name", URLEncoder.encode(name, "UTF-8"));
response.addCookie(cooName);

//終了済みのマッチングが存在しないかを確認
Match match = MatchList.getMatchFinished(key);
if (match != null)
{
    // 終了済みのマッチングが存在すれば、リザルト画面へ遷移
    response.sendRedirect("result");
    return;
}
%><!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" charset="utf-8">
<link rel="stylesheet" href="css/reset.css" type="text/css">
<link rel="stylesheet" href="css/top.css" type="text/css">
<title>[pane-tori] - トップ</title>
</head>
<body>
	<div class="page">
		<div class="logo01">
			<img src="img/logo/arai_logo_s.png" width="124" height="124" alt="企業ロゴ">
		</div>
		<div class="logo02">
			<img src="img/logo/olympic_logo.png" width="182" height="124" alt="大会ロゴ">
		</div>
		<header>
			<p>ARAIはTOKYO2020を応援しています</p>
		</header>

		<div class="wapper">
			<div class="backImg">

				<div class="contents clearfix">
					<div class="gameLogo">
						<img src="img/logo/pane-tori-logo2.png" width="307" height="124">
					</div>
					<!--gameLogo終わり-->

					<div class="form">
						<div class="formBlock">
							<p class="setsumei">パネルを選んで言葉を繋げるゲームです。</p>

							<div class="playStart">
								<form name="form" action="matching" method="post">
									<input type="hidden" name="key" value="<%=key%>">
									<input type="text" name="name" size="20" maxlength="20" value="<%=name%>"><br>
									<input type="submit" name="mode1" value="1人プレイ" />
									<input type="submit" name="mode2" value="2人プレイ" />
									<input type="reset" value="名前のリセット">
								</form>

								<table class="setsumei">
									<tr>
										<th>1人プレイ</th>
										<td>1人でどれだけパネルを取れるかチャレンジするモードです。</td>
									</tr>
									<tr>
										<th>2人プレイ</th>
										<td>他のプレイヤー（オートマッチング）との対戦です</td>
								</table>

							</div><!--playStart終わり-->
						</div><!--formBlock終わり-->
					</div><!--form終わり-->
				</div><!--contents終わり-->
			</div><!--backImg終わり-->
		</div><!--wapper終わり-->

		<footer>
			<p>
				&copy; 2017 ARAI CORPORATION.<br>
				IEなど一部のブラウザでは、表示が一部制限されることがあります。
			</p>
		</footer>
	</div><!--page終わり-->
</body>
</html>
