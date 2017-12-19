package modelPack;

import java.util.Date;

public class PlayResult
{
    private String key;
    private int playerCount;
    private Date playDate;
    private int winLose;
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
