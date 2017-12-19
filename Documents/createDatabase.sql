############################################
#       しりとり用データベース作成パッチ         #
############################################

# データベースの削除と作成(再作成)
DROP DATABASE PaneTori IF EXISTS;
CREATE DATABASE PaneTori;

# データベースの選択
USE PaneTori;

# 競技名単語テーブルの作成
CREATE TABLE tbl_word_base(
    ath_id  INT  ,
    name    TEXT ,
    picture TEXT
);

# しりとり単語テーブルの作成
CREATE TABLE tbl_word_siritori(
    ath_id    INT  ,
    word_id   INT  ,
    word_disp TEXT ,
    word_read TEXT ,
    level     INT
);

# ユーザテーブルの作成
CREATE TABLE tbl_user(
    user_key  TEXT ,
    name      TEXT
);

# マッチングテーブルの作成
CREATE TABLE tbl_play_result(
    id          INT  ,
    user_key    TEXT ,
    playdate    DATE ,
    winlose     INT  ,
    score       INT  ,
    playerCount INT
);

# 競技名単語テーブルにデータを挿入
INSERT INTO tbl_word_base VALUES(1,"バスケットボール","panel_basketball.png");
INSERT INTO tbl_word_base VALUES(2,"水泳","panel_swimming.png");
INSERT INTO tbl_word_base VALUES(3,"柔道","panel_judo.png");
INSERT INTO tbl_word_base VALUES(4,"バレーボール","panel_volleyball.png");
INSERT INTO tbl_word_base VALUES(5,"フェンシング","panel_fencing.png");
INSERT INTO tbl_word_base VALUES(6,"テニス","panel_tennis.png");
INSERT INTO tbl_word_base VALUES(7,"カヌー","panel_canoe.png");
INSERT INTO tbl_word_base VALUES(8,"ボクシング","panel_boxing.png");
INSERT INTO tbl_word_base VALUES(9,"馬術","panel_horsemanship.png");
INSERT INTO tbl_word_base VALUES(10,"ウェイトリフティング","panel_weightlifting.png");
INSERT INTO tbl_word_base VALUES(11,"体操","panel_gymnastics.png");
INSERT INTO tbl_word_base VALUES(12,"陸上","panel_athletics.png");
INSERT INTO tbl_word_base VALUES(13,"レスリング","panel_wrestling.png");
INSERT INTO tbl_word_base VALUES(14,"バドミントン","panel_badminton.png");
INSERT INTO tbl_word_base VALUES(15,"卓球","panel_tabletennis.png");
INSERT INTO tbl_word_base VALUES(16,"野球","panel_baseball.png");
INSERT INTO tbl_word_base VALUES(17,"アーチェリー","panel_archery.png");
INSERT INTO tbl_word_base VALUES(18,"ホッケー","panel_hockey.png");
INSERT INTO tbl_word_base VALUES(19,"ゴルフ","panel_golf.png");
INSERT INTO tbl_word_base VALUES(20,"ハンドボール","panel_handball.png");
INSERT INTO tbl_word_base VALUES(21,"スケートボード","panel_skateboard.png");
INSERT INTO tbl_word_base VALUES(22,"サーフィン","panel_surfing.png");
INSERT INTO tbl_word_base VALUES(23,"ラグビー","panel_rugby.png");
INSERT INTO tbl_word_base VALUES(24,"空手","panel_karate.png");

# しりとり単語テーブルにデータを挿入
INSERT INTO tbl_word_siritori VALUES (1,1,"バスケットボール","はすけつとほーる",0);
INSERT INTO tbl_word_siritori VALUES (1,2,"ダンク","たんく",0);
INSERT INTO tbl_word_siritori VALUES (1,3,"コーナー","こーなー",0);
INSERT INTO tbl_word_siritori VALUES (1,4,"ボール","ほーる",0);
INSERT INTO tbl_word_siritori VALUES (1,5,"シュート","しゆーと",0);
INSERT INTO tbl_word_siritori VALUES (1,6,"レイアップ","れいあっふ",1);
INSERT INTO tbl_word_siritori VALUES (1,7,"フリースロー","ふりーすろー",1);
INSERT INTO tbl_word_siritori VALUES (1,8,"スリーポイント","すりーほいんと",1);
INSERT INTO tbl_word_siritori VALUES (1,9,"ゾーンディフェンス","そーんていふえんす",2);
INSERT INTO tbl_word_siritori VALUES (1,10,"ディスクォリファリングファウル","ていすくおりふありんくふあうる",2);
INSERT INTO tbl_word_siritori VALUES (2,1,"水泳","すいえい",0);
INSERT INTO tbl_word_siritori VALUES (2,2,"ターン","たーん",0);
INSERT INTO tbl_word_siritori VALUES (2,3,"キック","きつく",0);
INSERT INTO tbl_word_siritori VALUES (2,4,"クロール","くろーる",0);
INSERT INTO tbl_word_siritori VALUES (2,5,"ゴーグル","こーくる",0);
INSERT INTO tbl_word_siritori VALUES (2,6,"バタフライ","はたふらい",1);
INSERT INTO tbl_word_siritori VALUES (2,7,"サイドキック","さいときつく",2);
INSERT INTO tbl_word_siritori VALUES (2,8,"フリップターン","ふりつぷたーん",2);
INSERT INTO tbl_word_siritori VALUES (3,1,"じゅうどう","しゆうとう",0);
INSERT INTO tbl_word_siritori VALUES (3,2,"おび","おひ",0);
INSERT INTO tbl_word_siritori VALUES (3,3,"どうぎ","とうき",0);
INSERT INTO tbl_word_siritori VALUES (3,4,"うちまた","うちまた",1);
INSERT INTO tbl_word_siritori VALUES (3,5,"もろてがり","もろてかり",2);
INSERT INTO tbl_word_siritori VALUES (3,6,"せおいなげ","せいおなけ",1);
INSERT INTO tbl_word_siritori VALUES (3,7,"いっぽんがち","いつほんかち",1);
INSERT INTO tbl_word_siritori VALUES (3,8,"うでひしぎじゅうじがため","うてひしきしゆうしかため",2);
INSERT INTO tbl_word_siritori VALUES (4,1,"バレーボール","はれーほーる",0);
INSERT INTO tbl_word_siritori VALUES (4,2,"ボール","ほーる",0);
INSERT INTO tbl_word_siritori VALUES (4,3,"ネット","ねつと",0);
INSERT INTO tbl_word_siritori VALUES (4,4,"サーブ","さーふいん",0);
INSERT INTO tbl_word_siritori VALUES (4,5,"コート","こーと",0);
INSERT INTO tbl_word_siritori VALUES (4,6,"アタック","あたつく",0);
INSERT INTO tbl_word_siritori VALUES (4,7,"アンテナ","あんてな",1);
INSERT INTO tbl_word_siritori VALUES (4,8,"スパイク","すはいく",1);
INSERT INTO tbl_word_siritori VALUES (4,9,"レシーブ","れしーふ",0);
INSERT INTO tbl_word_siritori VALUES (4,10,"はくたい","はくたい",2);
INSERT INTO tbl_word_siritori VALUES (4,11,"エンドライン","えんとらいん",1);
INSERT INTO tbl_word_siritori VALUES (4,12,"アタックライン","あたつくらいん",1);
INSERT INTO tbl_word_siritori VALUES (4,13,"フローターサーブ","ふろーたーさーふ",1);
INSERT INTO tbl_word_siritori VALUES (4,14,"オーバーハンドパス","おーはーはんとはす",1);
INSERT INTO tbl_word_siritori VALUES (5,1,"フェンシング","ふえんしんく",0);
INSERT INTO tbl_word_siritori VALUES (5,2,"けん","けん",0);
INSERT INTO tbl_word_siritori VALUES (5,3,"エペ","えへ",0);
INSERT INTO tbl_word_siritori VALUES (5,4,"マスク","ますく",0);
INSERT INTO tbl_word_siritori VALUES (5,5,"フルーレ","ふるーれ",0);
INSERT INTO tbl_word_siritori VALUES (5,6,"サーブル","さーふる",0);
INSERT INTO tbl_word_siritori VALUES (5,7,"ボンナバン","ほんなはん",2);
INSERT INTO tbl_word_siritori VALUES (5,8,"オンガード","おんかーと",2);
INSERT INTO tbl_word_siritori VALUES (5,9,"コントルシス","こんとるしす",2);
INSERT INTO tbl_word_siritori VALUES (5,10,"ボンナリエール","ほんなりえーる",2);
INSERT INTO tbl_word_siritori VALUES (5,11,"アロンジェルブラ","あるんしえるふら",2);
INSERT INTO tbl_word_siritori VALUES (6,1,"テニス","てにす",0);
INSERT INTO tbl_word_siritori VALUES (6,2,"ロブ","ろふ",1);
INSERT INTO tbl_word_siritori VALUES (6,3,"ボール","ほーる",0);
INSERT INTO tbl_word_siritori VALUES (6,4,"ネット","ねっと",0);
INSERT INTO tbl_word_siritori VALUES (6,5,"エース","えーす",1);
INSERT INTO tbl_word_siritori VALUES (6,6,"サービス","さーひす",0);
INSERT INTO tbl_word_siritori VALUES (6,7,"ラケット","らけつと",0);
INSERT INTO tbl_word_siritori VALUES (6,8,"フォールト","ふおーると",1);
INSERT INTO tbl_word_siritori VALUES (6,9,"ストローク","すとろーく",1);
INSERT INTO tbl_word_siritori VALUES (6,10,"スマッシュ","すまつしゆ",0);
INSERT INTO tbl_word_siritori VALUES (6,11,"アドバンテージ","あとはんてーし",2);
INSERT INTO tbl_word_siritori VALUES (6,12,"ダブルフォールト","たふるうふおーると",2);
INSERT INTO tbl_word_siritori VALUES (6,13,"パッシングショット","はつしんくしよつと",2);
INSERT INTO tbl_word_siritori VALUES (7,1,"カヌー","かぬー",0);
INSERT INTO tbl_word_siritori VALUES (7,2,"バウ","はう",1);
INSERT INTO tbl_word_siritori VALUES (7,3,"パドル","はとる",1);
INSERT INTO tbl_word_siritori VALUES (7,4,"ダッキー","たつきー",2);
INSERT INTO tbl_word_siritori VALUES (7,5,"クリーク","くりーく",2);
INSERT INTO tbl_word_siritori VALUES (7,6,"ヘルメット","へるめつと",0);
INSERT INTO tbl_word_siritori VALUES (7,7,"ローリング","ろーりんく",2);
INSERT INTO tbl_word_siritori VALUES (7,8,"チキンルート","ちきんるーと",2);
INSERT INTO tbl_word_siritori VALUES (7,9,"ライフジャケット","らいふしやけつと",0);
INSERT INTO tbl_word_siritori VALUES (7,10,"ドローストローク","とろーすとろーく",2);
INSERT INTO tbl_word_siritori VALUES (7,11,"リバースストローク","りはーすすとろーく",2);
INSERT INTO tbl_word_siritori VALUES (7,12,"スイープストローク","すいーふすとろーく",2);
INSERT INTO tbl_word_siritori VALUES (7,13,"フォワードストローク","ふおわーとすとろーく",2);
INSERT INTO tbl_word_siritori VALUES (8,1,"ボクシング","ほくしんく",0);
INSERT INTO tbl_word_siritori VALUES (8,2,"パンチ","はんち",0);
INSERT INTO tbl_word_siritori VALUES (8,3,"フック","ふつく",1);
INSERT INTO tbl_word_siritori VALUES (8,4,"ジャブ","しやふ",1);
INSERT INTO tbl_word_siritori VALUES (8,5,"リング","りんく",0);
INSERT INTO tbl_word_siritori VALUES (8,6,"アッパー","あつはー",1);
INSERT INTO tbl_word_siritori VALUES (8,7,"ダッキング","たつきんく",1);
INSERT INTO tbl_word_siritori VALUES (8,8,"ヘッドギア","へつときあ",0);
INSERT INTO tbl_word_siritori VALUES (8,9,"ストレート","すとれーと",1);
INSERT INTO tbl_word_siritori VALUES (8,10,"インファイト","いんふあいと",2);
INSERT INTO tbl_word_siritori VALUES (8,11,"コークスクリュー","こーくすくりゆー",2);
INSERT INTO tbl_word_siritori VALUES (8,12,"ヒットアンドアウェー","ひつとあんとあうえー",2);
INSERT INTO tbl_word_siritori VALUES (9,1,"馬術","はしゆつ",0);
INSERT INTO tbl_word_siritori VALUES (9,2,"馬","うま",0);
INSERT INTO tbl_word_siritori VALUES (9,3,"鐙","あふみ",1);
INSERT INTO tbl_word_siritori VALUES (9,4,"騎手","きしゆ",0);
INSERT INTO tbl_word_siritori VALUES (9,5,"手綱","たつな",0);
INSERT INTO tbl_word_siritori VALUES (9,6,"駈歩","かけあし",1);
INSERT INTO tbl_word_siritori VALUES (9,7,"速歩","はやあし",1);
INSERT INTO tbl_word_siritori VALUES (9,8,"ギャロップ","きやろつふ",1);
INSERT INTO tbl_word_siritori VALUES (9,9,"イベンティング","いへんていんく",2);
INSERT INTO tbl_word_siritori VALUES (9,10,"ドレッサージュ","とれつさーしゆ",2);
INSERT INTO tbl_word_siritori VALUES (10,1,"ウェイトリフティング","うえいとりふていんく",0);
INSERT INTO tbl_word_siritori VALUES (10,2,"プレス","ふれす",1);
INSERT INTO tbl_word_siritori VALUES (10,3,"スナッチ","すなつち",1);
INSERT INTO tbl_word_siritori VALUES (10,4,"リフター","りふたー",1);
INSERT INTO tbl_word_siritori VALUES (10,5,"バーベル","はーへる",1);
INSERT INTO tbl_word_siritori VALUES (10,6,"きんにく","きんにく",0);
INSERT INTO tbl_word_siritori VALUES (10,7,"マッスル","まつする",0);
INSERT INTO tbl_word_siritori VALUES (10,8,"スクワット","すくわつと",1);
INSERT INTO tbl_word_siritori VALUES (10,9,"ベンチプレス","へんちふれす",2);
INSERT INTO tbl_word_siritori VALUES (10,10,"プラットフォーム","ふらつとふおーむ",2);
INSERT INTO tbl_word_siritori VALUES (11,1,"たいそう","たいそう",0);
INSERT INTO tbl_word_siritori VALUES (11,2,"ゆか","ゆか",1);
INSERT INTO tbl_word_siritori VALUES (11,3,"跳馬","ちょうは",1);
INSERT INTO tbl_word_siritori VALUES (11,4,"鉄棒","てつほう",1);
INSERT INTO tbl_word_siritori VALUES (11,5,"あん馬","あんは",1);
INSERT INTO tbl_word_siritori VALUES (11,6,"つり輪","つりわ",1);
INSERT INTO tbl_word_siritori VALUES (11,7,"平行棒","へいこうほう",1);
INSERT INTO tbl_word_siritori VALUES (11,8,"マット","まつと",0);
INSERT INTO tbl_word_siritori VALUES (11,9,"踏み切り","ふみきり",0);
INSERT INTO tbl_word_siritori VALUES (11,10,"アクロバット","あくろばつと",2);
INSERT INTO tbl_word_siritori VALUES (11,11,"ムーンサルト","むーんさると",2);
INSERT INTO tbl_word_siritori VALUES (12,1,"陸上競技","りくしようきようき",0);
INSERT INTO tbl_word_siritori VALUES (12,2,"混成","こんせい",0);
INSERT INTO tbl_word_siritori VALUES (12,3,"ロード","ろーと",0);
INSERT INTO tbl_word_siritori VALUES (12,4,"リレー","りれー",0);
INSERT INTO tbl_word_siritori VALUES (12,5,"円盤投げ","えんはんなけ",1);
INSERT INTO tbl_word_siritori VALUES (12,6,"砲丸投げ","ほうかんなけ",1);
INSERT INTO tbl_word_siritori VALUES (12,7,"やり投げ","やりなけ",1);
INSERT INTO tbl_word_siritori VALUES (12,8,"ハンマー投げ","はんまーなけ",1);
INSERT INTO tbl_word_siritori VALUES (12,9,"マラソン","まらそん",1);
INSERT INTO tbl_word_siritori VALUES (12,10,"トラック","とらつく",1);
INSERT INTO tbl_word_siritori VALUES (12,11,"フィールド","ふいーると",1);
INSERT INTO tbl_word_siritori VALUES (12,12,"走り高跳び","はしりたかとひ",1);
INSERT INTO tbl_word_siritori VALUES (13,1,"レスリング","れすりんく",0);
INSERT INTO tbl_word_siritori VALUES (13,2,"グレコローマン","くれころーまん",2);
INSERT INTO tbl_word_siritori VALUES (13,3,"フリースタイル","ふりーすたいる",2);
INSERT INTO tbl_word_siritori VALUES (13,4,"マット","まつと",0);
INSERT INTO tbl_word_siritori VALUES (13,5,"タックル","たつくる",0);
INSERT INTO tbl_word_siritori VALUES (13,6,"ローリング","ろーりんく",1);
INSERT INTO tbl_word_siritori VALUES (13,7,"リフトアップ","りふとあつふ",1);
INSERT INTO tbl_word_siritori VALUES (13,8,"パシビティゾーン","ぱしびていそーん",2);
INSERT INTO tbl_word_siritori VALUES (14,1,"バドミントン","はとみんとん",0);
INSERT INTO tbl_word_siritori VALUES (14,2,"シャトル","しやとる",0);
INSERT INTO tbl_word_siritori VALUES (14,3,"コート","こーと",0);
INSERT INTO tbl_word_siritori VALUES (14,4,"ネット","ねつと",0);
INSERT INTO tbl_word_siritori VALUES (14,5,"ラケット","らけつと",0);
INSERT INTO tbl_word_siritori VALUES (14,6,"ガット","かつと",0);
INSERT INTO tbl_word_siritori VALUES (14,7,"スマッシュ","すまつしゆ",1);
INSERT INTO tbl_word_siritori VALUES (14,8,"ドライブ","とらいふ",1);
INSERT INTO tbl_word_siritori VALUES (14,9,"ドロップショット","とろつふしよつと",2);
INSERT INTO tbl_word_siritori VALUES (14,10,"オーバーネット","おーばーねつと",2);
INSERT INTO tbl_word_siritori VALUES (15,1,"卓球","たつきゆう",0);
INSERT INTO tbl_word_siritori VALUES (15,2,"ラケット","らけつと",0);
INSERT INTO tbl_word_siritori VALUES (15,3,"ネット","ねつと",0);
INSERT INTO tbl_word_siritori VALUES (15,4,"卓球台","たつきゆうたい",0);
INSERT INTO tbl_word_siritori VALUES (15,5,"テーブルテニス","てーふるてにす",0);
INSERT INTO tbl_word_siritori VALUES (15,6,"スマッシュ","すまつしゆ",0);
INSERT INTO tbl_word_siritori VALUES (15,7,"ピンポン","ひんほん",0);
INSERT INTO tbl_word_siritori VALUES (15,8,"シェークハンド","しえーくはんと",1);
INSERT INTO tbl_word_siritori VALUES (15,9,"スピン","すひん",1);
INSERT INTO tbl_word_siritori VALUES (15,10,"オブストラクション","おふすとらくしよん",2);
INSERT INTO tbl_word_siritori VALUES (16,1,"野球","やきゆう",0);
INSERT INTO tbl_word_siritori VALUES (16,2,"バット","はつと",0);
INSERT INTO tbl_word_siritori VALUES (16,3,"ボール","ほーる",0);
INSERT INTO tbl_word_siritori VALUES (16,4,"ヒット","ひつと",0);
INSERT INTO tbl_word_siritori VALUES (16,5,"ホームラン","ほーむらん",1);
INSERT INTO tbl_word_siritori VALUES (16,6,"バント","はんと",1);
INSERT INTO tbl_word_siritori VALUES (16,7,"ベースボール","へーすほーる",0);
INSERT INTO tbl_word_siritori VALUES (16,8,"盗塁","とうるい",1);
INSERT INTO tbl_word_siritori VALUES (16,9,"ストライク","すとらいく",1);
INSERT INTO tbl_word_siritori VALUES (16,10,"アウト","あうと",1);
INSERT INTO tbl_word_siritori VALUES (16,11,"グローブ","くろーふ",0);
INSERT INTO tbl_word_siritori VALUES (16,12,"キャッチャー","きやつちやー",0);
INSERT INTO tbl_word_siritori VALUES (16,13,"テキサスヒット","てきさすひつと",2);
INSERT INTO tbl_word_siritori VALUES (16,14,"ノーヒットノーラン","のーひつとのーらん",2);
INSERT INTO tbl_word_siritori VALUES (17,1,"アーチェリー","あーちえりー",0);
INSERT INTO tbl_word_siritori VALUES (17,2,"弓矢","ゆみや",0);
INSERT INTO tbl_word_siritori VALUES (17,3,"的","まと",0);
INSERT INTO tbl_word_siritori VALUES (17,4,"矢羽","やはね",1);
INSERT INTO tbl_word_siritori VALUES (17,5,"リカーブ","りかーふ",1);
INSERT INTO tbl_word_siritori VALUES (17,6,"コンパウンド","こんはうんと",1);
INSERT INTO tbl_word_siritori VALUES (17,7,"ベアボウ","へあほう",1);
INSERT INTO tbl_word_siritori VALUES (17,8,"ドローレングス","とろーれんくす",2);
INSERT INTO tbl_word_siritori VALUES (17,9,"フィストメルケージ","ふいすとめるけーし",2);
INSERT INTO tbl_word_siritori VALUES (18,1,"ホッケー","ほつけー",0);
INSERT INTO tbl_word_siritori VALUES (18,2,"スティック","すていつく",1);
INSERT INTO tbl_word_siritori VALUES (18,3,"コート","こーと",0);
INSERT INTO tbl_word_siritori VALUES (18,4,"ゴール","こーる",0);
INSERT INTO tbl_word_siritori VALUES (18,5,"ボール","ほーる",0);
INSERT INTO tbl_word_siritori VALUES (18,6,"キーパー","きーはー",0);
INSERT INTO tbl_word_siritori VALUES (18,7,"シュート","しゆーと",0);
INSERT INTO tbl_word_siritori VALUES (18,8,"ペナルティスポット","へなるていすほつと",2);
INSERT INTO tbl_word_siritori VALUES (18,9,"サークル","さーくる",1);
INSERT INTO tbl_word_siritori VALUES (18,10,"パス","はす",0);
INSERT INTO tbl_word_siritori VALUES (19,1,"ゴルフ","こるふ",0);
INSERT INTO tbl_word_siritori VALUES (19,2,"クラブ","くらふ",1);
INSERT INTO tbl_word_siritori VALUES (19,3,"グリーン","くりーん",1);
INSERT INTO tbl_word_siritori VALUES (19,4,"ボール","ほーる",0);
INSERT INTO tbl_word_siritori VALUES (19,5,"カップ","かつふ",1);
INSERT INTO tbl_word_siritori VALUES (19,6,"バンカー","はんかー",1);
INSERT INTO tbl_word_siritori VALUES (19,7,"フェアウェイ","ふえあうえい",2);
INSERT INTO tbl_word_siritori VALUES (19,8,"アイアン","あいあん",1);
INSERT INTO tbl_word_siritori VALUES (19,9,"パター","はたー",1);
INSERT INTO tbl_word_siritori VALUES (19,10,"ウェッジ","うえつし",1);
INSERT INTO tbl_word_siritori VALUES (19,11,"ドライバー","とらいはー",1);
INSERT INTO tbl_word_siritori VALUES (20,1,"ハンドボール","はんとほーる",0);
INSERT INTO tbl_word_siritori VALUES (20,2,"ゴール","こーる",0);
INSERT INTO tbl_word_siritori VALUES (20,3,"ボール","ほーる",0);
INSERT INTO tbl_word_siritori VALUES (20,4,"コート","こーと",0);
INSERT INTO tbl_word_siritori VALUES (20,5,"シュート","しゆーと",0);
INSERT INTO tbl_word_siritori VALUES (20,6,"フリースロー","ふりーすろー",1);
INSERT INTO tbl_word_siritori VALUES (20,7,"パッシブプレー","はつしふふれー",2);
INSERT INTO tbl_word_siritori VALUES (20,8,"キーパー","きーはー",0);
INSERT INTO tbl_word_siritori VALUES (21,1,"スケートボード","すけーとほーと",0);
INSERT INTO tbl_word_siritori VALUES (21,2,"トリック","とりつく",1);
INSERT INTO tbl_word_siritori VALUES (21,3,"セクション","せくしよん",1);
INSERT INTO tbl_word_siritori VALUES (21,4,"ウォール","うおーる",1);
INSERT INTO tbl_word_siritori VALUES (21,5,"フリップ","ふりつふ",1);
INSERT INTO tbl_word_siritori VALUES (21,6,"プッシュ","ふつしゆ",1);
INSERT INTO tbl_word_siritori VALUES (21,7,"マニュアル","まにゆある",1);
INSERT INTO tbl_word_siritori VALUES (21,8,"インディークラブ","いんていーくらふ",2);
INSERT INTO tbl_word_siritori VALUES (22,1,"サーフィン","さーふいん",0);
INSERT INTO tbl_word_siritori VALUES (22,2,"サーフボード","さーふほーと",0);
INSERT INTO tbl_word_siritori VALUES (22,3,"テイクオフ","ていくおふ",2);
INSERT INTO tbl_word_siritori VALUES (22,4,"パドリング","はとりんく",2);
INSERT INTO tbl_word_siritori VALUES (22,5,"波","なみ",0);
INSERT INTO tbl_word_siritori VALUES (22,6,"海","うみ",0);
INSERT INTO tbl_word_siritori VALUES (22,7,"ライディング","らいていんく",1);
INSERT INTO tbl_word_siritori VALUES (23,1,"ラグビー","らくひー",0);
INSERT INTO tbl_word_siritori VALUES (23,2,"タックル","たつくる",0);
INSERT INTO tbl_word_siritori VALUES (23,3,"タッチダウン","たつちたうん",1);
INSERT INTO tbl_word_siritori VALUES (23,4,"ボール","ほーる",0);
INSERT INTO tbl_word_siritori VALUES (23,5,"ゴール","こーる",0);
INSERT INTO tbl_word_siritori VALUES (23,6,"フリーキック","ふりーきつく",1);
INSERT INTO tbl_word_siritori VALUES (23,7,"オフサイド","おふさいと",1);
INSERT INTO tbl_word_siritori VALUES (23,8,"スクラム","すくらむ",2);
INSERT INTO tbl_word_siritori VALUES (23,9,"モール","もーる",2);
INSERT INTO tbl_word_siritori VALUES (24,1,"空手","からて",0);
INSERT INTO tbl_word_siritori VALUES (24,2,"帯","おひ",0);
INSERT INTO tbl_word_siritori VALUES (24,3,"道場","とうしよう",0);
INSERT INTO tbl_word_siritori VALUES (24,4,"組み手","くみて",1);
INSERT INTO tbl_word_siritori VALUES (24,5,"正拳","せいけん",1);
INSERT INTO tbl_word_siritori VALUES (24,6,"手刀","しゆとう",1);
INSERT INTO tbl_word_siritori VALUES (24,7,"貫手","ぬきて",1);
INSERT INTO tbl_word_siritori VALUES (24,8,"外八字立ち","そとはちしたち",2);
INSERT INTO tbl_word_siritori VALUES (24,9,"揚げ受け","あけうけ",2);

# 以上