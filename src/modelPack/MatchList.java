package modelPack;

import java.util.ArrayList;

public class MatchList
{
    private static ArrayList<Match> matchList;

    static
    {
        matchList = new ArrayList<Match>();
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
            if (ret < match.getMatchNo())
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

    public static Match getMatchWaiting(String key)
    {
        for (Match match : matchList)
        {
            if (!match.isStart() && match.getUser(key) == null && match.getPlayerCount() != 1)
            {
                return match;
            }
        }

        return null;
    }

    public static Match getMatch(String key)
    {
        Match ret = null;

        for (Match match : matchList)
        {
            if (!match.isFinish() && match.getUser(key) != null)
            {
                ret = match;
                break;
            }
        }

        return ret;
    }

    public static void remove(Match match)
    {
        matchList.remove(match);
    }

    public static Match getAndRemoveFinishedMatch(String key)
    {
        Match ret = null;

        for (Match match : matchList)
        {
            if (match.isFinish() && match.getUser(key) != null)
            {
                ret = match;
                matchList.remove(match);

                break;
            }
        }

        return ret;
    }
}
