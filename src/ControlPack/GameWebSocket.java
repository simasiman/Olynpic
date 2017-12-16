package ControlPack;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import Utility.HtmlGame;
import modelPack.Match;
import modelPack.MatchList;
import modelPack.User;

@ServerEndpoint(value = "/game")
public class GameWebSocket
{
    private static Set<Session> ses = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session)
    {
        System.out.println("onOpen : " + session);
        ses.add(session);
    }

    @OnMessage
    public void onMessage(String text, Session session)
    {
        String[] receive = text.split(",");
        String key = receive[0];

        Match match = MatchList.getMatch(key);

        if (!match.isStart())
        {
            return;
        }

        if (receive.length == 1)
        {
            // 初回接続時
            User user = match.getUser(key);
            user.session = session;

            return;
        }

        String selected = receive[1];

        GameServlet.panelSelect(selected, match, key);

        for (User user : match.getUserList())
        {
            if (ses.contains(user.session))
            {
                String ret = HtmlGame.makeGameHtml(match, user.getKey());
                user.session.getAsyncRemote().sendText("<!--[correct]-->" + ret);
            }
        }
    }

    @OnClose
    public void onClose(Session session)
    {
        System.out.println("onClose : " + session);
        ses.remove(session);
    }
}
