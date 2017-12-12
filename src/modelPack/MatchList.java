package modelPack;

import java.util.ArrayList;
import java.util.Timer;

public class MatchList
{
    private static ArrayList<Match> matchList;

    static
    {
        matchList = new ArrayList<Match>();

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new MatchListTimer(), 1000, 1000);
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
            match.timeOutCheck();
        }
    }

    public static ArrayList<Match> getMatchList()
    {
        return matchList;
    }

    public static int getNextMatchNumber()
    {
        int ret = 0;

        for (Match match : matchList)
        {
            if (ret <= match.getMatchNo())
            {
                ret = match.getMatchNo();
            }
        }

        return ret + 1;
    }

    public static void add(Match m)
    {
        matchList.add(m);
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

    public static void remove(Match match)
    {
        matchList.remove(match);
    }

}
