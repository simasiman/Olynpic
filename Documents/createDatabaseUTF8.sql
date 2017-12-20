############################################
#       しりとり用データベース作成パッチ         #
############################################

# データベースの削除と作成(再作成)
DROP DATABASE IF EXISTS PaneTori;
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
LOAD DATA LOCAL INFILE 'DB\tbl_word_base.csv' 
INTO TABLE tbl_word_base FIELDS TERMINATED BY ',' ENCLOSED BY '"';

# しりとり単語テーブルにデータを挿入
LOAD DATA LOCAL INFILE 'DB\tbl_word_siritori.csv' 
INTO TABLE tbl_word_siritori FIELDS TERMINATED BY ',' ENCLOSED BY '"';

# 以上