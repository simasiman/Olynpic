package modelPack;

import java.util.ArrayList;

public class FinishedMatchList
{
    private static ArrayList<Match> matchList = new ArrayList<Match>();

    public static void add(Match m)
    {
        matchList.add(m);
    }

    public static Match getMatch(String key)
    {
        // 探査前にマッチングリストのクリーンを行う
        clean();

        for (Match match : matchList)
        {
            User user = match.getUser(key);
            if (user != null && !user.isResultWatch())
            {
                return match;
            }
        }

        return null;
    }

    public static ArrayList<Match> getMatchList()
    {
        return matchList;
    }

    private static void clean()
    {
        for (Match match : matchList)
        {
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

}
