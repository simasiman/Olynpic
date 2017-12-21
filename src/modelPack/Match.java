package modelPack;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.websocket.Session;

import Utility.GameSetting;
import Utility.HtmlGame;
import Utility.Utility;

/**
 * ひとつのゲームを管理するクラスです
 */
public class Match
{
    /**
     * ゲームに参加するユーザの一覧
     */
    private ArrayList<User> userList = new ArrayList<User>();
    /**
     * ゲームで使用されるパネルの一覧
     */
    private ArrayList<Panel> panelList = new ArrayList<Panel>();
    /**
     * ゲームで使用された単語の一覧
     */
    private ArrayList<Word> selectedWordList = new ArrayList<Word>();
    /**
     * 直前に選択されたパネル
     */
    private Panel lastSelectedPanel;

    /**
     * ゲーム開始時間
     */
    private Date startTime;
    /**
     * ゲーム終了時間
     */
    private Date endTime;
    /**
     * 直前にパネルが選択された時間
     */
    private Date selectedTime;

    /**
     * ゲーム参加人数
     */
    private int playerCount = 0;
    /**
     * 現在どのユーザのターンか (userList.get(playerCount)を想定)
     */
    private int playerTurn = 0;

    /**
     * オリンピックモード
     */
    private boolean isOlympic = true;

    /**
     * ゲームが開始中であるか
     */
    private boolean isStart = false;
    /**
     * ゲームが最初のパネルをピックするフェイズかどうか
     */
    private boolean isFirstPick = true;
    /**
     * ゲームが既に終了したかどうか
     */
    private boolean isFinish = false;
    /**
     * ゲーム結果がサーバに登録されたかどうか
     */
    private boolean isUpload = false;
    /**
     * ゲームをメモリ上から破棄してよいか
     */
    private boolean isClean = false;

    /**
     * 直前の行動 (ACTION_****の定数を使用)
     */
    private int lastAction = -1;

    /**
     * 直前の行動 (正解を選択)
     */
    public static final int ACTION_CORRECT = 1;
    /**
     * 直前の行動 (不正解を選択)
     */
    public static final int ACTION_MISS = 2;
    /**
     * 直前の行動 (時間制限)
     */
    public static final int ACTION_TIMEOUT = 3;

    /**
     * 直前に選択された単語
     */
    private Word nowWord;

    /**
     * ゲームに参加するユーザ数を返します
     * 
     * @return ゲームに参加するユーザ数
     */
    public int getUserCount()
    {
        return userList.size();
    }

    /**
     * ゲームに参加するユーザ一覧を返します
     * 
     * @return ゲームに参加するユーザ一覧
     */
    public ArrayList<User> getUserList()
    {
        return this.userList;
    }

    public void addUser(User u)
    {
        userList.add(u);
    }

    /**
     * ユーザキーを元に、参加中のユーザを取得します
     * 
     * @param key
     *            ユーザキー
     * @return 該当ユーザあり:user 該当ユーザなし:null
     */
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

    /**
     * ゲームを次のユーザのターンへと遷移させます
     */
    public void setPlayerTurnNext()
    {
        playerTurn = (getPlayerTurn() + 1) % getPlayerCount();
        setSelectedTime(new Date());
    }

    public boolean isOlympic()
    {
        return isOlympic;
    }

    public void setOlympic(boolean isOlympic)
    {
        this.isOlympic = isOlympic;
    }

    public boolean isStart()
    {
        return isStart;
    }

    public boolean isFirstPick()
    {
        return isFirstPick;
    }

    public int getLastAction()
    {
        return lastAction;
    }

    /**
     * 直前の行動が正解を選択したかを返します
     * 
     * @return 直前の行動が正解：TRUE その他：FALSE
     */
    public boolean isLACorrect()
    {
        return lastAction == ACTION_CORRECT;
    }

    /**
     * 直前の行動が不正解を選択したかを返します
     * 
     * @return 直前の行動が不正解：TRUE その他：FALSE
     */
    public boolean isLAMiss()
    {
        return lastAction == ACTION_MISS;
    }

    /**
     * 直前の行動が時間制限を経過したかを返します
     * 
     * @return 直前の時間制限：TRUE その他：FALSE
     */
    public boolean isLATimeOut()
    {
        return lastAction == ACTION_MISS;
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

    public Word getNowWord()
    {
        return nowWord;
    }

    /**
     * 対象ユーザが選択したパネルを内部に保存します
     * 
     * @param key
     *            ユーザキー
     * @param panel
     *            選択パネル
     */
    public void addSelectPanel(String key, Panel panel)
    {
        getUser(key).addSelectedPanel(panel);
        panel.setUsed(true);
    }

    /**
     * 対処ユーザのミス回数をインクリメントします
     * 
     * @param key
     *            ユーザキー
     */
    public void addMissCount(String key)
    {
        getUser(key).addMiss();
    }

    /**
     * 対象ユーザの勝敗結果を取得します
     * 
     * @param key
     *            ユーザキー
     * @return 勝敗結果 (Userクラスが持つ定数と対応)
     */
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

    /**
     * ゲーム初回のパネル選択時の処理を行います
     * 
     * @param key
     *            ユーザキー
     * @param panel
     *            選択されたパネル
     */
    private void firstPick(String key, Panel panel)
    {
        isFirstPick = false;

        lastSelectedPanel = panel;

        Word word = panel.getWordList().get(0);
        selectWord(key, word, panel);
    }

    /**
     * ゲーム初回以降のパネル選択時の処理を行います
     * 
     * @param key
     *            ユーザキー
     * @param panel
     *            選択されたパネル
     * @return パネルが選択可能だったかどうか
     */
    private boolean nextPick(String key, Panel panel)
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
            lastAction = ACTION_MISS;
            addMissCount(key);
            setPlayerTurnNext();

            return false;
        }

        // 単語がマッチしている場合
        Word word = panel.getWordList().get(nextWordIndex);
        selectWord(key, word, panel);

        return true;
    }

    /**
     * パネルが選択可能だった際の単語に係る処理を行います
     * 
     * @param key
     *            ユーザキー
     * @param word
     *            選択されたワード
     * @param panel
     *            選択されたパネル
     */
    private void selectWord(String key, Word word, Panel panel)
    {
        lastAction = ACTION_CORRECT;

        selectedWordList.add(word);

        panel.setSelectedUserId(key);
        panel.setSelectedWord(word);

        nowWord = word;
        addSelectPanel(key, panel);

        setPlayerTurnNext();
    }

    /**
     * ゲーム中にパネルが選択された際の処理を統括して行います
     * 
     * @param key
     *            ユーザキー
     * @param selectedIndex
     *            選択されたパネルの索引番号
     */
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
     * 待機状態のマッチングを作成します
     * 
     * @param playerCount
     *            プレイヤー人数
     * @param isOlympic
     *            オリンピックモード
     */
    public void createMatch(int playerCount, boolean isOlympic)
    {
        try
        {
            for (int i = 0; i < playerCount; i++)
            {
                this.userList.add(MatchUserList.pull(playerCount, isOlympic));
            }
            this.playerCount = playerCount;
            this.playerTurn = (playerCount == 1) ? 0 : new Random().nextInt(playerCount);
            this.getUserList().get(playerTurn).setFirst(true);

            this.isOlympic = isOlympic;

            panelList = ((new SiritoriDao()).getRandomPanel(24, isOlympic));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }

        // マッチ管理用リストに追加
        MatchList.add(this);

        String userName = "";
        for (User user : userList)
        {
            userName += "[" + user.getKey() + " : " + user.getName() + "]";
        }
        Utility.outputLog(null, "Create Match : " + userName);
    }

    /**
     * ゲーム開始可能な状態かを判断します
     * 
     * @return ゲーム開始可能:TRUE ゲーム開始不可能:FALSE
     */
    private boolean isCanMatchStart()
    {
        return getPlayerCount() == getUserCount();
    }

    /**
     * ゲームを開始します
     */
    public void startMatch()
    {
        startTime = new Date();
        isStart = true;
        selectedTime = new Date();

        String userName = "";
        for (User user : userList)
        {
            userName += "[" + user.getKey() + " : " + user.getName() + "]";
        }
        Utility.outputLog(null, "Start Match  : " + userName);
    }

    /**
     * ゲームを終了します
     */
    public void finishMatch()
    {
        endTime = new Date();
        isFinish = true;

        setWinLose();

        if (!isUpload)
        {
            isUpload = true;

            updateUser();
            insertPlayResult();
        }

        String userName = "";
        for (User user : userList)
        {
            userName += "[" + user.getKey() + " : " + user.getName() + "]";
        }
        Utility.outputLog(null, "Finish Match : " + userName);
    }

    /**
     * 対象ユーザのターンかを判定します
     * 
     * @param key
     *            ユーザキー
     * @return 対象ユーザのターン:TRUE そうでない:FALSE
     */
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

    /**
     * 参加中のユーザに対して、勝敗結果を割り当てます
     */
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

            // 2017.12.21 リザルト画面が引き分け表示に対応していない為、引き分け＝敗北の解釈で修正
            if (user1.session == null || user1.isTimeOutTwice())
            {
                if (user2.session == null || user2.isTimeOutTwice())
                {
                    // 両者タイムアウトの場合
                    user1.setWin(User.LOSE);
                    user2.setWin(User.LOSE);
                }
                else
                {
                    // 片方がセッション喪失、あるいは２連続タイムアウトである場合
                    user1.setWin(User.LOSE);
                    user2.setWin(User.WIN);
                }

            }
            else if (user2.session == null || user2.isTimeOutTwice())
            {
                // 片方がセッション喪失、あるいは２連続タイムアウトである場合
                user1.setWin(User.WIN);
                user2.setWin(User.LOSE);
            }
            else if (user1.getScore() > user1.getScore())
            {
                // 合計点が多い方を勝利とする
                user1.setWin(User.WIN);
                user2.setWin(User.LOSE);
            }
            else if (user1.getScore() < user2.getScore())
            {
                user1.setWin(User.LOSE);
                user2.setWin(User.WIN);
            }
            else if (user1.getTotalScoreBonus() < user2.getTotalScoreBonus())
            {
                // ボーナス点が多い方を勝利とする
                user1.setWin(User.WIN);
                user2.setWin(User.LOSE);
            }
            else if (user1.getTotalScoreBonus() > user2.getTotalScoreBonus())
            {
                user1.setWin(User.LOSE);
                user2.setWin(User.WIN);
            }
            else if (user1.getScoreMultiple() < user2.getScoreMultiple())
            {
                // 点数倍率が低い方を勝利とする
                user1.setWin(User.WIN);
                user2.setWin(User.LOSE);
            }
            else if (user1.getScoreMultiple() > user2.getScoreMultiple())
            {
                user1.setWin(User.LOSE);
                user2.setWin(User.WIN);
            }
            else if (user1.isFirst())
            {
                // 先行を勝利とする
                user1.setWin(User.WIN);
                user2.setWin(User.LOSE);
            }
            else
            {
                user1.setWin(User.LOSE);
                user2.setWin(User.WIN);
            }
        }
    }

    /**
     * 現在時刻とパネル選択時間の相違秒数を求めます
     * 
     * @return 相違秒数
     */
    private int getTimeDiff()
    {
        return (int) (((new Date()).getTime() - selectedTime.getTime()) / 1000);
    }

    /**
     * タイマーによって呼び出される、マッチングを開始させる処理です
     */
    public void matchingCheck()
    {
        if (isStart || isFinish)
        {
            return;
        }

        // 参加人数を満たした場合、ゲームスタート
        if (isCanMatchStart())
        {
            sendSessionMatchingComplete();
            startMatch();
        }
    }

    /**
     * タイマーによって呼び出される、ゲーム中の時間制限を確認する処理です
     */
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

            if (user.session == null || user.isTimeOutTwice())
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

    /**
     * ユーザ全員が時間制限を超えているかを判定します
     * 
     * @return 全員が時間制限:TRUE そうでない:FALSE
     */
    private boolean isTimeOutEnd()
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

    /**
     * ユーザが持つセッションに対して、マッチング完了の情報を送信します
     * 
     * @return 送信結果
     */
    public void sendSessionMatchingComplete()
    {
        for (User user : userList)
        {
            Session session = user.session;
            if (session != null && session.isOpen())
            {
                session.getAsyncRemote().sendText("<!--complete-->");
            }
        }
    }

    /**
     * ユーザが持つセッションに対して、ゲーム更新の情報を送信します
     */
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

    /**
     * ユーザが持つセッションに対して、あるユーザがセッションを復帰した情報を送信します
     */
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

    /**
     * ユーザが持つセッションに対して、あるユーザがセッションを切断した情報を送信します
     */
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

    /**
     * パネルを選択する為の索引が有効かを判定します
     * 
     * @param index
     *            パネル選択の索引
     * @return 有効:TRUE 無効:FALSE
     */
    private boolean isPanelRange(int index)
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

    /**
     * ゲームに参加しているユーザのDBに登録している情報を、追加・ないし更新します
     * 
     * @return 処理完了:TRUE エラー発生:FALSE
     */
    private boolean updateUser()
    {
        try
        {
            SiritoriDao dao = new SiritoriDao();
            for (User user : userList)
            {
                User target = dao.selectUser(user.getKey());
                if (target == null)
                {
                    // 新規ユーザであれば登録
                    dao.insertUser(user);
                }
                else if (!target.getName().equals(user.getName()))
                {
                    // 旧来ユーザで名前が異なれば更新
                    dao.updateUser(user);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * ゲームの結果をDBに登録します
     * 
     * @return 処理完了:TRUE エラー発生:FALSE
     * 
     */
    private boolean insertPlayResult()
    {
        try
        {
            for (User user : userList)
            {
                PlayResult result = new PlayResult();

                result.setKey(user.getKey());
                result.setPlayerCount(playerCount);
                result.setPlayDate(new Date());
                result.setScore(user.getScore());
                result.setWinLose(user.getWin());

                (new SiritoriDao()).insertPlayResult(result);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
