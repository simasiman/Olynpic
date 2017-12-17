package modelPack;

import java.util.ArrayList;

import javax.websocket.Session;

public class MatchUserList
{
    private static ArrayList<User> userList = new ArrayList<User>();

    public static void add(User user)
    {
        userList.add(user);
    }

    public static boolean isMatchable(int playerCount)
    {
        int count = 0;

        for (User user : userList)
        {
            if (user.getWishPlayerCount() == playerCount)
            {
                count++;
            }
        }

        return count >= playerCount;
    }

    public static User pull(int playerCount)
    {
        for (User user : userList)
        {
            if (user.getWishPlayerCount() == playerCount)
            {
                userList.remove(user);
                return user;
            }
        }

        return null;
    }

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

    public static void setUserSession(String key, Session session)
    {
        User user = get(key);
        user.session = session;
    }
}
