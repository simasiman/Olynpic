package modelPack;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import Utility.GameSetting;

public class Match
{
    private int matchNo = 0;

    private ArrayList<User> userList = new ArrayList<User>();

    private ArrayList<Panel> panelList = new ArrayList<Panel>();
    private ArrayList<Integer> selectedPanelList = new ArrayList<Integer>();
    private ArrayList<Word> selectedWordList = new ArrayList<Word>();

    private Date startTime;
    private Date endTime;
    private Date selectedTime;

    private int playerCount = 0;
    private int playerTurn = 0;

    private boolean isStart = false;
    private boolean isFirstPick = true;
    private boolean isFinish = false;
    private boolean isClean = false;

    private Word nowWord;

    private int winUser = 0;
    private int score = 0;

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

    public ArrayList<User> getUserList()
    {
        return this.userList;
    }

    public void addUser(User u)
    {
        userList.add(u);
    }

    public User getUser(String key)
    {
        for (User user : userList)
        {
            if (user.getKey().equals(key))
            {
                return user;
            }
        }

        return null;
    }

    public ArrayList<Panel> getPanelList()
    {
        return panelList;
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

    public void setPlayerTurnNext()
    {
        setPlayerTurn((getPlayerTurn() + 1) % getPlayerCount());
    }

    public boolean isStart()
    {
        return isStart;
    }

    public void setStart(boolean isStart)
    {
        this.isStart = isStart;
    }

    public boolean isFirstPick()
    {
        return isFirstPick;
    }

    public void setFirstPick(boolean isFirstPick)
    {
        this.isFirstPick = isFirstPick;
    }

    public void firstPick(String key, Panel panel)
    {
        isFirstPick = false;

        Word word = panel.getWordList().get(0);
        selectWord(key, word, panel);
    }

    public boolean nextPick(String key, Panel panel)
    {
        // ユーザのTimeOutフラグを解除
        for (User user : userList)
        {
            if (user.getKey().equals(key))
            {
                user.setTimeOut(false);
            }
        }

        selectedTime = new Date();

        int nextWordIndex = panel.isMatchWord(nowWord, selectedWordList);
        if (nextWordIndex < 0)
        {
            // 単語がミスしている場合
            addMissCount(key);
            setPlayerTurnNext();

            return false;
        }

        // 単語がマッチしている場合
        Word word = panel.getWordList().get(nextWordIndex);
        selectWord(key, word, panel);

        return true;
    }

    private void selectWord(String key, Word word, Panel panel)
    {
        // 単語がマッチしている場合
        selectedWordList.add(word);

        panel.setSelectedUserId(key);
        panel.setSelectedWord(word);

        setNowWord(word);
        addSelectPanel(key, panel);

        addScore(key, word);

        setPlayerTurnNext();
    }

    public boolean isFinish()
    {
        return isFinish;
    }

    public void setFinish(boolean isFinish)
    {
        this.isFinish = isFinish;
    }

    public boolean isClean()
    {
        return isClean;
    }

    public void setClean(boolean isClean)
    {
        this.isClean = isClean;
    }

    public void setWinUser(int winUser)
    {
        this.winUser = winUser;
    }

    public int getWinUser()
    {
        return winUser;
    }

    public Word getNowWord()
    {
        return nowWord;
    }

    public void setNowWord(Word nowWord)
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
     * 現在マッチで選択されている単語に対して、選択可能なパネルが存在するかを判断します
     * 
     * @return マッチ継続の可否
     */
    public boolean isEnableContinue()
    {
        if (isFirstPick)
        {
            return true;
        }

        for (Panel p : panelList)
        {
            if (!p.isUsed() && p.isMatchWord(nowWord, selectedWordList) >= 0)
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
        this.userList.add(u);
        this.matchNo = MatchList.getNextMatchNumber();
        this.playerCount = 1;
        this.playerTurn = 0;

        try
        {
            panelList = ((new SiritoriDao()).getRandomPanel(24));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }

        // マッチ管理用リストに追加
        MatchList.add(this);
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
    public void createMatch(User u, int playerCount)
    {
        this.userList.add(u);
        this.matchNo = MatchList.getNextMatchNumber();
        this.playerCount = playerCount;

        this.playerTurn = (playerCount == 1) ? 0 : new Random().nextInt(playerCount - 1);

        try
        {
            panelList = ((new SiritoriDao()).getRandomPanel(24));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }

        // マッチ管理用リストに追加
        MatchList.add(this);
    }

    public boolean isCanMatchStart()
    {
        return getPlayerCount() == getUserCount();
    }

    public void startMatch()
    {
        startTime = new Date();
        isStart = true;

        selectedTime = new Date();
    }

    /**
     * マッチ終了時の各処理を行います
     */
    public void finishMatch()
    {
        endTime = new Date();
        isFinish = true;

        setWinLose();
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

    public void addSelectPanel(String key, Panel panel)
    {
        getUser(key).addSelectedPanel(panel);
        panel.setUsed(true);
    }

    public void addMissCount(String key)
    {
        getUser(key).addMiss();
    }

    public void addScore(String key, Word word)
    {
        getUser(key).addScore(word.getScore());
    }

    public int getUserWin(String key)
    {
        return getUser(key).getWin();
    }

    public ArrayList<Word> getSelectedWordList()
    {
        return selectedWordList;
    }

    public Date getSelectedTime()
    {
        return selectedTime;
    }

    public void setSelectedTime(Date selectedTime)
    {
        this.selectedTime = selectedTime;
    }

    // private method
    private void setWinLose()
    {
        if (userList.size() == 1)
        {
            userList.get(0).setWin(User.WIN);
        }
        else
        {
            // とりあえず二人用の対戦結果確認だけ判定
            User user1 = userList.get(0);
            User user2 = userList.get(1);

            if (user1.getScore() > user1.getScore())
            {
                user1.setWin(User.WIN);
                user2.setWin(User.LOSE);
            }
            else if (user1.getScore() < user2.getScore())
            {
                user1.setWin(User.LOSE);
                user2.setWin(User.WIN);
            }
            else
            {
                user1.setWin(User.DRAW);
                user2.setWin(User.DRAW);
            }
        }
    }

    public int getTimeDiff()
    {
        return (int) (((new Date()).getTime() - selectedTime.getTime()) / 1000);
    }

    public boolean timeOutCheck()
    {
        if (!isStart || isFinish)
        {
            return false;
        }

        // 制限時間外の場合
        if (isTimeOutEnd())
        {
            finishMatch();
        }

        User user = userList.get(playerTurn);

        if (getTimeDiff() >= GameSetting.MATCH_TIME)
        {
            user.setTimeOut(true);
            setPlayerTurnNext();
            setSelectedTime(new Date());

            return true;
        }

        return false;
    }

    public boolean isTimeOutEnd()
    {
        for (User user : userList)
        {
            if (!user.isTimeOut())
            {
                return false;
            }
        }

        return true;
    }

    public boolean isPanelRange(int index)
    {
        try
        {
            panelList.get(index);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

}
