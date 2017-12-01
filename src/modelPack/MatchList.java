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

    public static void addMatchList(Match m)
    {
        matchList.add(m);
    }

    public static Match getMatching()
    {
        for (Match match : matchList)
        {
            if (!match.isStart() && match.getPlayerCount() != 1)
            {
                return match;
            }
        }

        return null;
    }
}
