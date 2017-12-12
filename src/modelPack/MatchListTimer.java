package modelPack;

import java.util.TimerTask;

public class MatchListTimer extends TimerTask
{
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
