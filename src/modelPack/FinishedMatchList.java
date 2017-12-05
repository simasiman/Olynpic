package modelPack;

import java.util.ArrayList;

public class FinishedMatchList
{
    private static ArrayList<Match> matchList = new ArrayList<Match>();

    public static void add(Match m)
    {
        matchList.add(m);
    }

    public static void clean()
    {
        for (Match match : matchList)
        {
            boolean isClean = true;

            for (User user : match.getUserList())
            {
                if (!user.isResultWatch())
                {
                    isClean = false;
                }
            }

            if (isClean)
            {
                matchList.remove(match);
            }
        }
    }

    public static Match getMatch(String key)
    {
        for (Match match : matchList)
        {
            User user = match.getUser(key);

            if (user == null)
            {
                continue;
            }

            if (user.isResultWatch())
            {
                continue;
            }

            user.setResultWatch(true);
            return match;
        }

        return null;
    }

    public static ArrayList<Match> getMatchList()
    {
        return matchList;
    }
}
