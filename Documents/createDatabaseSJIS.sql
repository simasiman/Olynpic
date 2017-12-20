############################################
#       ����Ƃ�p�f�[�^�x�[�X�쐬�p�b�`         #
############################################

# �f�[�^�x�[�X�̍폜�ƍ쐬(�č쐬)
DROP DATABASE IF EXISTS PaneTori;
CREATE DATABASE PaneTori;

# �f�[�^�x�[�X�̑I��
USE PaneTori;

# ���Z���P��e�[�u���̍쐬
CREATE TABLE tbl_word_base(
    ath_id  INT  ,
    name    TEXT ,
    picture TEXT
);

# ����Ƃ�P��e�[�u���̍쐬
CREATE TABLE tbl_word_siritori(
    ath_id    INT  ,
    word_id   INT  ,
    word_disp TEXT ,
    word_read TEXT ,
    level     INT
);

# ���[�U�e�[�u���̍쐬
CREATE TABLE tbl_user(
    user_key  TEXT ,
    name      TEXT
);

# �}�b�`���O�e�[�u���̍쐬
CREATE TABLE tbl_play_result(
    id          INT  ,
    user_key    TEXT ,
    playdate    DATE ,
    winlose     INT  ,
    score       INT  ,
    playerCount INT
);

# ���Z���P��e�[�u���Ƀf�[�^��}��
LOAD DATA LOCAL INFILE '.\\DB\\tbl_word_base.csv' 
INTO TABLE tbl_word_base FIELDS TERMINATED BY ',' ENCLOSED BY '"';

# ����Ƃ�P��e�[�u���Ƀf�[�^��}��
LOAD DATA LOCAL INFILE '.\\DB\\tbl_word_siritori.csv' 
INTO TABLE tbl_word_siritori FIELDS TERMINATED BY ',' ENCLOSED BY '"';

# �ȏ�