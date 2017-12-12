package Utility;

import java.util.Date;

public class Utility
{
    public static final int INDENT_SPACE = 4;

    public static String getDefaultKey()
    {
        return (String.valueOf(new Date().getTime()));
    }

    public static String getDefaultName()
    {
        return "名もなきアスリート";
    }

    public static String appendLineIndent(int indentCount, String line)
    {
        return appendIndent(indentCount) + appendLine(line);
    }

    private static String appendIndent(int indentCount)
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

    private static String appendLine(String line)
    {
        return line + "\r\n";
    }

}
