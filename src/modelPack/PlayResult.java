package modelPack;

import java.util.Date;

/**
 * ゲームプレイ後の結果を収める為のクラスです
 */
public class PlayResult
{
    /**
     * ユーザキー
     */
    private String key;
    /**
     * ゲーム参加人数
     */
    private int playerCount;
    /**
     * プレイ日
     */
    private Date playDate;
    /**
     * 勝敗結果 (Userクラスが持つ定数を使用)
     */
    private int winLose;
    /**
     * 取得点数
     */
    private int score;

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public int getPlayerCount()
    {
        return playerCount;
    }

    public void setPlayerCount(int playerCount)
    {
        this.playerCount = playerCount;
    }

    public Date getPlayDate()
    {
        return playDate;
    }

    public void setPlayDate(Date playDate)
    {
        this.playDate = playDate;
    }

    public int getWinLose()
    {
        return winLose;
    }

    public void setWinLose(int winLose)
    {
        this.winLose = winLose;
    }

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }
}
