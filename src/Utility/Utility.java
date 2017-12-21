package Utility;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

public class Utility
{
    private static final int INDENT_SPACE = 4;

    /** 濁音 */
    private static final String DAKUON = "ヴがぎぐげござじずぜぞだぢづでどばびぶべぼぱぴぷぺぽ";

    /** 清音 */
    private static final String SEION = "うかきくけこさしすせそたちつてとはひふへほはひふへほ";

    /** 小文字 */
    private static final String KOMOZI = "ぁぃぅぇぉっゃゅょ";

    /** 大文字 */
    private static final String OOMOZI = "あいうえおつやゆよ";

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

    public static String appendLine(String line)
    {
        return line + "\r\n";
    }

    public static String htmlEscape(String target)
    {
        return target.trim().replace("&", "&amp;").replace("\"", "&quot;").replace("<", "&lt;").replace(">", "&gt;").replace("'", "&#39;");
    }

    public static String convertHirakana(String input)
    {
        String result = input;
        for (int i = 0; i < DAKUON.length(); i++)
        {
            String s1 = DAKUON.substring(i, i + 1);
            String s2 = SEION.substring(i, i + 1);
            result = result.replaceAll(s1, s2);
        }

        for (int i = 0; i < KOMOZI.length(); i++)
        {
            String s1 = KOMOZI.substring(i, i + 1);
            String s2 = OOMOZI.substring(i, i + 1);
            result = result.replaceAll(s1, s2);
        }

        return result;
    }

    public static void outputLog(HttpServletRequest req, String message)
    {
        if (!GameSetting.LOGGED)
        {
            return;
        }

        String proxyIp = "";
        if (req != null)
        {
            proxyIp = "(" + req.getRemoteAddr() + ") ";
        }

        System.out.println(message + "\t" + proxyIp + new Date());
    }

}
