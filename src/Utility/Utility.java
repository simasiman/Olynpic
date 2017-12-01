package Utility;

import java.util.Date;

import modelPack.SiritoriDao;

public class Utility
{
    public static String getKey()
    {
        return (String.valueOf(new Date().getTime()));
    }

    public static String getName(String key)
    {
        try
        {
            return (new SiritoriDao()).selectUserName(key);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

}
