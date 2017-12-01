package modelPack;

import java.util.ArrayList;

public class User
{
    private String key;
    private String name;

    private int highScore;
    private int win;

    private ArrayList<Panel> selectedPanel = new ArrayList<Panel>();
    private ArrayList<String> selectedWord = new ArrayList<String>();

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

    public int getHighScore()
    {
        return highScore;
    }

    public void setHighScore(int highScore)
    {
        this.highScore = highScore;
    }

    public int getWin()
    {
        return win;
    }

    public void setWin(int win)
    {
        this.win = win;
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

    public ArrayList<String> getSelectedWord()
    {
        return selectedWord;
    }

    public void setSelectedWord(ArrayList<String> selectedWord)
    {
        this.selectedWord = selectedWord;
    }

}
