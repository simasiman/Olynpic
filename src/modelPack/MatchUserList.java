package modelPack;

import java.util.ArrayList;

import javax.websocket.Session;

/**
 * マッチング希望のユーザを一時的に管理するリストです
 */
public class MatchUserList
{
    /**
     * ユーザ管理用リスト
     */
    private static ArrayList<User> userList = new ArrayList<User>();

    public static void add(User user)
    {
        userList.add(user);
    }

    /**
     * 指定された条件を満たすマッチングを作成可能かを判定します。
     * 
     * @param playerCount
     *            希望プレイヤー人数
     * @param isOlympic
     *            希望モード(オリンピックモード)
     * @return マッチング可能:TRUE 不可能:FALSE
     */
    public static boolean isMatchable(int playerCount, boolean isOlympic)
    {
        int count = 0;

        for (User user : userList)
        {
            if (isAvailableStatus(user, playerCount, isOlympic))
            {
                count++;
            }
        }

        return count >= playerCount;
    }

    /**
     * 該当条件を満たすユーザを抽出し、ユーザ管理リストから削除します
     * 
     * @param playerCount
     *            希望プレイヤー人数
     * @param isOlympic
     *            希望モード(オリンピックモード)
     * @return 該当あり:user 該当なし:null
     */
    public static User pull(int playerCount, boolean isOlympic)
    {
        for (User user : userList)
        {
            if (isAvailableStatus(user, playerCount, isOlympic))
            {
                userList.remove(user);
                return user;
            }
        }

        return null;
    }

    /**
     * マッチング条件として判定が必要な処理を一律して行います
     * 
     * @param user
     *            対象ユーザ
     * @param playerCount
     *            希望プレイヤー人数
     * @param isOlympic
     *            希望モード(オリンピックモード)
     * @return 条件を満たす:TRUE 満たさない:FALSE
     */
    private static boolean isAvailableStatus(User user, int playerCount, boolean isOlympic)
    {
        return user.getWishPlayerCount() == playerCount && user.isOlympic() == isOlympic;
    }

    /**
     * ユーザキーを元に、該当するユーザを取得します
     * 
     * @param key
     *            ユーザキー
     * @return 該当あり:user 該当なし:null
     */
    public static User get(String key)
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

    /**
     * ユーザキーを元に、該当するユーザを削除します
     * 
     * @param key
     *            ユーザキー
     */
    public static void remove(String key)
    {
        for (User user : userList)
        {
            if (user.getKey().equals(key))
            {
                userList.remove(user);
            }
        }
    }

    /**
     * セッション情報を元に、該当するユーザを取得します
     * 
     * @param session
     *            セッション
     * @return 該当あり:user 該当なし:null
     */
    public static User get(Session session)
    {
        for (User user : userList)
        {
            if (user.session == session)
            {
                return user;
            }
        }

        return null;
    }

    /**
     * セッション情報を元に、該当するユーザを削除します
     * 
     * @param session
     *            セッション
     */
    public static void remove(Session session)
    {
        for (User user : userList)
        {
            if (user.session == session)
            {
                userList.remove(user);
                return;
            }
        }
    }

    /**
     * ユーザキーを元に、該当するユーザを探索し、セッション情報を付与します
     * 
     * @param key
     *            ユーザキー
     * @param session
     *            付与するセッション情報
     */
    public static void setUserSession(String key, Session session)
    {
        User user = get(key);
        user.session = session;
    }
}
