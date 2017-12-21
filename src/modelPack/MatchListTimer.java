package modelPack;

import java.util.TimerTask;

/**
 * サーバが管理するゲームのすべてに対して、定期的に実行する処理を行う為のクラスです
 */
public class MatchListTimer extends TimerTask
{
    /**
     * タイマーによる処理を開始します
     */
    public void run()
    {
        try
        {
            MatchList.CheckTimer();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
