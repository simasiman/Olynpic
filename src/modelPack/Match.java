package modelPack;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.websocket.Session;

import Utility.GameSetting;
import Utility.HtmlGame;

public class Match
{
    private int matchNo = 0;

    private ArrayList<User> userList = new ArrayList<User>();
    private ArrayList<Panel> panelList = new ArrayList<Panel>();
    private ArrayList<Word> selectedWordList = new ArrayList<Word>();

    private Panel lastSelectedPanel;

    private Date startTime;
    private Date endTime;
    private Date selectedTime;

    private int playerCount = 0;
    private int playerTurn = 0;

    private boolean isStart = false;
    private boolean isFirstPick = true;
    private boolean isMiss = false;
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
        setSelectedTime(new Date());
    }

    public boolean isStart()
    {
        return isStart;
    }

    public boolean isFirstPick()
    {
        return isFirstPick;
    }

    public boolean isMiss()
    {
        return isMiss;
    }

    public boolean isFinish()
    {
        return isFinish;
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

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
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

    public Panel getLastSelectedPanel()
    {
        return lastSelectedPanel;
    }

    public void firstPick(String key, Panel panel)
    {
        isFirstPick = false;
        isMiss = false;

        lastSelectedPanel = panel;

        Word word = panel.getWordList().get(0);
        selectWord(key, word, panel);
    }

    public boolean nextPick(String key, Panel panel)
    {
        // ユーザのTimeOutフラグを解除
        User user = getUser(key);
        user.resetTimeOutCnt();

        selectedTime = new Date();

        lastSelectedPanel = panel;

        int nextWordIndex = panel.isMatchWord(nowWord, selectedWordList);
        if (nextWordIndex < 0)
        {
            // 単語がミスしている場合
            isMiss = true;
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
        selectedWordList.add(word);

        panel.setSelectedUserId(key);
        panel.setSelectedWord(word);

        isMiss = false;
        nowWord = word;
        addSelectPanel(key, panel);

        addScore(key, word);

        setPlayerTurnNext();
    }

    public void panelSelect(String key, String selectedIndex)
    {
        timeOutCheck();
        if (!isFinish() && isHisTurn(key))
        {
            if (selectedIndex != null && !selectedIndex.isEmpty())
            {
                // ゲーム継続時
                try
                {
                    int selected = Integer.parseInt(selectedIndex);
                    Panel selectedPanel = panelList.get(selected);

                    if (isPanelRange(selected) && !selectedPanel.isUsed())
                    {
                        // 使われていないパネルが選択された場合
                        if (isFirstPick())
                        {
                            // 最初のパネル選択の場合
                            firstPick(key, selectedPanel);
                        }
                        else if (nextPick(key, selectedPanel))
                        {
                            // 最初以降のパネル選択の場合
                            // パネル選択可能の場合
                        }
                        else
                        {
                            // 最初以降のパネル選択の場合
                            // パネル選択不可能の場合
                        }
                    }
                }
                catch (IndexOutOfBoundsException e)
                {
                    System.out.println("範囲外のパネルが指定されました。");
                }
                catch (NumberFormatException e)
                {
                    System.out.println("パネルの指定で無効な数値が入力されました。");
                }
            }
            if (!isEnableContinue())
            {
                finishMatch();
            }
        }
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
     * 待機状態のマッチングを作成(複数人対戦用)
     * 
     * @param playerCount
     *            プレイヤー人数
     */
    public void createMatch(int playerCount)
    {
        try
        {
            for (int i = 0; i < playerCount; i++)
            {
                this.userList.add(MatchUserList.pull(playerCount));
            }
            this.matchNo = MatchList.getNextMatchNumber();
            this.playerCount = playerCount;
            this.playerTurn = (playerCount == 1) ? 0 : new Random().nextInt(playerCount);

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

            if (user1.isTimeOut() && user2.isTimeOut())
            {
                // 両者タイムアウトの場合
                user1.setWin(User.DRAW);
                user2.setWin(User.DRAW);
            }
            else if (user1.isTimeOutTwice())
            {
                // 片方が２連続タイムアウトである場合
                user1.setWin(User.LOSE);
                user2.setWin(User.WIN);
            }
            else if (user2.isTimeOutTwice())
            {
                // 片方が２連続タイムアウトである場合
                user1.setWin(User.WIN);
                user2.setWin(User.LOSE);
            }
            else if (user1.getScore() > user1.getScore())
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

    public void matchingCheck()
    {
        if (isStart || isFinish)
        {
            return;
        }

        // 参加人数を満たした場合、ゲームスタート
        if (isCanMatchStart())
        {
            startMatch();
            sendSessionMatchingComplete();
        }
    }

    public void timeOutCheck()
    {
        if (!isStart || isFinish)
        {
            return;
        }

        if (getTimeDiff() >= GameSetting.MATCH_TIME)
        {
            User user = userList.get(playerTurn);
            user.addTimeOutCnt();

            if (user.isTimeOutTwice())
            {
                // 既にタイムアウト状態の場合
                finishMatch();
            }

            setPlayerTurnNext();

            sendSessionGameUpdate();
        }

        // 制限時間外の場合
        if (isTimeOutEnd())
        {
            finishMatch();

            sendSessionGameUpdate();
        }
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

    public void sendSessionMatchingComplete()
    {
        String ret = "<!--complete-->";

        for (User user : userList)
        {
            Session session = user.session;
            if (session != null && session.isOpen())
            {
                session.getAsyncRemote().sendText(ret);
            }
        }
    }

    public void sendSessionGameUpdate()
    {
        for (User user : userList)
        {
            Session session = user.session;
            if (session != null && session.isOpen())
            {
                String ret = HtmlGame.makeGameHtml(this, user.getKey());
                session.getAsyncRemote().sendText("<!--[selected]-->" + ret);
            }
        }
    }

    public void sendSessionGameReconnect()
    {
        for (User user : userList)
        {
            Session session = user.session;
            if (session != null && session.isOpen())
            {
                String ret = HtmlGame.makeGameHtml(this, user.getKey());
                session.getAsyncRemote().sendText("<!--[reconnect]-->" + ret);
            }
        }
    }

    public void sendSessionGameDisconnect(Session session)
    {
        for (User user : userList)
        {
            Session us = user.session;
            if (us != session && us != null && us.isOpen())
            {
                String ret = HtmlGame.makeGameHtml(this, user.getKey());
                us.getAsyncRemote().sendText("<!--[disconnect]-->" + ret);
            }
        }
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
