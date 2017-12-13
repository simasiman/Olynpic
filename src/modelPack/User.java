package modelPack;

import java.util.ArrayList;

public class User
{
    private String key;
    private String name;

    private int score;
    private int miss;
    private int win;

    private int timeOutCnt = 0;
    private boolean isResultWatch = false;

    private ArrayList<Panel> selectedPanel = new ArrayList<Panel>();

    public static final int NONE = 0;
    public static final int DRAW = -1;
    public static final int WIN = 1;
    public static final int LOSE = 2;

    public User()
    {
        key = "";
        name = "";
        score = 0;
        miss = 0;
        win = -1;
        isResultWatch = false;
        isResultWatch = false;
    }

    public User(String key, String name)
    {
        this.key = key;
        this.name = name;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }

    public void addScore(int score)
    {
        this.score += score;
    }

    public int getMiss()
    {
        return miss;
    }

    public void setMiss(int miss)
    {
        this.miss = miss;
    }

    public void addMiss()
    {
        this.miss++;
    }

    public int getWin()
    {
        return win;
    }

    public void setWin(int win)
    {
        this.win = win;
    }

    public boolean isResultWatch()
    {
        return isResultWatch;
    }

    public void setResultWatch(boolean isResultWatch)
    {
        this.isResultWatch = isResultWatch;
    }

    public boolean isTimeOut()
    {
        return timeOutCnt >= 1;
    }

    public boolean isTimeOutTwice()
    {
        return timeOutCnt >= 2;
    }

    public void resetTimeOutCnt()
    {
        this.timeOutCnt = 0;
    }

    public void addTimeOutCnt()
    {
        timeOutCnt++;
    }

    public ArrayList<Panel> getSelectedPanel()
    {
        return selectedPanel;
    }

    public void setSelectedPanel(ArrayList<Panel> selectedPanel)
    {
        this.selectedPanel = selectedPanel;
    }

    public void addSelectedPanel(Panel panel)
    {
        selectedPanel.add(panel);
    }

}
