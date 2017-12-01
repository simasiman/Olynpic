package modelPack;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Match
{
    private int matchNo = 0;

    private ArrayList<User> userList = new ArrayList<User>();

    private ArrayList<Panel> panelList = new ArrayList<Panel>();
    private ArrayList<Integer> selectedPanelList = new ArrayList<Integer>();

    private Date startTime;
    private Date endTime;

    private int playerCount = 0;
    private int playerTurn = 0;

    private boolean isStart = false;
    private boolean isFinish = false;

    private String nowWord;
    private String firstWord;
    private String lastWord;

    private int winUser = 0;

    private int score;

    public int getMatchNo()
    {
        return matchNo;
    }

    public void setMatchNo(int matchNo)
    {
        this.matchNo = matchNo;
    }

    public int getUserCount()
    {
        return userList.size();
    }

    public void addUser(User u)
    {
        userList.add(u);
    }

    public ArrayList<User> getUserList()
    {
        return userList;
    }

    public ArrayList<Panel> getPanelList()
    {
        return panelList;
    }

    public void setPanelList(ArrayList<Panel> panelList)
    {
        this.panelList = panelList;
    }

    public void createPanelList()
    {
        this.panelList = new ArrayList<Panel>();
    }

    public ArrayList<Integer> getSelectedPanelList()
    {
        return selectedPanelList;
    }

    public void setSelectedPanelList(ArrayList<Integer> selectedPanelList)
    {
        this.selectedPanelList = selectedPanelList;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public int getPlayerCount()
    {
        return playerCount;
    }

    public void setPlayerCount(int playerCount)
    {
        this.playerCount = playerCount;
    }

    public int getPlayerTurn()
    {
        return playerTurn;
    }

    public void setPlayerTurn(int playerTurn)
    {
        this.playerTurn = playerTurn;
    }

    public boolean isStart()
    {
        return isStart;
    }

    public void setStart(boolean isStart)
    {
        this.isStart = isStart;
    }

    public boolean isFinish()
    {
        return isFinish;
    }

    public void setFinish(boolean isFinish)
    {
        this.isFinish = isFinish;
    }

    public void setWinUser(int winUser)
    {
        this.winUser = winUser;
    }

    public int getWinUser()
    {
        return winUser;
    }

    public String getFirstWord()
    {
        return firstWord;
    }

    public void setFirstWord(String firstWord)
    {
        this.firstWord = firstWord;
    }

    public String getLastWord()
    {
        return lastWord;
    }

    public void setLastWord(String lastWord)
    {
        this.lastWord = lastWord;
    }

    public String getNowWord()
    {
        return nowWord;
    }

    public void setNowWord(String nowWord)
    {
        this.nowWord = nowWord;
    }

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }

    /**
     * マッチ最初に使用可能な語句を設定します
     */
    public void setStartWord()
    {
        int listCount = panelList.size();

        ArrayList<Word> wordList = panelList.get(new Random().nextInt(listCount)).getWordList();

        Word word = wordList.get(0);

        this.firstWord = word.getWordHead();
        this.lastWord = word.getWordTail();
    }

    /**
     * 現在マッチで選択されている単語に対して、選択可能なパネルが存在するかを判断します
     * 
     * @return マッチ継続の可否
     */
    public boolean isEnableContinue()
    {
        for (Panel p : panelList)
        {
            if (!p.isUsed() && p.isMatchWord(firstWord, lastWord) >= 0)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * 待機状態のマッチングを作成(一人対戦用)
     * 
     * @param u
     *            ユーザ
     */
    public void createMatch1(User u)
    {
        addUser(u);
        setStart(true);
        setStartTime(new Date());
        setMatchNo(MatchList.getNextMatchNumber());
        setPlayerCount(1);
        setPlayerTurn(0);

        try
        {
            setPanelList((new SiritoriDao()).getRandomPanel(24));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        setStartWord();
    }

    /**
     * 待機状態のマッチングを作成(複数人対戦用)
     * 
     * @param u
     *            ユーザ
     * @param playerCount
     *            プレイヤー人数
     * @return マッチング管理用インスタンス
     */
    public void createMatchMulti(User u, int playerCount)
    {
        addUser(u);
        // setStartTime(new Date());
        setMatchNo(MatchList.getNextMatchNumber());
        setPlayerCount(playerCount);
        // 先行後攻をランダム付与
        setPlayerTurn(new Random().nextInt(playerCount));

        try
        {
            setPanelList((new SiritoriDao()).getRandomPanel(24));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        setStartWord();
    }

    /**
     * マッチ終了時の各処理を行います
     */
    public void finishMatch()
    {
        endTime = new Date();
        isFinish = true;
    }

    public boolean isHisTurn(String key)
    {
        int index = -1;
        for (int i = 0; i < userList.size(); i++)
        {
            String keyMatch = userList.get(i).getKey();
            if (keyMatch.equals(key))
            {
                index = i;
                break;
            }
        }

        return index == playerTurn;
    }

}
