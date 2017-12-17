package Utility;

import java.net.InetAddress;

public class GameSetting
{
    // ゲームの各設定の固定値一覧

    /**
     * ゲームにおける１ターンの制限秒数
     */
    public static final int MATCH_TIME = 20;

    /**
     * マッチング監視用タイマーの更新間隔
     */
    public static final int SERVER_TIMER_INTERVAL = 1000;

    // サーバ接続情報一覧
    public static String SERVER_ADDRESS = "localhost";
    public static final String DB_DATABASE = "Olynpic";
    public static final String DB_USER = "Mulder";
    public static final String DB_PASS = "TrustNo1";
    public static final String DB_ENCOFING = "UTF-8";

    static
    {
        try
        {
            SERVER_ADDRESS = InetAddress.getLocalHost().getHostAddress();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("※サーバのIPアドレスの設定に失敗しました。サーバのアドレスは[localhost]になります。");
        }
    }
}
