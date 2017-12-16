package modelPack;

import java.util.ArrayList;
import java.util.Timer;

import Utility.GameSetting;

public class MatchList
{
    private static ArrayList<Match> matchList;
    private static int matchNumber = 0;

    static
    {
        matchList = new ArrayList<Match>();

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new MatchListTimer(), 1000, GameSetting.SERVER_TIMER_INTERVAL);
    }

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
                matchList.remove(match);
            }
        }
    }

    public static void CheckTimer()
    {
        for (Match match : matchList)
        {
            match.matchingCheck();
            match.timeOutCheck();
        }
    }

    public static ArrayList<Match> getMatchList()
    {
        return matchList;
    }

    public static int getNextMatchNumber()
    {
        return ++matchNumber;
    }

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

    public static Match getMatchWaiting(String key)
    {
        // 探査前にマッチングリストのクリーンを行う
        clean();

        for (Match match : matchList)
        {
            if (!match.isStart() && !match.isFinish() && match.getUser(key) == null && match.getPlayerCount() != 1)
            {
                return match;
            }
        }

        return null;
    }

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

    public static void add(Match m)
    {
        matchList.add(m);
    }

    public static void remove(Match match)
    {
        matchList.remove(match);

        // 開始されていないマッチングが削除される場合、マッチング中のユーザに対してリダイレクトを行わせる
        if (!match.isStart())
        {
            match.sendUserSessionMatchingDestruct();
        }
    }

}
