package Utility;

import java.util.Date;

import modelPack.SiritoriDao;

public class Utility
{
    public static final int INDENT_SPACE = 4;

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

    public static String appendIndent(int indentCount)
    {
        String ret = "";

        for (int i = 0; i < indentCount; i++)
        {
            for (int j = 0; j < INDENT_SPACE; j++)
            {
                ret += " ";
            }
        }

        return ret;
    }

    public static String appendLine(String line)
    {
        return line + "\r\n";
    }

    public static String appendLineIndent(int indentCount, String line)
    {
        return appendIndent(indentCount) + appendLine(line);
    }

}
