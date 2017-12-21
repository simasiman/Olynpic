package modelPack;

import java.util.ArrayList;
import java.util.Timer;

import javax.websocket.Session;

import Utility.GameSetting;

/**
 * サーバ単位におけるゲームのすべてを管理するクラスです
 */
public class MatchList
{
    /**
     * マッチング管理用リスト
     */
    private static ArrayList<Match> matchList;

    /**
     * ゲーム管理用リストの初回ロード時に実行される処理
     */
    static
    {
        matchList = new ArrayList<Match>();

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new MatchListTimer(), 1000, GameSetting.SERVER_TIMER_INTERVAL);
    }

    /**
     * ゲーム管理用リストに対して、削除可能なゲームを削除します (※タイミングによってExceptionが発生する為、注意)
     */
    public static void clean()
    {
        for (Match match : matchList)
        {
            if (!match.isFinish())
            {
                continue;
            }

            match.setClean(true);

            for (User user : match.getUserList())
            {
                if (!user.isResultWatch())
                {
                    // 未閲覧のユーザがいる限り削除を行わない
                    match.setClean(false);
                    break;
                }
            }
        }

        for (int i = 0; i < matchList.size(); i++)
        {
            Match match = matchList.get(i);
            if (match.isClean())
            {
                // matchList.remove(match);
            }
        }
    }

    /**
     * 各ゲームに対して、タイマーによって定期的に実行する処理をまとめて行います
     */
    public static void CheckTimer()
    {
        // (2人用)オリンピックモードのマッチング
        while (MatchUserList.isMatchable(2, true))
        {
            new Match().createMatch(2, true);
        }

        // (2人用)非オリンピックモードのマッチング
        while (MatchUserList.isMatchable(2, false))
        {
            new Match().createMatch(2, false);
        }

        for (Match match : matchList)
        {
            match.timeOutCheck();
            match.matchingCheck();
        }
    }

    public static ArrayList<Match> getMatchList()
    {
        return matchList;
    }

    /**
     * ユーザキーを元に、そのユーザが参加していて終了していないゲームを取得します
     * 
     * @param key
     *            ユーザキー
     * @return 該当あり:match 該当なし:null
     */
    public static Match getMatch(String key)
    {
        // 探査前にマッチングリストのクリーンを行う
        clean();

        for (Match match : matchList)
        {
            if (!match.isFinish() && match.getUser(key) != null)
            {
                return match;
            }
        }

        return null;
    }

    /**
     * ユーザキーを元に、そのユーザが参加していて終了しているゲームを取得します
     * 
     * @param key
     *            ユーザキー
     * @return 該当あり:match 該当なし:null
     */
    public static Match getMatchFinished(String key)
    {
        // 探査前にマッチングリストのクリーンを行う
        clean();

        for (Match match : matchList)
        {
            User user = match.getUser(key);
            if (match.isFinish() && user != null && !user.isResultWatch())
            {
                return match;
            }
        }

        return null;
    }

    /**
     * セッションを元に、そのユーザが参加していて終了しているゲームを取得します
     * 
     * @param session
     *            セッション
     * @return 該当あり:match 該当なし:null
     */
    public static Match getMatch(Session session)
    {
        // 探査前にマッチングリストのクリーンを行う
        clean();

        for (Match match : matchList)
        {
            for (User user : match.getUserList())
            {
                if (user.session == session)
                {
                    return match;
                }
            }
        }

        return null;
    }

    public static void add(Match m)
    {
        matchList.add(m);
    }

    public static void remove(Match match)
    {
        matchList.remove(match);
    }

    /**
     * 対象ユーザを特定し、セッション情報を付与します
     * 
     * @param key
     *            ユーザキー
     * @param session
     *            セッション
     */
    public static void setUserSession(String key, Session session)
    {
        for (Match match : matchList)
        {
            User user = match.getUser(key);
            if (user != null)
            {
                user.session = session;
            }
        }
    }

    /**
     * 対象ユーザを特定し、セッション情報を削除します
     * 
     * @param session
     *            セッション
     */
    public static void setUserSessionNull(Session session)
    {
        for (Match match : matchList)
        {
            for (User user : match.getUserList())
            {
                if (user.session == session)
                {
                    user.session = null;
                }
            }
        }
    }
}
