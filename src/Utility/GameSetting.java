package Utility;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;

public class GameSetting
{
    // ゲームの各設定の固定値一覧

    /**
     * 各デバッグ用表示を切り替える
     */
    public static final boolean DEBUG = false;

    /**
     * ログ出力の切り替え
     */
    public static final boolean LOGGED = true;

    /**
     * ゲームにおける１ターンの制限秒数
     */
    public static final int MATCH_TIME = 20;

    /**
     * マッチング監視用タイマーの更新間隔
     */
    public static final int SERVER_TIMER_INTERVAL = 500;

    /**
     * リザルト画面における、ランキングで表示する件数
     */
    public static final int RANKING_COUNT = 3;

    // サーバ接続情報一覧
    public static String SERVER_ADDRESS = null;
    public static final String DB_DATABASE = "PaneTori";
    public static final String DB_USER = "Mulder";
    public static final String DB_PASS = "TrustNo1";
    public static final String DB_ENCOFING = "UTF-8";

    // プロジェクト名
    public static String PROJECT_NAME = "PaneTori";

    static
    {
        // サーバのIPアドレスを自動取得
        try
        {
            for (NetworkInterface n : Collections.list(NetworkInterface.getNetworkInterfaces()))
            {
                for (InetAddress addr : Collections.list(n.getInetAddresses()))
                {
                    if (addr instanceof Inet4Address && !addr.isLoopbackAddress())
                    {
                        SERVER_ADDRESS = addr.getHostAddress();
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("※サーバのIPアドレスの設定に失敗しました。サーバのアドレスは[localhost]になります。");

            SERVER_ADDRESS = "localhost";
        }
    }
}
