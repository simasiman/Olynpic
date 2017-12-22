<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" charset="utf-8">
<link rel="stylesheet" href="css/reset.css" type="text/css">
<link rel="stylesheet" href="css/matching.css" type="text/css">
<style type="text/css">
.input-file .preview {
  background-image: url(/img/panel/160x120.png);
}
.input-file input[type="file"] {
  opacity: 0;
}
</style>
<script>
//documentと毎回書くのがだるいので$に置き換え
var $ = document;
var $form = $.querySelector('form');// jQueryの $("form")相当

//jQueryの$(function() { 相当(ただし厳密には違う)
$.addEventListener('DOMContentLoaded', function() {
	//画像ファイルプレビュー表示
	//  jQueryの $('input[type="file"]')相当
	// addEventListenerは on("change", function(e){}) 相当
	$.querySelector('input[type="file"]').addEventListener('change', function(e) {
		var file = e.target.files[0],
			   reader = new FileReader(),
			   $preview =  $.querySelector(".preview"), // jQueryの $(".preview")相当
			   t = this;

		// 画像ファイル以外の場合は何もしない
		if(file.type.indexOf("image") < 0){
		  return false;
		}

		reader.onload = (function(file) {
		  return function(e) {
			 //jQueryの$preview.empty(); 相当
			 while ($preview.firstChild) $preview.removeChild($preview.firstChild);

			// imgタグを作成
			var img = document.createElement( 'img' );
			img.setAttribute('src',  e.target.result);
			img.setAttribute('width', '150px');
			img.setAttribute('title',  file.name);
			// imgタグを$previeの中に追加
			$preview.appendChild(img);
		  };
		})(file);

		reader.readAsDataURL(file);
	});
});

function check(){
	var inputFlg = 0;
	var lengthFlg = 0;

	var colorSafe = "#FFFFFF";
	var colorError = "#FF9999";
	var colorErrorLength = "#99FF99";

	var field = document.getElementsByName("panelImage")[0];
	if(field.value == "")
	{
		inputFlg = 1;
		field.style.backgroundColor = colorError;
	}
	else
	{
		field.style.backgroundColor = colorSafe;
	}

	var field = document.getElementsByName("panelName")[0];
	if(field.value == "")
	{
		inputFlg = 1;
		field.style.backgroundColor = colorError;
	}
	else
	{
		field.style.backgroundColor = colorSafe;
	}

	var i = 0;
	for (i = 1; i <= 8; i++)
	{
		var field = document.getElementsByName( "Disp" + i)[0];

		if(field.value == "")
		{
			inputFlg = 1;
			field.style.backgroundColor = colorError;
		}
		else
		{
			field.style.backgroundColor = colorSafe;
		}
	}

	var i = 0;
	for (i = 1; i <= 8; i++)
	{
		var field = document.getElementsByName( "Read" + i)[0];

		if(field.value == "")
		{
			inputFlg = 1;
			field.style.backgroundColor = colorError;
		}
		else
		{
			field.style.backgroundColor = colorSafe;
		}
		
		if (field.value.length > 30)
	    {
			lengthFlg = 1;
			field.style.backgroundColor = colorErrorLength
		}
		else
		{
			field.style.backgroundColor = colorSafe;
		}
		
	}

	if(inputFlg){
		window.alert('パネル名と「表示名＋よみかな」＊８パターンを入力してください');
		return false;
	}
	else if (lengthFlg){
		window.alert('「よみかな」の文字数は30字以内にしてください');
		return false;
	}
	else{
		return true;
	}

}
</script>
<title>[pane-tori] - パネル登録</title>
</head>
<body>

<div class="page">
<header><img src="img/logo/pane-tori-logo_s.png" alt="ゲームのロゴ"></header>
<div class="wapper">
	<h1>パネル登録</h1>
	<form action="regist" method="post" enctype="multipart/form-data" onSubmit="return check();">
        <input type="hidden" name="isUpload" value="1">
        <br>
		<p class="btn_upload">
			画像ファイルを選択してアップロード
		</p>
        <br>
		<input type="file" name="panelImage">
		<div class="preview"></div>
        <br>
		パネル名：<input type="text" name="panelName"><br>
		<br>
		<table border="1">
			<tr>
			    <th>
				<th>表示
				<th>よみ(ひらかな)
			</tr>
			<tr>
			    <td>1.
				<td><input type="text" name="Disp1">
				<td><input type="text" name="Read1">
			</tr>
			<tr>
			    <td>2.
				<td><input type="text" name="Disp2">
				<td><input type="text" name="Read2">
			</tr>
			<tr>
			    <td>3.
				<td><input type="text" name="Disp3">
				<td><input type="text" name="Read3">
			</tr>
			<tr>
			    <td>4.
				<td><input type="text" name="Disp4">
				<td><input type="text" name="Read4">
			</tr>
			<tr>
			    <td>5.
				<td><input type="text" name="Disp5">
				<td><input type="text" name="Read5">
			</tr>
			<tr>
			    <td>6.
				<td><input type="text" name="Disp6">
				<td><input type="text" name="Read6">
			</tr>
			<tr>
			    <td>7.
				<td><input type="text" name="Disp7">
				<td><input type="text" name="Read7">
			</tr>
			<tr>
			    <td>8.
				<td><input type="text" name="Disp8">
				<td><input type="text" name="Read8">
			</tr>
		</table>
		<br>
		<p>
		    画像を指定し、８つ全ての単語「表示名：よみかな」を入力してください。
		</p>
		<br>
		<p>
			<input type="submit" value="送信">
		</p>
		<br>
	</form>

	<p class="uploadMessage"><%=request.getAttribute("message")%></p>

	<a href="top" class="back">トップへ戻る</a>

</div>
<footer>&copy; 2017&nbsp; ARAI CORPORATION.</footer>
</div>

</body>